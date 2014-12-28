package com.knownstylenolife.hadoop.scalding

import com.twitter.scalding._

class WordCountJob(args : Args) extends Job(args) {
  TextLine( args("input") )
    .flatMap('line -> 'word) { line : String => tokenize(line) }
    .groupBy('word) { _.size }
    .write(Tsv(args("output")))

  def tokenize(text: String): Array[String] = text.toLowerCase.replaceAll("[^a-zA-Z0-9\\s]", "").split("\\s+")
}

object WordCountJob extends JobRunner {
  override val className = classOf[WordCountJob].getName
  override val localModeArgs = Array("--local",
    "--input", "data/excite/excite-small.log",
    "--output", "target/WordCountJob/output")
}