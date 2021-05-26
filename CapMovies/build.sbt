import play.sbt.routes.RoutesKeys

name := "Recipies_New_Play_Version"
 
version := "1.0" 
      
lazy val `recipies_new_play_version` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"
      
scalaVersion := "2.13.5"

libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice )

libraryDependencies += ws


libraryDependencies += "org.scalatest" %% "scalatest-wordspec" % "3.2.2" % "test"
libraryDependencies += "org.scalatest" %% "scalatest-shouldmatchers" % "3.2.2" % "test"
libraryDependencies += "org.scalatestplus" %% "mockito-3-4" % "3.2.5.0" % "test"

libraryDependencies ++= Seq(
  // Enable reactive mongo for Play 2.8
  "org.reactivemongo" %% "play2-reactivemongo" % "0.20.13-play28",
  // Provide JSON serialization for reactive mongo
  "org.reactivemongo" %% "reactivemongo-play-json-compat" % "1.0.1-play28",
  // Provide BSON serialization for reactive mongo
  "org.reactivemongo" %% "reactivemongo-bson-compat" % "0.20.13",
  // Provide JSON serialization for Joda-Time
  "com.typesafe.play" %% "play-json-joda" % "2.7.4",
  //Splice HTML
  "org.jsoup"  %  "jsoup"  % "1.13.1"
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/CapMovies.test" )

RoutesKeys.routesImport += "play.modules.reactivemongo.PathBindables._"

