val dottyVersion = "0.8.0-RC1"
val scala212Version = "2.12.6"

lazy val root = project
  .in(file("."))
  .settings(
    name := "type-inference-reproducer",
    version := "0.1.0",

    //libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test",

    // To make the default compiler and REPL use Dotty
    scalaVersion := scala212Version,

    // To cross compile with Dotty and Scala 2
    crossScalaVersions := Seq(dottyVersion, scala212Version)
  )
