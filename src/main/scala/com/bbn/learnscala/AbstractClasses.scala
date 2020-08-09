package com.bbn.learnscala

object AbstractClasses extends App {

  abstract class Animal {
    val creatureType: String
    def eat: Unit
  }

  trait Carnivore {
    def eat(animal: Animal): Unit
  }

  class Dog extends Animal {
    override val creatureType: String = "Canine"
    override def eat: Unit = println("nom nom nom")
  }

  class Croc extends Animal with Carnivore {
    override val creatureType: String = "Crocodile"
    override def eat: Unit = println("CRUNCH !!!")
    override def eat(animal: Animal): Unit = println(s"I am a $creatureType")
  }

  val dog = new Dog
  val croc = new Croc
  croc.eat(dog)
  dog.eat
}
