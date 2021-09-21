package com.ryoserver.File

import java.io.PrintWriter
import java.nio.file.{Files, Paths}
import scala.io.Source

class createFiles {

  def createResourcesFile(): Unit = {
    /*
      指定したresourcesにあるファイルが存在しなかった場合に作成する
     */
    List(
      "tips.yml"
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

}
