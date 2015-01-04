package com.knownstylenolife.hadoop.scalding

object JobTestHelper {

  type ScaldingLines = List[(String, String)]

  case class Lines(l: String*) {

    val rawLines = l.toList
    val scaldingLines = toScaldingLines(rawLines)

    private def toScaldingLines(lines: List[String]): ScaldingLines =
      for ((line, i) <- lines zip (0 until lines.size)) yield (i.toString -> line)
  }
}