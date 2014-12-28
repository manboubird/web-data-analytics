import sbt._
import Keys._

object BuildSettings {

  lazy val basicSettings = Seq[Setting[_]](
    version       := "0.0.1",
    scalaVersion  := "2.10.4",
    organization  := "com.knownstylenolife",
    scalacOptions := Seq("-deprecation", "-encoding", "utf8"),
    resolvers     ++= Dependencies.resolutionRepos
  )

  import sbtassembly.Plugin._
  import AssemblyKeys._

  lazy val sbtAssemblySettings = assemblySettings ++ Seq(

    jarName in assembly := { s"${name.value}-${version.value}.jar" },

    // Uncomment if you don't want to run all the tests before building assembly
    // test in assembly := {},
    logLevel in assembly := Level.Warn,

    excludedJars in assembly <<= (fullClasspath in assembly) map { cp =>
      val excludes = Set(
        "jsp-api-2.1-6.1.14.jar",
        "jsp-2.1-6.1.14.jar",
        "jasper-compiler-5.5.12.jar",
        "janino-2.5.16.jar", // Janino includes a broken signature, and is not needed
        "minlog-1.2.jar", // Otherwise causes conflicts with Kyro (which bundles it)
        "commons-beanutils-core-1.8.0.jar", // Clash with each other and with commons-collections
        "commons-beanutils-1.7.0.jar",      // "
        "hadoop-tools-2.0.0-mr1-cdh4.5.0.jar" //"
      )
      cp filter { jar => excludes(jar.data.getName) }
    },

    mergeStrategy in assembly <<= (mergeStrategy in assembly) {
      (old) => {
        case "project.clj" => MergeStrategy.discard
        case x => old(x)
      }
    }
  )

  lazy val buildSettings = basicSettings ++ sbtAssemblySettings
}
