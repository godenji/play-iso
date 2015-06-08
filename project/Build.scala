import sbt._
import Keys._

object ApplicationBuild extends Build {
  val scalaVersions = Seq("2.10.5", "2.11.6")
  
  lazy val root = (project in file(".")).settings(
    name := "play-iso-build",
    version := "1.1",
    scalacOptions ++= scalaOptionsVersion(
      scalaVersion.value, flags210 = Seq("-Yfundep-materialization"), Nil
    ),
    libraryDependencies +=
      "org.scala-lang" % "scala-reflect" % scalaVersion.value
  )
  lazy val bindings = (project in file("bindings")).settings(
    name := "play-iso",
    version := "1.1",
    crossScalaVersions := scalaVersions,
    scalaVersion in ThisBuild := scalaVersions.head,
    scalacOptions ++= scalaOptionsVersion(
      scalaVersion.value, flags210 = Seq("-Xdivergence211"), Nil
    )
  ).enablePlugins(play.sbt.PlayScala).dependsOn(root).aggregate(root)
  
  // provide scalac flag(s) based on Scala version
  def scalaOptionsVersion(
    scalaVersion: String, flags210: Seq[String], flags211: Seq[String]
  ) =
    (CrossVersion.partialVersion(scalaVersion) match {
      case Some((2, scalaMajor)) if scalaMajor == 10 => flags210
      case Some((2, scalaMajor)) if scalaMajor == 11 => flags211
      case _=> Nil
    })
}
