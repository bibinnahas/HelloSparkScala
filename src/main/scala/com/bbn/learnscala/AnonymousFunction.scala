package com.bbn.learnscala

object AnonymousFunction extends App {

  val doubler = new Function[Int, Int] {
    override def apply(v1: Int): Int = v1 * 2
  }
  val anotherDoubler: Int => Int = x => x * 2

  println(doubler(5))
  println(anotherDoubler(10))
}
