import scala.io.StdIn

/**
 * Solution for Suspicious activities from logs hackerrank problem
 * Input - stdin - 1.Logs size, 2.Logs (format - sender receiver amount), 3.threshold
 *
 */
object Solution {
  def main(args: Array[String]) {
//    Read input arguments from stdin
    val logsCount = StdIn.readLine.trim.toInt

    val logs = Array.ofDim[String](logsCount)

    for (i <- 0 until logsCount) {
      val logsItem = StdIn.readLine
      logs(i) = logsItem
    }

    val threshold = StdIn.readLine.trim.toInt

    val result: Unit = processLogs(logs, threshold)

  }

  /**
   * This method holds the main logic of the process. Splits the individual transactions
   * and finds the accounts with transactions more than the threshold passed as argument
   * @param logs : input logs in the format [sender recipient ampoount]
   * @param threshold : integer value which is used to store the threshold value above or
   *                  equal to which output should be displayed
   */
  def processLogs(logs: Array[String], threshold: Int): Unit = {

    /**
     * Methos to handle cases when sender and recipient are the same
     * @param lst : List of Lists of individual transactions
     * @return : ditinct elements from each lists
     */
    def removeDuplicates(lst: List[String]): List[String] = lst.distinct

//    Splits the individual transactions and removes amount column which is not required
    val explodeLogs = logs.flatMap(x => x.split(" ")).zipWithIndex
      .filter { case (_, i) => (i + 1) % 3 != 0 }
      .map { case (e, _) => e }.toList

//    Calls remove duplicate method to handle cases where sender and recipient are the same
    val removeDuplicateTransactions = explodeLogs.grouped(2).toList.map(x => removeDuplicates(x)).flatten

//    returns a map with all accounts above threshold
    val result = removeDuplicateTransactions
      .map(x => (x, 1))
      .groupBy(_._1)
      .mapValues(x => {
        x.map(_._2).sum
      })

//    Writes the map keys to an array, sorts and displays in the output format
    val arrResult = result.filter(_._2 >= threshold).keys.toArray.toList.map(_.toString.toInt).sortWith(_ < _)

    arrResult.foreach(println)
  }
}
