package com.knownstylenolife.hadoop.scalding.etl

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import scalaz._
import scalaz.Scalaz._
import scalaz.ValidationNel

object ValidationUtils {

  val IPADDRESS_PATTERN = """^([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3})$""".r;
  
  def validateTime(time: String, pattern: String): ValidatedDateTime = {
    try {
      DateTimeFormat.forPattern(pattern).parseDateTime(time).success
    } catch {
      case e:Throwable => "Fail to convert time [%s] in pattern [%s]: [%s]".format(time, pattern, e.getMessage).failureNel
    }
  }

  def validateIp(ip: String): ValidatedString = ip match {
    case IPADDRESS_PATTERN(s) => s.success 
    case _ => "Illegal host: [%s]".format(ip).failureNel
  }

}
