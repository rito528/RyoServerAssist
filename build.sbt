ThisBuild / version := "1.8.0"
ThisBuild / scalaVersion := "2.13.7"
ThisBuild / organization := "com.ryoserver"

mainClass := Some("com.ryoserver.RyoServerAssist")

assemblyJarName := {
  s"${name.value}-${version.value}.jar"
}

resolvers ++= Seq(
  "maven.elmakers.com" at "https://maven.elmakers.com/repository/",
  "sk89q-repo" at "https://maven.enginehub.org/repo/",
  "OnARandomBox" at "https://repo.onarandombox.com/content/groups/public/",
  "jitpack" at "https://jitpack.io",
  "repo.phoenix616.dev" at "https://repo.phoenix616.dev",
  "playpro-repo" at "https://maven.playpro.com",
  Resolver.jcenterRepo,
  Resolver.mavenLocal
)

libraryDependencies ++= Seq(
  "org.spigotmc" % "spigot-api" % "1.17.1-R0.1-SNAPSHOT",
  "com.sk89q.worldguard" % "worldguard-bukkit" % "7.0.6",
  "com.sk89q.worldedit" % "worldedit-bukkit" % "7.2.0-SNAPSHOT",
  "com.onarandombox.multiversecore" % "Multiverse-Core" % "4.3.2-SNAPSHOT",
  "com.onarandombox.multiverseportals" % "Multiverse-Portals" % "4.2.2-SNAPSHOT",
  "com.github.nuvotifier.nuvotifier" % "nuvotifier-bukkit" % "2.6.0",
  "net.coreprotect" % "coreprotect" % "20.4"
).map(_ % "provided")

libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "8.0.27",
  "org.apache.httpcomponents" % "httpclient" % "4.5.13",
  "com.beachape" %% "enumeratum" % "1.7.0",
  "com.fasterxml.jackson.core" % "jackson-core" % "2.13.1",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.13.1",
  "com.fasterxml.jackson.core" % "jackson-annotations" % "2.13.1"
)

lazy val root = (project in file("."))
  .settings(
    name := "RyoServerAssist",
    javaOptions ++= Seq("-encoding","utf-8")
  )

assembly / assemblyMergeStrategy := {
  case PathList("javax", "servlet", _*) => MergeStrategy.first
  case PathList(ps@_*) if ps.last endsWith ".properties" => MergeStrategy.first
  case PathList(ps@_*) if ps.last endsWith ".xml" => MergeStrategy.first
  case PathList(ps@_*) if ps.last endsWith ".types" => MergeStrategy.first
  case PathList(ps@_*) if ps.last endsWith ".class" => MergeStrategy.first
  case "application.conf" => MergeStrategy.concat
  case "unwanted.txt" => MergeStrategy.discard
  case x =>
    val oldStrategy = (assembly / assemblyMergeStrategy).value
    oldStrategy(x)
}
