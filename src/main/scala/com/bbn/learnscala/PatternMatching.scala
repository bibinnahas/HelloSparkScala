package com.bbn.learnscala

object PatternMatching extends App {

  val numbers = List(1, 2, 3)
  //  Right associativity
  println(1 :: numbers)
  println(numbers.::(1))

  class Person(val name: String, val age: Int)

  object Person {
    def unapply(person: Person): Option[(String, Int)] = {
      if (person.age < 21) None
      else Some((person.name), (person.age))
    }

    def unapply(age: Int): Option[(String)] = Some(if (age < 21) "minor" else "major")
  }

  val bibin = new Person("bibin", 32)
  val greeting = bibin match {
    case Person(n, a) => s"Person matched was $n and age is $a"
    case _ =>
  }

  val legalStatus = bibin.age match {
    case Person(status) => s"My legal status is $status"
  }

  println(greeting)
  println(legalStatus)

}
