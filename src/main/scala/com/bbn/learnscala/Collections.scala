package com.bbn.learnscala

import scala.util.Random

object Collections extends App {
  val maxRun = 10000
  val maxCapacity = 10000

  val numberList = (1 to maxCapacity).toList
  val numberVector = (1 to maxCapacity).toVector

  def getTimes(collections: Seq[Int]): Double = {
    val r = new Random
    val times = for {
      iter <- 1 to maxRun
    } yield {
      val currentTime = System.nanoTime()
      collections.updated(r.nextInt(maxCapacity), r.nextInt())
      System.nanoTime() - currentTime
    }
    times.sum * 1.0/maxRun
  }
  println(getTimes(numberList))
  println(getTimes(numberVector))
}
