package com.knownstylenolife.hadoop.scalding

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.util.ToolRunner

import com.twitter.scalding.{Tool, Args}

trait JobRunner {

  val className: String
  val localModeArgs: Array[String]

  def main(args: Array[String]) {
    args match {
      case Array() => run(localModeArgs) // local mode during development
      case _ => run(Args(args).toList.toArray)
    }
  }

  def run(args:Array[String]) = ToolRunner.run(new Configuration, new Tool, className +: args)

}
