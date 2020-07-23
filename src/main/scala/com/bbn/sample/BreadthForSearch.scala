package com.bbn.sample

import org.apache.spark.SparkContext
import org.apache.spark.util.LongAccumulator

import scala.collection.mutable.ArrayBuffer

object BreadthForSearch {

  val startCharacterId = 5306
  val targetCharacterId = 14

  var hitCounter: Option[LongAccumulator] = None

  type BFSData = (Array[Int], Int, String)
  type BFSNode = (Int, BFSData)

  def convertToBFS(line: String): BFSNode = {
    val fields = line.split("\\s+")
    val heroId = fields(0).toInt

    var connections: ArrayBuffer[Int] = ArrayBuffer()
    for (connection <- 1 to (fields.length - 1)) {
      connections += fields(connection).toInt
    }

    var color = "WHITE"
    var separation = 9999

    if (heroId == startCharacterId) {
      color = "GRAY"
      separation = 0
    }
    (heroId, (connections.toArray, separation, color))
  }

  def readRdd(sc: SparkContext) = {
    val input = sc.textFile(s"${Frame.path}/Marvel-graph.txt")
    input.map(convertToBFS)
  }

  def mapToBFS(node: BFSNode): Array[BFSNode] = {
    val characterId: Int = node._1
    val data: BFSData = node._2
    val connections = data._1
    val distance = data._2
    var color = data._3

    val results: ArrayBuffer[BFSNode] = ArrayBuffer()

    if(color == "GRAY") {
      for (connection <- connections) {
        val newCharacterId = connection
        val newDistance = distance + 1
        val newColor = "GRAY"

        if (targetCharacterId == connection) {
          if (hitCounter.isDefined) {
            hitCounter.get.add(1)
          }
        }

        val newEntry: BFSNode = (newCharacterId, (Array(), newDistance, newColor))
        results += newEntry
      }
      color = "BLACK"
    }
    val thisEntry: BFSNode = (characterId, (connections, distance, color))
    results += thisEntry
    return results.toArray
  }

  def bfsReduce(data1:BFSData, data2:BFSData): BFSData = {

    // Extract data that we are combining
    val edges1:Array[Int] = data1._1
    val edges2:Array[Int] = data2._1
    val distance1:Int = data1._2
    val distance2:Int = data2._2
    val color1:String = data1._3
    val color2:String = data2._3

    // Default node values
    var distance:Int = 9999
    var color:String = "WHITE"
    var edges:ArrayBuffer[Int] = ArrayBuffer()

    // See if one is the original node with its connections.
    // If so preserve them.
    if (edges1.length > 0) {
      edges ++= edges1
    }
    if (edges2.length > 0) {
      edges ++= edges2
    }

    // Preserve minimum distance
    if (distance1 < distance) {
      distance = distance1
    }
    if (distance2 < distance) {
      distance = distance2
    }

    // Preserve darkest color
    if (color1 == "WHITE" && (color2 == "GRAY" || color2 == "BLACK")) {
      color = color2
    }
    if (color1 == "GRAY" && color2 == "BLACK") {
      color = color2
    }
    if (color2 == "WHITE" && (color1 == "GRAY" || color1 == "BLACK")) {
      color = color1
    }
    if (color2 == "GRAY" && color1 == "BLACK") {
      color = color1
    }
    if (color1 == "GRAY" && color2 == "GRAY") {
      color = color1
    }
    if (color1 == "BLACK" && color2 == "BLACK") {
      color = color1
    }

    return (edges.toArray, distance, color)
  }


  def main(args: Array[String]): Unit = {

    Frame.logger
    val sc = Frame.contextSpark()
    hitCounter = Some(sc.longAccumulator)
    var iterationRdd = readRdd(sc)
    //var iteration = 0
    for (iteration <- 1 to 10) {
      val mapped = iterationRdd.flatMap(mapToBFS)
      println(s"Processing..${mapped.count()} values")
      if (hitCounter.isDefined) {
        val hitCount = hitCounter.get.value
        if (hitCount > 0) {
          println(s"Hit the target from ${hitCount} directions")
          return
        }
      }
      iterationRdd = mapped.reduceByKey(bfsReduce)
    }
  }

}
