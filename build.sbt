name := "HelloSparkScala"

version := "0.1"

scalaVersion := "2.12.11"

val sparkVersion = "3.0.0"

libraryDependencies ++= Seq(

  "org.apache.spark" %% "spark-hive" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-catalyst" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion
//  "com.github.nscala-time" %% "nscala-time" % "1.8.0",
//  "com.github.scopt" %% "scopt" % "3.5.0"

)