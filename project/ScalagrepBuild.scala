import sbt._
import sbt.Keys._

object ScalagrepBuild extends Build {

  lazy val scalagrep = Project(
    id = "scala-grep",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "scala-grep",
      organization := "com.abishekk92",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.10.4",
      libraryDependencies ++= Seq("org.scalatest" % "scalatest_2.10" % "2.2.0" % "test",
                                  "org.yaml" % "snakeyaml" % "1.13")
    )
  )
}
