//package com.bbn.hackerrank
//
//import java.io._
//import scala.io._
//
//
//object ComputingClusterQuality {
//  def main(args: Array[String]) {
//
//    def maximumClusterQuality(speed: Array[Int], reliability: Array[Int], maxMachines: Int): Long = {
//
//    }
//
//    val speedCount = StdIn.readLine.trim.toInt
//
//    val speed = Array.ofDim[Int](speedCount)
//
//    for (i <- 0 until speedCount) {
//      val speedItem = StdIn.readLine.trim.toInt
//      speed(i) = speedItem
//    }
//
//    val reliabilityCount = StdIn.readLine.trim.toInt
//
//    val reliability = Array.ofDim[Int](reliabilityCount)
//
//    for (i <- 0 until reliabilityCount) {
//      val reliabilityItem = StdIn.readLine.trim.toInt
//      reliability(i) = reliabilityItem
//    }
//
//    val maxMachines = StdIn.readLine.trim.toInt
//
//    val result = maximumClusterQuality(speed, reliability, maxMachines)
//
//  }
//}
//
