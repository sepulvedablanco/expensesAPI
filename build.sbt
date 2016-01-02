name := """expensesAPI"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava,PlayEbean)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs
)

libraryDependencies += "postgresql" % "postgresql" % "9.1-901-1.jdbc4"
libraryDependencies += "org.iban4j" % "iban4j" % "3.2.0"
libraryDependencies += "commons-validator" % "commons-validator" % "1.5.0"
libraryDependencies += "org.apache.httpcomponents" % "httpcore" % "4.4.4"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
