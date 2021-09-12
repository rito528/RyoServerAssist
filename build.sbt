name := "RyoServerAssist"

version := "1.0.0"

scalaVersion := "2.13.6"

mainClass := Some("com.gmail.rotoyutoriapp.HomeSystem")

assemblyJarName := { s"${name.value}-${version.value}.jar" }

resolvers += "spigot-repo" at "https://hub.spigotmc.org/nexus/content/repositories/snapshots/"
resolvers += Resolver.jcenterRepo
resolvers += Resolver.bintrayIvyRepo("com.eed3si9n", "sbt-plugins")
resolvers += Resolver.mavenLocal
libraryDependencies += "org.spigotmc" % "spigot-api" % "1.17.1-R0.1-SNAPSHOT" % "provided"
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.25"


assemblyMergeStrategy in assembly := {
  case PathList("javax", "servlet", xs @ _*)         => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".properties" => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".xml" => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".types" => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".class" => MergeStrategy.first
  case "application.conf"                            => MergeStrategy.concat
  case "unwanted.txt"                                => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}
