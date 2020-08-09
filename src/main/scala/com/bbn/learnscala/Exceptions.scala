package com.bbn.learnscala

object Exceptions extends App {
  class OverFlowException extends RuntimeException("Value is bad")
  class UnderFlowException extends RuntimeException

  object PocketCalculator {
    def add(x: Int, y: Int): Int = {
      val result = x + y
      if (x > 0 && y > 0 && result < 0) throw new OverFlowException
      else if (x < 0 && y < 0 && result > 0) throw new UnderFlowException
      else result
    }
  }
  println(PocketCalculator.add(Int.MaxValue, 1))
}
