package com.bbn.sample

import java.nio.charset.CodingErrorAction

import scala.io.{Codec, Source}

object FavMovies {

  def loadMovieNames() : Map[Int, String] = {

    implicit val codec = Codec("UTF-8")
    codec.onMalformedInput(CodingErrorAction.REPLACE)
    codec.onUnmappableCharacter(CodingErrorAction.REPLACE)

    var movieNames: Map[Int, String] = Map()
    val lines = Source.fromFile(s"${Frame.path}/../ml-100k/u.item").getLines()
    for (line <- lines) {
      val fields = line.split('|')
      if (fields.length > 1) {
        movieNames += (fields(0).toInt -> fields(1))
      }
    }
    movieNames
  }

  def parser(line: String) = {
    (line.split("\t")(1).toInt,1)
  }
  def main(args: Array[String]): Unit = {

    val sc = Frame.contextSpark("FavMovies")
    Frame.logger
    val lines = sc.textFile(s"${Frame.path}/../ml-100k/u.data")
    val nameDict = sc.broadcast(loadMovieNames)
    val mostRated = lines
      .map(parser).reduceByKey(_ + _)
      .map(x => (x._2, x._1))
      .sortByKey(false)
      .map(x => (nameDict.value(x._2), x._1))
      .take(5)

    mostRated.foreach(println)
  }
}
