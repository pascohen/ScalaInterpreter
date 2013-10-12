name := "scalaInterpreter"

version := "1.0"

resolvers += Resolver.sonatypeRepo("snapshots")

scalaVersion := "2.11.0-SNAPSHOT"

scalacOptions += "-deprecation"

scalacOptions += "-feature"

libraryDependencies += "org.scala-lang"%"scala-compiler"%"2.11.0-SNAPSHOT"
