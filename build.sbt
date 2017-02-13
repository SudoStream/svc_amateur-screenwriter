enablePlugins(JavaAppPackaging)
enablePlugins(UniversalPlugin)
enablePlugins(DockerPlugin)

name := """amateur-screenwriter"""
organization := "io.sudostream.api-antagonist"
version := "0.0.1"
scalaVersion := "2.11.7"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

//sbtavrohugger.SbtAvrohugger.avroSettings
sbtavrohugger.SbtAvrohugger.specificAvroSettings

resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= {
  val akkaV = "2.4.12"
  val akkaHttpVersion = "2.4.11"
  val scalaTestV = "2.2.6"

  Seq(
    "io.sudostream.api-antagonist" %% "messages" % "0.0.1-SNAPSHOT",
    "io.swagger" % "swagger-parser" % "1.0.20",
    "com.typesafe.play" %% "play-json" % "2.3.4",
    "org.apache.avro" % "avro" % "1.8.1",

    // Typesafe stack
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-stream" % akkaV,
    "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-experimental" %akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-stream-kafka" % "0.13",


    // Test related dependencies
    "org.scalatest" %% "scalatest" % "2.2.4" % "test",
    "org.scalamock" %% "scalamock-scalatest-support" % "3.2.2" % "test",
    "info.cukes" % "cucumber-scala_2.11" % "1.2.4" % "test",
    "info.cukes" % "cucumber-junit" % "1.2.4" % "test",
    "junit" % "junit" % "4.12" % "test"
  )
}

enablePlugins(CucumberPlugin)
CucumberPlugin.glue := "bdd"

dockerExposedPorts := Seq(9000)
dockerRepository := Some("eu.gcr.io/api-event-horizon-151020")
dockerUpdateLatest := true
