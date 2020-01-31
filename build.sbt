import sbt._
import Keys._
import Dependencies._

lazy val buildSettings = Seq(
  organization := "io.tabmo",
  scalaVersion := Version.scala
)

lazy val compilerOptions = Seq(
  "-language:postfixOps",
  "-language:reflectiveCalls",
  "-deprecation",                      // Emit warning and location for usages of deprecated APIs.
  "-encoding", "utf-8",                // Specify character encoding used by source files.
  "-explaintypes",                     // Explain type errors in more detail.
  "-feature",                          // Emit warning and location for usages of features that should be imported explicitly.
  "-language:existentials",            // Existential types (besides wildcard types) can be written and inferred
  "-language:experimental.macros",     // Allow macro definition (besides implementation and application)
  "-language:higherKinds",             // Allow higher-kinded types
  "-language:implicitConversions",     // Allow definition of implicit functions called views
  "-unchecked",                        // Enable additional warnings where generated code depends on assumptions.
  "-Xcheckinit",                       // Wrap field accessors to throw an exception on uninitialized access.
  "-Xfatal-warnings",                  // Fail the compilation if there are any warnings.
  "-Xfuture",                          // Turn on future language features.
  "-Xlint",                            // Additional warnings (see scalac -Xlint:help)
  "-Yno-adapted-args",                 // Do not adapt an argument list (either by inserting () or creating a tuple) to match the receiver.
  "-Ypartial-unification",             // Enable partial unification in type constructor inference
  "-Ywarn-dead-code",                  // Warn when dead code is identified.
  "-Ywarn-inaccessible",               // Warn about inaccessible types in method signatures.
  "-Ywarn-infer-any",                  // Warn when a type argument is inferred to be `Any`.
  "-Ywarn-nullary-override",           // Warn when non-nullary `def f()' overrides nullary `def f'.
  "-Ywarn-nullary-unit",               // Warn when nullary methods return Unit.
  "-Ywarn-numeric-widen",              // Warn when numerics are widened.
  "-Ywarn-unused:implicits",           // Warn if an implicit parameter is unused.
  "-Ywarn-unused:imports",             // Warn if an import selector is not referenced.
  "-Ywarn-unused:locals",              // Warn if a local definition is unused.
  "-Ywarn-unused:patvars",             // Warn if a variable bound in a pattern is unused.
  "-Ywarn-unused:privates",            // Warn if a private member is unused.
  "-Ywarn-value-discard"               // Warn when non-Unit expression results are unused.
)

lazy val macroProjectSettings = Seq(
  libraryDependencies += "org.scala-lang" % "scala-reflect" % Version.scala
)

lazy val baseSettings = Seq(
  scalacOptions in (Compile, console) := compilerOptions,
  scalacOptions in (Compile, test) := compilerOptions,
  resolvers ++= Seq(
    "Typesafe Releases" at "https://repo.typesafe.com/typesafe/releases/",
    Resolver.sonatypeRepo("releases")
  )
)

lazy val publishSettings = Seq(
  licenses += ("Apache-2.0", url("https://opensource.org/licenses/Apache-2.0")),
  bintrayOrganization := Some("tabmo")
)

lazy val allSettings = buildSettings ++ baseSettings ++ publishSettings

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
    "org.slf4j" % "slf4j-nop" % "1.7.30" % "provided"
  )
) .settings(allSettings: _*)
  .dependsOn(isoMacro).aggregate(isoMacro)

