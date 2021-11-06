package com.ryoserver.Security

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

import java.io.PrintWriter
import java.nio.file.{Files, Paths}
import scala.sys.process.Process

class CheckVPNAndProxy {


  private val Key = Config.getApiKey

  val getJSON: String => String = (ipAddress: String) =>
    Process(Seq("curl", "-s", "https://www.iphunter.info:8082/v1/ip/" + ipAddress, s"-H", s"X-Key: $Key")).!!

  def loginCheck(p: Player, plugin: Plugin): Boolean = {
    val mapper: ObjectMapper = new ObjectMapper();
    val json = getJSON(players.getPlayerIP(p))
    val node: JsonNode = mapper.readTree(json)
    if (node.get("status").textValue() == "success") {
      val data = node.get("data")
      savePlayerIPInfo(p, plugin, json)
      if (data.get("block").intValue() == 1 && !(data.get("isp").textValue() == "Private IP Address LAN" || data.get("isp").textValue() == "Loopback")) {
        false
      } else {
        true
      }
    } else {
      false
    }
  }

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

  def savePlayerIPInfo(p: Player, plugin: Plugin, data: String): Unit = {
    val name = p.getName;
    new BukkitRunnable {
      override def run(): Unit = {
        val dir = "plugins/MinecraftServerSecurity/Players"
        val saveDir = Paths.get(dir)
        if (Files.notExists(saveDir)) Files.createDirectories(saveDir)
        val playerFilePath = dir + "/" + name
        val playerFile = Paths.get(playerFilePath)
        if (Files.notExists(playerFile)) {
          val pw = new PrintWriter(playerFilePath)
          pw.write(data);
          pw.close()
        }
      }
    }.runTaskAsynchronously(plugin)
  }

  def notification(name: String): Unit = {
    Process(s"curl -s -H \"Accept: application/json\" -H \"Content-type: application/json\" -X POST -d '{\"username\":\"MinecraftServerSecurity\",\"content\":\"${name}のVPN使用を検知しました。\"}' 'https://discord.com/api/webhooks/823179337250504706/khLMPDcINh3Wc60FhK3VoNWwdMxH_uK70sOumhepNFipaLVd2n_c0EAdgbfO_8K--Joj'")
  }


}
