name := "comunications"
version := "0.1"
scalaVersion := "2.12.7"

enablePlugins(PlayScala, PlayNettyServer)
disablePlugins(PlayLayoutPlugin, PlayAkkaHttpServer)

libraryDependencies ++= Seq(
  ehcache,
  ws,
  specs2 % Test,
  guice
)

val elastic4sVersion = "6.3.7"
val slickPGExtensionsVersion = "0.17.2"

libraryDependencies ++= Seq (
  "com.typesafe.play" %% "play-slick" % "5.0.0-M4",
  "com.typesafe.slick" %% "slick-codegen" % "3.3.2",
  "org.postgresql" % "postgresql" % "42.2.6",
  "com.zaxxer" % "HikariCP" % "3.3.1",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.2",

  "com.sksamuel.elastic4s" %% "elastic4s-core" % elastic4sVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-http" % elastic4sVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-embedded" % elastic4sVersion,


  "com.github.tminglei" %% "slick-pg" % slickPGExtensionsVersion,
  "com.github.tminglei" %% "slick-pg_play-json" % slickPGExtensionsVersion,

  "org.apache.httpcomponents" % "httpclient" % "4.5.9",
  
  "org.typelevel" %% "cats-core" % "1.6.0",
  "org.typelevel" %% "cats-core" % "1.6.0",
  "org.typelevel" %% "cats-effect" % "1.2.0",
) ++ testDependencies

val testDependencies = Seq (
  "com.sksamuel.elastic4s" %% "elastic4s-testkit" % elastic4sVersion % Test,
  "com.spotify" % "docker-client" % "3.5.12" % Test
)

routesImport += "com.elasticbot.controllers.utils.Binders._"
routesGenerator := InjectedRoutesGenerator