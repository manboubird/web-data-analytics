package com.knownstylenolife.hadoop.scalding.etl

import org.json4s.JValue
import org.json4s.JsonDSL.pair2Assoc
import org.json4s.JsonDSL.seq2jvalue
import org.json4s.JsonDSL.string2jvalue
import org.json4s.jackson.JsonMethods.compact

case class BadLine(val line: String, val errors: List[String]) {

  def toJValue: JValue =
    ("line"   -> line) ~
    ("errors" -> errors)

  def toJson: String = compact(this.toJValue)
}