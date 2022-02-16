package com.ryoserver.File

import java.io.{File, PrintWriter}
import java.nio.file.{Files, Paths}
import scala.io.Source

class CreateFiles {

  def createResourcesFile(): Unit = {
    /*
      指定したresourcesにあるファイルが存在しなかった場合に作成する
     */
    List(
      "tips.yml",
      "title.yml",
      "event.yml",
      "Notification.yml"
    ).foreach(f => {
      val is = getClass.getClassLoader.getResourceAsStream(f)
      val fileData = Source.fromInputStream(is).getLines().mkString("\n")
      val file = Paths.get("plugins/RyoServerAssist/" + f)
      if (Files.notExists(file)) {
        file.toFile.createNewFile()
        val pw = new PrintWriter(file.toFile.getPath)
        pw.println(fileData)
        pw.close()
      }
    })
  }

  /*
    flywayによるマイグレーションファイルを作成するにはこの関数にファイルを追加する必要があります。
   */
  def createMigrationFiles(): Unit = {
    new File("plugins/RyoServerAssist/db/migration").mkdirs()
    Set(
      "V1.7.1__CreateTables.sql"
    ).foreach(f => {
      val is = getClass.getClassLoader.getResourceAsStream(s"db/migration/$f")
      val fileData = Source.fromInputStream(is).getLines().mkString("\n")
      val file = Paths.get(s"plugins/RyoServerAssist/db/migration/$f")
      if (Files.notExists(file)) {
        file.toFile.createNewFile()
        val pw = new PrintWriter(file.toFile.getPath)
        pw.println(fileData)
        pw.close()
      }
    })
  }

}
