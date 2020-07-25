package com.bbn.sample

import java.nio.charset.CodingErrorAction

import scala.io.{Codec, Source}

object MovieRecommendation {

  def loadMovieNames(): Map[Int, String] = {
    implicit val codec = Codec("UTF-8")
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

  def makePairs(userRating: UserRatingPair) = {
    val movieRating1 = userRating._2._1
    val movieRating2 = userRating._2._2
    val movie1 = movieRating1._1
    val rating1 = movieRating1._2
    val movie2 = movieRating2._1
    val rating2 = movieRating2._2

    ((movie1, movie2), (rating1, rating2))
  }

  def computeSimilarity = {
    ???
  }

  def main(args: Array[String]): Unit = {
    Frame.logger
    val sc = Frame.contextSpark("MovieRecommendationAlgorithm")
    //Load movie names
    val movieDict = loadMovieNames()
    val data = sc.textFile(s"${Frame.path}../ml-100k/u.data")
    val ratings = data.map(x => x.split("\t")).map(x => (x(0).toInt, (x(1).toInt, x(2).toDouble)))
    val selfJoinedRatings = ratings.join(ratings)
    val uniqueJoinedRating = selfJoinedRatings.filter(filterDuplicates)
    val moviePairs = uniqueJoinedRating.map(makePairs).groupByKey().mapValues(computeSimilarity).cache()

    moviePairs.foreach(println)


  }
}
