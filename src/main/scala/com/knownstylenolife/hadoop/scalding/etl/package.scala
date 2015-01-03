package com.knownstylenolife.hadoop.scalding

import org.joda.time.DateTime

import com.knownstylenolife.hadoop.scalding.etl.AccessLogData

import scalaz._
import scalaz.ValidationNel

package object etl {

  /**
   * ValidationNel alias of scalaz
   */
  type Validated[A] = ValidationNel[String, A]

  type ValidatedAccessLogData = Validated[AccessLogData]

  type ValidatedDateTime = Validated[DateTime]

  type ValidatedString = Validated[String]

}
