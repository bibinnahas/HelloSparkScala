import scala.io.StdIn

object SuspeciosActivityfromLogs {
  def main(args: Array[String]) {
    val logsCount = StdIn.readLine.trim.toInt

    val logs = Array.ofDim[String](logsCount)

    for (i <- 0 until logsCount) {
      val logsItem = StdIn.readLine
      logs(i) = logsItem
    }

    val threshold = StdIn.readLine.trim.toInt

    val result: Unit = processLogs(logs, threshold)

  }

  def processLogs(logs: Array[String], threshold: Int): Unit = {

    def removeDuplicates(lst: List[String]): List[String] = lst.distinct

    val explodeLogs = logs.flatMap(x => x.split(" ")).zipWithIndex
      .filter { case (_, i) => (i + 1) % 3 != 0 }
      .map { case (e, _) => e }.toList

    val removeDuplicateTransactions = explodeLogs.grouped(2).toList.map(x => removeDuplicates(x)).flatten

    val result = removeDuplicateTransactions
      .map(x => (x, 1))
      .groupBy(_._1)
      .mapValues(x => {
        x.map(_._2).sum
      })

    val arrResult = result.filter(_._2 >= threshold).keys.toArray.toList.map(_.toString.toInt).sortWith(_ < _)

    arrResult.foreach(println)
  }
}
