name := "akka-quick-sort"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.5"

scalacOptions ++= Seq("-deprecation", "-feature")

resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.3" % "test"

libraryDependencies += "junit" % "junit" % "4.12" % "test"

libraryDependencies += "org.specs2" %% "specs2" % "2.4.16"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.10"

libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.3.10"