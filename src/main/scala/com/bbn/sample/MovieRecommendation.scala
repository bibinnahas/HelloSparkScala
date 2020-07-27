package com.bbn.sample

import java.nio.charset.CodingErrorAction

import scala.io.{Codec, Source}
import scala.math.sqrt

object MovieRecommendation {

  def loadMovieNames(): Map[Int, String] = {
    implicit val codec: Codec = Codec("UTF-8")
    codec.onMalformedInput(CodingErrorAction.REPLACE)
    codec.onUnmappableCharacter(CodingErrorAction.REPLACE)

    var movieNames: Map[Int, String] = Map()

    val lines = Source.fromFile(s"${Frame.path}../ml-100k/u.item").getLines()
    for (line <- lines) {
      val fields = line.split('|')
      if (fields.length > 1) {
        movieNames += (fields(0).toInt -> fields(1))
      }
    }
    movieNames
  }

  type MovieRating = (Int, Double)
  type UserRatingPair = (Int, (MovieRating, MovieRating))

  def filterDuplicates(userRating: UserRatingPair): Boolean = {
    val movieRating1 = userRating._2._1
    val movieRating2 = userRating._2._2
    val movie1 = movieRating1._1
    val movie2 = movieRating2._1
    movie1 < movie2
  }

  def makePairs(userRating: UserRatingPair): ((Int, Int), (Double, Double)) = {
    val movieRating1 = userRating._2._1
    val movieRating2 = userRating._2._2
    val movie1 = movieRating1._1
    val rating1 = movieRating1._2
    val movie2 = movieRating2._1
    val rating2 = movieRating2._2

    ((movie1, movie2), (rating1, rating2))
  }

  type RatingPair = (Double, Double)
  type RatingPairs = Iterable[RatingPair]

  def computeSimilarity(ratingPairs: RatingPairs): (Double, Int) = {
    var numPairs = 0
    var sum_xx = 0.0
    var sum_yy = 0.0
    var sum_xy = 0.0

    for (pair <- ratingPairs) {
      val ratingX = pair._1
      val ratingY = pair._2

      sum_xx += ratingX * ratingX
      sum_yy += ratingY * ratingY
      sum_xy += ratingX * ratingY
      numPairs += 1
    }
    val numerator = sum_xy
    val denominator = sqrt(sum_xx) * sqrt(sum_yy)

    var score = 0.0
    if (denominator != 0) score = numerator / denominator

    (score, numPairs)
  }

  def main(args: Array[String]): Unit = {
    Frame.logger
    val sc = Frame.contextSpark("MovieRecommendationAlgorithm")
    val movieDict = loadMovieNames()
    val data = sc.textFile(s"${Frame.path}../ml-100k/u.data")
    val ratings = data.map(x => x.split("\t")).map(x => (x(0).toInt, (x(1).toInt, x(2).toDouble)))
    val selfJoinedRatings = ratings.join(ratings)
    val uniqueJoinedRating = selfJoinedRatings.filter(filterDuplicates)
    val moviePairsTemp = uniqueJoinedRating.map(makePairs).groupByKey()
    val moviePairs  = moviePairsTemp.mapValues(computeSimilarity).cache()

    if (args.length > 0) {
      val scoreThreshold = 0.97
      val coOccurenceThreshold = 50.0

      val movieID: Int = args(0).toInt
      val filteredResults = moviePairs.filter(x => {
        val pair = x._1
        val sim = x._2
        (pair._1 == movieID || pair._2 == movieID) && sim._1 > scoreThreshold && sim._2 > coOccurenceThreshold
      })

      val results = filteredResults.map(x => (x._2, x._1)).sortByKey(false).take(10)
      println("\nTop 10 similar movies for " + movieDict(movieID))
      for (result <- results) {
        val sim = result._1
        val pair = result._2
        var similarMovieID = pair._1
        if (similarMovieID == movieID) {
          similarMovieID = pair._2
        }
        println(movieDict(similarMovieID) + "\tscore: " + sim._1 + "\tstrength: " + sim._2)
      }
    }
    moviePairsTemp.foreach(println)
  }
}
