package com.bbn.sample

import java.nio.charset.CodingErrorAction
import scala.io.{Codec, Source}

object ML {
  def loadMovies:Map[Int, String] = {
    implicit val codec = Codec("UTF-8")
    codec.onMalformedInput(CodingErrorAction.REPLACE)
    codec.onUnmappableCharacter(CodingErrorAction.REPLACE)

    var movieNames: Map[Int, String] = Map()
    val lines = Source.fromFile(s"${Frame.path}/../ml-100k/u.item").getLines()
    for (line <- lines) {
      var fields = line.split("|")
      if (fields.length > 1) {
        movieNames += (fields(0).toInt -> fields(1))
      }
    }
    movieNames
  }

  case class Rating(userId: Int, movieId: Int, rating: Double)

  def main(args: Array[String]): Unit = {

    Frame.logger
    val sc = Frame.contextSpark("MovieRecommendationsALS")
    val nameDict = loadMovies

    val data = sc.textFile("../ml-100k/u.data")
    val ratings = data.map( x => x.split('\t') ).map( x => Rating(x(0).toInt, x(1).toInt, x(2).toDouble) ).cache()

    val als = new ALS()

    println("\nTraining recommendation model...")

  }
}
