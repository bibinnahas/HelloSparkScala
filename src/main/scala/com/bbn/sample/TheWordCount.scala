package com.bbn.sample

object TheWordCount {
  def main(args: Array[String]): Unit = {
    val sc = Frame.contextSpark("TheWordCount")
    Frame.logger
    val lines = sc.textFile(s"${Frame.path}a/book.txt")
    val words = lines.flatMap(x => x.toLowerCase().split("\\W+")).countByValue()
    val oldStyleWords = lines
      .flatMap(x => x.toLowerCase.split("\\W+"))
      .map(x => (x , 1))
      .reduceByKey(_ + _)
      .map(x => (x._2, x._1))
      .sortByKey(false)

    for (word <- oldStyleWords){
      val count = word._1
      val words = word._2
      println(s"$words: $count")
    }
  }
}
