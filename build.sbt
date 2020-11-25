import sbt.Keys.testFrameworks

ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "dev.ligature"
ThisBuild / organizationName := "Ligature"

val dottyVersion = "3.0.0-M1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "wander",

    scalaVersion := dottyVersion,

    libraryDependencies += ("dev.ligature" %% "ligature" % "0.1.0-SNAPSHOT").withDottyCompat(scalaVersion.value),
    libraryDependencies += "org.typelevel" %% "cats-parse" % "0.1-493581c",
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.16" % Test,
    testFrameworks += new TestFramework("munit.Framework")
  )
