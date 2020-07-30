package com.bbn.sample

import org.apache.spark.SparkContext
import org.apache.spark._
import org.apache.spark.sql._
import org.apache.log4j._

object Frame {

  val path = "path/to/file"

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
