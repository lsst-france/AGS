name := "AGS"

version := "0.1"

scalaVersion := "2.11.7"

val jts = "com.vividsolutions" % "jts" % "1.13"
val spark = "org.apache.spark" %% "spark-core" % "2.1.0" % "provided"
val sparkSQL = "org.apache.spark" %% "spark-sql" % "2.1.0" % "provided"

scalacOptions ++= Seq("-encoding", "UTF-8", "-unchecked", "-feature")

libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _ % "provided")
libraryDependencies ++= Seq(jts, spark, sparkSQL)
