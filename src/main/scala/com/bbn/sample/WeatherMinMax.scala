package com.bbn.sample

import scala.math.min

object WeatherMinMax {

  def parseLine(line: String) = {
    val fields = line.split(",")
    val stationId = fields(0)
    val entryType = fields(2)
    val temperature = fields(3)
    (stationId, entryType, temperature)
  }

  def main(args: Array[String]): Unit = {
    val sc = Frame.contextSpark("MinMaxTemp")
    Frame.logger
    val lines = sc.textFile(s"${Frame.path}/1800.csv")
    val parseLines = lines.map(parseLine)
    val minTemp = parseLines.filter(x => x._2 == "TMIN")
    val stationRdd = minTemp.map(x => (x._1, x._3.toFloat))
    val minStationRdd = stationRdd.reduceByKey( (x, y) => min(x, y)).collect()

    for(result <- minStationRdd.sorted) {
      val station = result._1
      val temp = result._2
      val formattedTemp = f"$temp%.2f F"
      println(s"Temperature of $station was lowest at $formattedTemp")
    }

  }

}
