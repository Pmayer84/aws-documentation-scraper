import Dependencies._
import sbt.Keys.libraryDependencies

ThisBuild / scalaVersion     := "2.13.7"  // Updated Scala version if available
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "io.scalastic"
ThisBuild / organizationName := "scalastic"

lazy val root = (project in file("."))
  .settings(
    name := "aws-doc-scraper",
    resolvers += "Rally Health" at "https://dl.bintray.com/rallyhealth/maven",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      "com.softwaremill.sttp.client3" %% "core" % "3.8.0", // Updated version
      "com.lihaoyi" %% "upickle" % "1.4.0",  // Updated version
      "org.json" % "json" % "20211205",  // Updated version
      "org.scala-lang.modules" %% "scala-xml" % "2.0.1"
    )
  )
