name := """scram"""

organization := "io.sudostream.api-event-horizon"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.7"

//sbtavrohugger.SbtAvrohugger.avroSettings
sbtavrohugger.SbtAvrohugger.specificAvroSettings

resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"
//resolvers +=
//  "Artifactory" at "http://104.199.1.33/artifactory/libs-snapshot/"
//resolvers +=
//  "Artifactory" at "http://104.199.1.33/artifactory/libs-release/"

libraryDependencies ++= Seq(
  "io.sudostream.api-event-horizon" %% "aeh-messages" % "1.0.0-SNAPSHOT",
  "io.swagger" % "swagger-parser" % "1.0.20",
  "com.typesafe.play" %% "play-json" % "2.3.4",
  "org.apache.avro" % "avro" % "1.8.1",

  // Test related dependencies
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.2.2" % "test",
  "info.cukes" % "cucumber-scala_2.11" % "1.2.4" % "test",
  "info.cukes" % "cucumber-junit" % "1.2.4" % "test",
  "junit" % "junit" % "4.12" % "test"
)

publishTo :=
  {
    val nexus = "https://my.artifact.repo.net/"
    if (isSnapshot.value)
      Some("Artifactory Realm" at "http://104.199.1.33/artifactory/ext-snapshot-local;build.timestamp=" + new java.util.Date().getTime)
    else
      Some("Artifactory Realm" at "http://104.199.1.33/artifactory/ext-release-local;build.timestamp=" + new java.util.Date().getTime)
  }

credentials += Credentials("Artifactory Realm", "[[ip_addr]]", "user", "password")

enablePlugins(CucumberPlugin)

CucumberPlugin.glue := "bdd"
