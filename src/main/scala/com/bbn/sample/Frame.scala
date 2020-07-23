package com.bbn.sample

import org.apache.log4j._
import org.apache.spark.SparkContext

object Frame {

  val path = "/home/thesnibibin/Desktop/SparkScala/SparkScala/"

  def contextSpark(app: String = "DefaultApp") = {
    new SparkContext("local[*]", app)
  }

  def logger = {
    Logger.getLogger("org").setLevel(Level.ERROR)
  }

}
