package com.bbn.sample

import org.apache.spark.SparkContext
import org.apache.spark.sql._
import org.apache.log4j._

object Frame {

  val path = "/home//Desktop/SparkScala/SparkScala"

  def contextSpark(app: String = "DefaultApp"): SparkContext = {
    new SparkContext("local[*]", app)
  }

  def sparkSql(appName: String): SparkSession = {
    SparkSession
      .builder
      .appName(appName)
      .master("local[*]")
      .getOrCreate()
  }

  def logger = {
    Logger.getLogger("org").setLevel(Level.ERROR)
  }

}
