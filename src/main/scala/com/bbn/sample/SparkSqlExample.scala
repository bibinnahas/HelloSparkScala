package com.bbn.sample

import Frame.sparkSql

object SparkSqlExample {

  case class Person(ID: Int, name: String, age: Int, numOfFriends: Int)

  def mapper(line: String): Person = {
    val fields = line.split(",")
    Person(fields(0).toInt, fields(1), fields(2).toInt, fields(3).toInt)
  }

  def main(args: Array[String]): Unit = {
    Frame.logger
    val spark = sparkSql("sample")
    val lines = spark.sparkContext.textFile(s"${Frame.path}/fakefriends.csv")
    import spark.implicits._
    val people = lines.map(mapper).toDS

    people.printSchema()
    people.createOrReplaceTempView("people_table")
    val teenagers = spark.sql("SELECT * FROM people_table WHERE age > 12 and age < 20")
    teenagers.collect().foreach(println)

    spark.stop()

  }
}
