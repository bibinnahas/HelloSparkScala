package com.bbn.learnscala

object SuntatacticSugar extends App {

  class TeenBoy(name: String) {
    def `thought and said`(rude: String): Unit = println(s"$name said $rude")
  }

  val bibin = new TeenBoy("bibin")
  bibin `thought and said` "@$#$@#$"

}
