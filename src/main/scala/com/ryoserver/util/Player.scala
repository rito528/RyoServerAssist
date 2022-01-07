package com.ryoserver.util

import com.fasterxml.jackson.databind.ObjectMapper

import java.util.UUID
import scala.io.Source

object Player {

  /*
  mojangAPIを利用してプレイヤー名からUUIDを取得する
 */
  def nameFromUUID(name: String): UUID = {
    val source = Source.fromURL(s"https://api.mojang.com/users/profiles/minecraft/$name")
    val jsonData = source.getLines().mkString("")
    source.close()
    val mapper = new ObjectMapper()
    val jsonNode = mapper.readTree(jsonData)
    UUID.fromString(jsonNode.get("id").textValue().replaceFirst( "([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", "$1-$2-$3-$4-$5" ))
  }

}
