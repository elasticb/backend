name := "comunications"
version := "0.1"
scalaVersion := "2.12.7"

enablePlugins(PlayScala, PlayNettyServer)
disablePlugins(PlayLayoutPlugin, PlayAkkaHttpServer)

libraryDependencies ++= Seq(
  //  jdbc,
  ehcache,
  ws,
  specs2 % Test,
  guice
)

libraryDependencies ++= Seq (
  "com.typesafe.play" %% "play-slick" % "5.0.0-M4",
  "com.typesafe.slick" %% "slick-codegen" % "3.3.2",
  "org.postgresql" % "postgresql" % "42.2.6",
  "com.github.tminglei" %% "slick-pg" % "0.15.3",
  "com.github.tminglei" %% "slick-pg_play-json" % "0.15.3",
  "com.github.tminglei" %% "slick-pg_jts" % "0.17.2",
  "com.zaxxer" % "HikariCP" % "3.3.1",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.2"
)

routesImport += "com.elasticbot.controllers.utils.Binders._"
routesGenerator := InjectedRoutesGenerator