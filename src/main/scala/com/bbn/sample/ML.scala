package com.bbn.sample

import java.nio.charset.CodingErrorAction

import org.apache.spark.sql.SparkSession

import scala.io.{Codec, Source}

import org.apache.spark.SparkContext._
import org.apache.log4j._
import scala.io.Source
import java.nio.charset.CodingErrorAction
import scala.io.Codec
import org.apache.spark.ml.recommendation._

object ML {
  def loadMovies: Map[Int, String] = {
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
    import org.apache.spark

    Frame.logger
    val spark = SparkSession
      .builder()
      .appName("ALS Sample")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    val nameDict = loadMovies

    val data = spark.read.textFile("../ml-100k/u.data")
    val ratings = data.map(x => x.split('\t')).map(x => Rating(x(0).toInt, x(1).toInt, x(2).toDouble)).toDF()

    println("\nTraining recommendation model...")

    val als = new ALS()
      .setMaxIter(5)
      .setRegParam(0.01)
      .setUserCol("userId")
      .setItemCol("movieId")
      .setRatingCol("rating")

    val model = als.fit(ratings)

    val userid: Int = args(0).toInt
    val users = Seq(userid).toDF("userId")
    val recommendations = model.recommendForAllUsers(10)

    println("\nTop 10 recommendations for user" + userid + ":")



  }
}
