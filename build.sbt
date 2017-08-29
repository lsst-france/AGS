name := "AGS"

val jts = "com.vividsolutions" % "jts" % "1.13"
//val spark = "org.apache.spark" %% "spark-core" % "1.6.1" % "provided"
val spark = "org.apache.spark" %% "spark-core" % "2.1.0" % "provided"
//val sparkSQL = "org.apache.spark" %% "spark-sql" % "1.6.1" % "provided"
val sparkSQL = "org.apache.spark" %% "spark-sql" % "2.1.0" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "2.2.4" % "test"
//val sparkTestingBase = "com.holdenkarau" %% "spark-testing-base" % "1.3.0_0.0.5" % "test"

version := "0.1"
scalaVersion := "2.11.7"
scalacOptions ++= Seq("-encoding", "UTF-8", "-unchecked", "-deprecation", "-feature")
parallelExecution := false

libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _ % "provided")
libraryDependencies ++= Seq(jts, scalaTest, spark, sparkSQL)

// https://mvn.repository.com/artifact/com.vividsolutions/jts
//libraryDependencies += "com.vividsolutions" % "jts" % "1.13"
//libraryDependencies += "org.apach.spark" %% "spark-core" % "2.0.0"
