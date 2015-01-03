package com.knownstylenolife.hadoop.scalding.etl

import org.joda.time.DateTime

object AccessLogData {

  def toOption(s: String): Option[String] = Option(s) match {
    case Some("-") => None
    case Some("")  => None
    case s         => s
  }
}

final case class AccessLogData(
  time: DateTime,
  host: String,
  reqMethod: String,
  reqParam: String,
  status: String,
  referer: Option[String],
  ua: Option[String],
  ext: Option[String]
)
