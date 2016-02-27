import sbt._
import Keys._

object ApplicationBuild extends Build {
  val playIsoVersion = "1.1"

  val scalaVersions = Seq("2.11.7", "2.10.6")
  val playVersion = "2.4.6"
  val validationVersion = "1.1"

  lazy val root = (project in file(".")).settings(
    name := "play-iso-build-slick",
    version := playIsoVersion,
    scalacOptions ++= scalaOptionsVersion(
      scalaVersion.value, flags210 = Seq("-Yfundep-materialization"), Nil
    )
  )

  lazy val bindings = (project in file("bindings")).settings(
    name := "play-iso-slick",
    version := playIsoVersion,
    crossScalaVersions := scalaVersions,
    scalaVersion in ThisBuild := scalaVersions.head,
    scalacOptions ++= scalaOptionsVersion(
      scalaVersion.value, flags210 = Seq("-Xdivergence211"), Nil
    ),
    mappings in (Compile, packageBin) ++= mappings.in(root, Compile, packageBin).value,
    mappings in (Compile, packageSrc) ++= mappings.in(root, Compile, packageSrc).value,
    resolvers in ThisBuild ++= Seq(
      "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/",
      Resolver.sonatypeRepo("releases")
    ),
    libraryDependencies in ThisBuild ++= Seq(
      "com.typesafe.play" %% "play" % playVersion,
      "com.typesafe.play" %% "play-json" % playVersion,
      "io.github.jto" %% "validation-core" % validationVersion,
      "io.github.jto" %% "validation-json" % validationVersion,
      "com.typesafe.slick" %% "slick" % "3.1.1",
      "org.slf4j" % "slf4j-nop" % "1.7.18" % "provided",
      "org.scala-lang" % "scala-reflect" % scalaVersion.value
    )
  )
    .dependsOn(root).aggregate(root)

  // provide scalac flag(s) based on Scala version
  def scalaOptionsVersion(
    scalaVersion: String, flags210: Seq[String], flags211: Seq[String]
  ) =
    (CrossVersion.partialVersion(scalaVersion) match {
      case Some((2, scalaMajor)) if scalaMajor == 10 => flags210
      case Some((2, scalaMajor)) if scalaMajor == 11 => flags211
      case _=> Nil
    })

  // Scala settings
  scalacOptions in ThisBuild ++= Seq(
    "-deprecation",           // Warn when deprecated API are used
    "-feature",               // Warn for usages of features that should be importer explicitly
    "-unchecked",             // Warn when generated code depends on assumptions
    "-Ywarn-dead-code",       // Warn when dead code is identified
    "-Ywarn-numeric-widen",   // Warn when numeric are widened,
    "-Xlint",                 // Additional warnings (see scalac -Xlint:help)
    "-Ywarn-adapted-args"     // Warn if an argument list is modified to match the receive
  )

}
