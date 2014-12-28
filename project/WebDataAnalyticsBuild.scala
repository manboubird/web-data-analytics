import sbt._
import Keys._

object WebDataAnalyticsBuild extends Build {

  import Dependencies._
  import BuildSettings._

  override lazy val settings = super.settings :+ {
    shellPrompt := { s => Project.extract(s).currentProject.id + " > " }
  }

  lazy val project = Project("web-data-analytics", file("."))
    .settings(buildSettings: _*)
    .settings(
      libraryDependencies ++= Seq(
        Libraries.scaldingCore,
        Libraries.hadoopCore,
        Libraries.hadoopCommon,
        Libraries.hadoopClient,
        Libraries.scalaTest
      )
    )
}
