package com.bbn.sample

object CustomerOrder {

  def parseLines(lines: String): (Int, Int, Float) = {
    val line = lines.split(",")
    val custId = line(0).toInt
    val prodId = line(1).toInt
    val amtSpent = line(2).toFloat
    (custId, prodId, amtSpent)
  }

  def main(args: Array[String]): Unit = {
    val sc = Frame.contextSpark("RatingsCounter")
    Frame.logger
    val lines = sc.textFile(s"${Frame.path}/customer-orders.csv")
    val amountRdd = lines
      .map(parseLines)
      .map(x => (x._1, x._3))
      .reduceByKey(_ + _)
      .map(x => (x._2, x._1))
      .sortByKey(false)
      .take(5)

    amountRdd.foreach(println)
  }
}
