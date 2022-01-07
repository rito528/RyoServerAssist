package com.ryoserver.util

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import org.bukkit.Material
import org.bukkit.entity.EntityType

import java.io.InputStream
import scala.io.Source

object Translate {

  private var langFile: JsonNode = _

  def loadLangFile(): Unit = {
    val is: InputStream = getClass.getClassLoader.getResourceAsStream("ja_jp.json")
    val mapper = new ObjectMapper()
    langFile = mapper.readTree(Source.fromInputStream(is).getLines().mkString)
  }

  def materialNameToJapanese(material: Material): String = {
    if (material.isBlock) {
      return langFile.get(s"block.${material.getKey.toString.replace(":", ".")}").textValue()
    } else if (material.isItem) {
      return langFile.get(s"item.${material.getKey.toString.replace(":", ".")}").textValue()
    }
    null
  }

  def entityNameToJapanese(entityType: EntityType): String = {
    langFile.get("entity." + entityType.getKey.toString.replace(":", ".")).textValue()
  }

}
