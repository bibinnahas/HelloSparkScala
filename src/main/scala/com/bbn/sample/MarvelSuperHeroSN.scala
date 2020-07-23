package com.bbn.sample

import java.nio.charset.CodingErrorAction

import scala.io.{Codec, Source}

object MarvelSuperHeroSN {

  def parseNames(lines: String): Option[(Int, String)] = {
    val fields = lines.split("\"")
    if (fields.length > 1) Some(fields(0).trim.toInt, fields(1)) else None
  }

  def parseGraph(line: String) = {
    val fields = line.split("\\s+")
    (fields(0).toInt, fields.length - 1)
  }

  def main(args: Array[String]): Unit = {
    Frame.logger
    val sc = Frame.contextSpark("MarvelSuperHeroSample")
    val names = sc.textFile(s"${Frame.path}/Marvel-names.txt")
    val namesRdd = names.flatMap(parseNames)
    val graph = sc.textFile(s"${Frame.path}/Marvel-graph.txt")
    val graphRdd = graph.map(parseGraph).reduceByKey(_ + _).map(x => (x._2, x._1)).max()
    val mostPopularHero = namesRdd.lookup(graphRdd._2)(0)
    print(s"The most popular Marvel hero is $mostPopularHero and has ${graphRdd._1} referrences")
  }
}
