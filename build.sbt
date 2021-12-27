name := "RyoServerAssist"

version := "1.5.0"

scalaVersion := "2.13.6"

mainClass := Some("com.ryoserver.RyoServerAssist")

assemblyJarName := {
  s"${name.value}-${version.value}.jar"
}

resolvers += "maven.elmakers.com" at "https://maven.elmakers.com/repository/"
resolvers += "sk89q-repo" at "https://maven.enginehub.org/repo/"
resolvers += "OnARandomBox" at "https://repo.onarandombox.com/content/groups/public/"
resolvers += "jitpack" at "https://jitpack.io"
resolvers += Resolver.jcenterRepo
resolvers += Resolver.bintrayIvyRepo("com.eed3si9n", "sbt-plugins")
resolvers += Resolver.mavenLocal

libraryDependencies += "org.spigotmc" % "spigot-api" % "1.17.1-R0.1-SNAPSHOT" % "provided"
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.27"
libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.5.13"
libraryDependencies += "com.sk89q.worldguard" % "worldguard-bukkit" % "7.0.6" % "provided"
libraryDependencies += "com.sk89q.worldedit" % "worldedit-bukkit" % "7.2.0-SNAPSHOT" % "provided"
libraryDependencies += "com.onarandombox.multiversecore" % "Multiverse-Core" % "4.3.2-SNAPSHOT" % "provided"
libraryDependencies += "com.onarandombox.multiverseportals" % "Multiverse-Portals" % "4.2.2-SNAPSHOT" % "provided"
libraryDependencies += "com.github.nuvotifier.nuvotifier" % "nuvotifier-bukkit" % "2.6.0" % "provided"
libraryDependencies += "com.beachape" %% "enumeratum" % "1.7.0"
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-core" % "2.13.1"
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.13.1"
dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-annotations" % "2.13.1"

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