package com.knownstylenolife.hadoop.scalding.etl.excite

import com.knownstylenolife.hadoop.scalding.JobRunner
import com.twitter.scalding.Args
import com.twitter.scalding.Job
import com.twitter.scalding.Tsv
import com.twitter.scalding.TypedPipe
import com.twitter.scalding.TypedTsv
import com.twitter.scalding.WritableSequenceFile

import cascading.pipe.Pipe

object ExciteEtlJob extends JobRunner {
  override val className = classOf[ExciteEtlJob].getName
  override val localModeArgs = Array("--local",
    "--inputFormat", "SequenceFile"
    ,"--inDir", "data/user/hive/warehouse/excite_act/dt=2013-02-08/type=search/000000_0"
    ,"--outDir", "target/ExciteEtlJob/out"
    ,"--exceptionDir", "target/ExciteEtlJob/exception"
  )
}

object Act {
  def fromTuple(t:(String, String, String, String, String)):Act = Act(t._1, t._2, t._3, t._4, t._5)
}
case class Act(ts: String, sid: String, ext: String, dt: String, ttype: String) {
  def toTuple:(String, String, String, String, String) = { (ts, sid, ext, dt, ttype) }
}

class ExciteEtlJob(args: Args) extends Job(args) {

  def readSequenceFile(path: String, dt: String, ttype: String): TypedPipe[Act] = {
    val SEPARATOR = """\u0001"""
    val inDir = WritableSequenceFile(path, ('offset, 'line))
    
    val rawPipe: Pipe = args.optional("exceptionDir") match {
      case Some(dir) => inDir.read.addTrap(Tsv(dir))
      case None => inDir.read
    }
    
    val lines: TypedPipe[(String, String)] = TypedPipe.from[(String, String)](rawPipe, ('offset, 'line))
    lines.map {
      case (offset, line) =>
        val splitted = line.split(SEPARATOR)
        splitted match {
          case Array(a, b) => Act.fromTuple(a, b, "", dt, ttype)
          case Array(a, b, c) => Act.fromTuple(a, b, c, dt, ttype)
          case _ => throw new RuntimeException
        }
    }
  }

  val inDir = args("inDir")
  val goodOutput = TypedTsv[(String, String, String)](args("outDir"))

  val dt = "2013-02-08"
  val ttype = "search"
  
  val inputRaw:TypedPipe[Act] = readSequenceFile(inDir, dt, ttype)
  inputRaw.map { act => 
    (act.ts, act.sid, act.ext) 
  }.write(goodOutput)

}
