package com.ryoserver.Security

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.ryoserver.Config.ConfigData.getConfig
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

import java.io.PrintWriter
import java.nio.file.{Files, Paths}
import scala.sys.process.Process

class GetPlayerInfo {

  val getJSON: String => String = (ipAddress: String) =>
    Process(Seq("curl", "-s", "https://www.iphunter.info:8082/v1/ip/" + ipAddress, s"-H", s"X-Key: ${getConfig.IPHunterAPIKey}")).!!

  def getIPInfo(ipAddress: String): Map[String, String] = {
    val mapper: ObjectMapper = new ObjectMapper();
    val node: JsonNode = mapper.readTree(getJSON(ipAddress))
    val data = node.get("data")
    Map[String, String](
      "country" -> data.get("country_name").textValue(),
      "city" -> data.get("city").textValue(),
      "isp" -> data.get("isp").textValue()
    )
  }


}
