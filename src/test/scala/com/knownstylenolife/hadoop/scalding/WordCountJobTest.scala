package com.knownstylenolife.hadoop.scalding

import com.twitter.scalding.{Tsv, TextLine, JobTest}
import org.scalatest.{FlatSpec, Matchers}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class WordCountJobTest extends FlatSpec with Matchers {

  "WordCount job" should "count words" in {
    JobTest(classOf[WordCountJob].getName).
      arg("input", "inputFile").
      arg("output", "outputFile").
      source(TextLine("inputFile"), List("0" -> "a a b")).
      sink[(String, Int)](Tsv("outputFile")){ outputBuffer =>
        val outMap = outputBuffer.toMap
          outMap("a") should equal(2)
          outMap("b") should equal(1)
      }.run.finish
  }
}