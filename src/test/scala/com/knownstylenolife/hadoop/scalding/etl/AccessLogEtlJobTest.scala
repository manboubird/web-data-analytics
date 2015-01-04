package com.knownstylenolife.hadoop.scalding.etl

import scala.collection.mutable.Buffer
import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import com.knownstylenolife.hadoop.scalding.JobTestHelper.Lines
import com.twitter.scalding.JobTest
import com.twitter.scalding.TextLine
import com.twitter.scalding.Tsv
import cascading.tuple.TupleEntry
import org.scalatest.junit.JUnitRunner

object AccessLogLine {

  val lines = Lines(
    """192.168.1.1 - - [01/Dec/2012:01:00:00] "GET /test?pId=001&JSESSIONID=SID012FF7ADFF4953 HTTP 1.1" 200 1352 "http://www.example.com/abc.html" "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.46 Safari/536.5" ext001""",
    """209.160.24.63 - - [20/Dec/2014:18:22:16] "GET /test?productId=WC-SH-A02&JSESSIONID=SD0SL6FF7ADFF4953 HTTP 1.1" 200 3878 "http://www.google.com" "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.46 Safari/536.5" """"
  )
}

@RunWith(classOf[JUnitRunner])
class AccessLogEtlJobTest extends FlatSpec with Matchers {

  "AccessLogEtl job" should "output good and bad access logs" in {
    JobTest(classOf[AccessLogEtlJob].getName).
      arg("inDir", "inDir").
      arg("outDir", "outDir").
      arg("badDir", "badDir").
      arg("exceptionDir", "exceptionDir").
      source(TextLine("inDir"), AccessLogLine.lines.scaldingLines).
      sink[String](Tsv("outDir")){ buf : Buffer[String] =>
        val lines = buf.toList
        val expected = AccessLogLine.lines.rawLines
        buf.size should equal(expected.size)
        for ((line, i) <- lines zip (0 until lines.size))
          line should equal(expected(i))
      }.
      sink[TupleEntry](Tsv("badDir")){ error : Buffer[TupleEntry] =>
        error shouldBe empty
      }.
      sink[TupleEntry](Tsv("exceptionDir")){ trap =>
        trap shouldBe empty
      }.
      run.finish
  }
}
