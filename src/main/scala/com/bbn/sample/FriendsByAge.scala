package com.bbn.sample

object FriendsByAge {

  def parseFile(line: String) = {
    val fields = line.split(",")
    val age = fields(2).toInt
    val numFriends = fields(3).toInt
    (age, numFriends)
  }

  def main(args: Array[String]) = {
    val sc = Frame.contextSpark("FriendsByAge")
    Frame.logger
    val lines = sc.textFile(s"${Frame.path}/fakeFriends.csv")
    val rdd = lines.map(parseFile)
    val totalsByAge = rdd.mapValues(x => (x, 1)).reduceByKey((x,y) => (x._1 + y._1,x._2 + y._2))
    val averagesByAge = totalsByAge.mapValues(x => x._1/x._2)
    val results = averagesByAge.collect()
    results.sorted.foreach(println)
    //totalsByAge.collect().sorted.foreach(println)

  }

}
