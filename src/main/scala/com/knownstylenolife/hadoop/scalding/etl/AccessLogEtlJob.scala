package com.knownstylenolife.hadoop.scalding.etl

import com.knownstylenolife.hadoop.scalding.JobRunner
import com.twitter.scalding.Args
import com.twitter.scalding.Job
import com.twitter.scalding.TextLine
import com.twitter.scalding.Tsv

import scalaz._
import scalaz.Scalaz._

object AccessLogEtlJob extends JobRunner {
  override val className = classOf[AccessLogEtlJob].getName
  override val localModeArgs = Array("--local",
    "--inDir", "data/splunk/www1/access.log",
    "--outDir", "target/AccessLogEtlJob/out",
    "--badDir", "target/AccessLogEtlJob/bad",
    "--exceptionDir", "target/AccessLogEtlJob/exception")
}

class AccessLogEtlJob(args : Args) extends Job(args) {
    
  val inDir = TextLine(args("inDir"))
  val input = args.optional("exceptionDir") match {
    case Some(dir) => inDir.read.addTrap(Tsv(dir))
    case None => inDir.read
  }
  
  val goodOutput = Tsv(args("outDir"))
  val badOutput = Tsv(args("badDir"))

  // 12 tuples
  private val APACHE_LOG_REGEX = """^([^ ]*) ([^ ]*) ([^ ]*) \[([^\]]*)\] "([^ ]*) *(([^ ]*) *([^"]*))?" ([^ ]*) ([^ ]*) "(.*?)" "(.*?)" (.*)""".r
 
  private val TIME_FORMAT_PATTERN = "dd/MMM/yyyy:HH:mm:ss"

  def process(line:String): ValidatedAccessLogData = {

    def build(time:String, host:String, reqMethod:String, reqParam:String, status:String, referer:String, ua:String, ext: Option[String]): ValidatedAccessLogData = {

      val validatedTime = ValidationUtils.validateTime(time, TIME_FORMAT_PATTERN)
      val validatedHost = ValidationUtils.validateIp(host)

      (validatedTime |@| validatedHost) { (t, h) =>
        new AccessLogData(
          t,
          h, 
          reqMethod, 
          reqParam, 
          status, 
          AccessLogData.toOption(referer), 
          AccessLogData.toOption(ua), 
          ext
        )
      }
    }

    line match {
      case APACHE_LOG_REGEX(host, _, _, time, reqMethod, _, reqParam, _, status, _, referer, ua, ext) =>
        build(time, host, reqMethod, reqParam, status, referer, ua, ext.some)
      case APACHE_LOG_REGEX(host, _, _, time, reqMethod, _, reqParam, _, status, _, referer, ua, null) =>
        build(time, host, reqMethod, reqParam, status, referer, ua, None)
      case _ =>
        "Line does not match log format".failureNel[AccessLogData]
    }
  }
  
 def common = input.map('line -> 'all){ line: String => 
    process(line) 
  }
  
 val bad = common
    .filter('all) { f: ValidatedAccessLogData => f.isFailure }
    .map('all -> 'errors) { f: ValidatedAccessLogData => 
      f.swap.toOption match { 
        case Some(errs) => errs
        case None => throw new RuntimeException // it's impossible to reach here
      }
    }
    .mapTo(('line, 'errors) -> 'json) { f: (String, NonEmptyList[String]) =>
      val (line, errors) = f
      BadLine(line, errors.toList).toJson
    }   
    .write(badOutput)
    
  val good = common
    .filter('all) { f: ValidatedAccessLogData => f.isSuccess }
    .project('line)
    .write(goodOutput)

}