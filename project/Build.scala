import sbt._
import Keys._

object ApplicationBuild extends Build {

  object Version {
    val playiso = "1.2"
    val scala = "2.11.7"

    object Dependencies {
      val play = "2.4.6"
      val validation = "1.1"
      val slick = "3.1.1"
    }
  }

  lazy val buildSettings = Seq(
    organization := "io.tabmo",
    version := Version.playiso,
    scalaVersion := Version.scala
  )

  lazy val compilerOptions = Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-unchecked",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Xfuture"
  )

  lazy val macroProjectSettings = Seq(
    libraryDependencies <+= (scalaVersion)(
      "org.scala-lang" % "scala-reflect" % _
    )
  )

  lazy val baseSettings = Seq(
    scalacOptions in (Compile, console) := compilerOptions,
    scalacOptions in (Compile, test) := compilerOptions,
    resolvers ++= Seq(
      "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/",
      Resolver.sonatypeRepo("releases")
    )
  )

  lazy val allSettings = buildSettings ++ baseSettings

  lazy val isoMacro = (project in file("macro")).settings(
    name := "play-iso-macro-slick",
    libraryDependencies ++= Seq(
      "com.typesafe.slick" %% "slick" % Version.Dependencies.slick
    )
  ) .settings(allSettings: _*)
    .settings(macroProjectSettings: _*)



  lazy val root = (project in file(".")).settings(
    name := "play-iso-slick",
    mappings in (Compile, packageBin) ++= mappings.in(isoMacro, Compile, packageBin).value,
    mappings in (Compile, packageSrc) ++= mappings.in(isoMacro, Compile, packageSrc).value,
    libraryDependencies ++= Seq(
      "com.typesafe.play" %% "play" % Version.Dependencies.play,
      "com.typesafe.play" %% "play-json" % Version.Dependencies.play,
      "io.github.jto" %% "validation-core" % Version.Dependencies.validation,
      "io.github.jto" %% "validation-json" % Version.Dependencies.validation,
      "org.slf4j" % "slf4j-nop" % "1.7.18" % "provided"
    )
  ) .settings(allSettings: _*)
    .dependsOn(isoMacro).aggregate(isoMacro)


}
