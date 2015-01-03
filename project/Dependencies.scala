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
    val jodaTime     = "2.1"
    val jodaConvert  = "1.2"
    // scala
    val scalaz7      = "7.1.0"
    val json4s       = "3.2.11"
    // test
    val scalaTest    = "2.2.1"
  }

  object Libraries {
    val scaldingCore  = "com.twitter"                %% "scalding-core"        % V.scalding
    val hadoopCore    = "org.apache.hadoop"          %  "hadoop-core"          % V.hadoopCore   % "provided"
    val hadoopCommon  = "org.apache.hadoop"          %  "hadoop-common"        % V.hadoopCommon % "provided"
    val hadoopClient  = "org.apache.hadoop"          %  "hadoop-client"        % V.hadoopClient % "provided"
    val jodaTime      = "joda-time"                  %  "joda-time"            % V.jodaTime
    val jodaConvert   = "org.joda"                   %  "joda-convert"         % V.jodaConvert
    // scala
    val scalaz7       = "org.scalaz"                 %% "scalaz-core"          % V.scalaz7
    val json4sJackson = "org.json4s"                 %% "json4s-jackson"       % V.json4s
    val json4sScalaz  = "org.json4s"                 %% "json4s-scalaz"        % V.json4s    
    // test
    val scalaTest     = "org.scalatest"              %% "scalatest"            % V.scalaTest    % "test"
  }
}
