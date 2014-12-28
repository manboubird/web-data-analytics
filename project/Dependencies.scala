import sbt._

object Dependencies {
  val resolutionRepos = Seq(
    "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases",
    "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    "Concurrent Maven Repo" at "http://conjars.org/repo",
    //"Clojars Repository" at "http://clojars.org/repo",
    //"Twitter Maven" at "http://maven.twttr.com",
    "Cloudera Releases" at "https://repository.cloudera.com/artifactory/cloudera-repos"
    ,"Local Ivy2 Repository" at file(Path.userHome.absolutePath + "/.ivy2/local").getAbsolutePath
    ,"Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository"
  )

  object V {
    val scalding     = "0.12.0-mr1-cdh4.5.0" // NOTE: compiled with cdh
    val hadoopCore   = "2.0.0-mr1-cdh4.5.0"
    val hadoopCommon = "2.0.0-cdh4.5.0"
    val hadoopClient = "2.0.0-mr1-cdh4.5.0"
    val specs2       = "2.3.11"//"1.12.3" // -> "1.13" when we bump to Scala 2.10.0
  }

  object Libraries {
    val scaldingCore = "com.twitter"                %% "scalding-core"        % V.scalding
    val hadoopCore   = "org.apache.hadoop"          %  "hadoop-core"          % V.hadoopCore   % "provided"
    val hadoopCommon = "org.apache.hadoop"          %  "hadoop-common"        % V.hadoopCommon % "provided"
    val hadoopClient = "org.apache.hadoop"          %  "hadoop-client"        % V.hadoopClient % "provided"
    // test
    val specs2       = "org.specs2"                 %% "specs2"               % V.specs2       % "test"
  }
}
