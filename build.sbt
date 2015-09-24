name := "simple-rest-client"

organization := "org.rga"

version := "1.0"

scalaVersion := "2.11.7"


libraryDependencies ++= Seq(
  "org.apache.httpcomponents" % "httpclient" % "4.5",
  "org.specs2" %% "specs2-core" % "3.6.4" % "test",
  "com.github.tomakehurst" % "wiremock" % "1.57" % "test",
  "junit" % "junit" % "4.12" % "test"
)

javacOptions ++= Seq("-source", "1.7", "-target", "1.7")

scalacOptions ++= Seq("-target:jvm-1.7")

scalacOptions in Test ++= Seq("-Yrangepos")

