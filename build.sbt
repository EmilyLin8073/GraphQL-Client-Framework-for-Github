name := "Project"

version := "0.1"

scalaVersion := "2.13.2"

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "org.scalatest" %% "scalatest" % "3.1.1" % "test",
  "com.novocode" % "junit-interface" % "0.11" % "test",
  "com.typesafe.play" %% "play-json" % "2.7.4",
  "org.apache.httpcomponents" % "httpclient" % "4.5.10",
  "org.json4s" %% "json4s-native" % "latest.integration",
  "com.typesafe" % "config" % "1.4.0")
