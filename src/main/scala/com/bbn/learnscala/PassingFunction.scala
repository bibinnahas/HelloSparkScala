package com.bbn.learnscala

object PassingFunction extends App {
  def cubeIt(x: Int): Int = x * x * x

  def transform(x: Int, f: Int => Int): Int = f(x)

  println(transform(2, cubeIt))

  println(transform(2, x => x * x))

}
