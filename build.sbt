scalaVersion := "2.11.8"

name := "conwaysjs"

enablePlugins(ScalaJSPlugin)

scalaJSUseRhino in Global := false

libraryDependencies ++= Seq(
  "org.scala-js"  %%% "scalajs-dom" % "0.9.0",
  "com.chuusai"   %%% "shapeless"   % "2.3.0",
  "org.scalatest" %%% "scalatest"   % "3.0.0-M15" % "test"
)

