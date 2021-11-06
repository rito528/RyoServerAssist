package com.ryoserver.Menu

import com.google.common.io.ByteStreams
import com.ryoserver.RyoServerAssist
import org.bukkit.{Bukkit, ChatColor, Material, Sound}
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class MenuMotion(ryoServerAssist: RyoServerAssist) {

  def openWorkBench(p: Player): Unit = {
    p.openWorkbench(null, true)
  }

  def openEnderChest(p: Player): Unit = {
    p.openInventory(p.getEnderChest)
    p.playSound(p.getLocation, Sound.BLOCK_ENDER_CHEST_OPEN, 1, 1)
  }

  def giveFirework(p: Player): Unit = {
    p.getInventory.addItem(new ItemStack(Material.FIREWORK_ROCKET, 64))
    p.playSound(p.getLocation, Sound.UI_BUTTON_CLICK, 1, 1)
  }

  def worldTeleport(p: Player, world: Boolean): Unit = {
    if (world) {
      p.teleport(Bukkit.getWorld("world").getSpawnLocation)
      p.sendMessage(ChatColor.AQUA + "worldのスポーン地点にテレポートしました！")
    } else {
      p.teleport(p.getWorld.getSpawnLocation)
      p.sendMessage(ChatColor.AQUA + "スポーン地点にテレポートしました！")
    }
    p.playSound(p.getLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1)
  }

  def teleportToHub(p: Player): Unit = {
    p.playSound(p.getLocation, Sound.BLOCK_WOODEN_DOOR_OPEN, 1, 1)
    val out = ByteStreams.newDataOutput
    out.writeUTF("Connect")
    out.writeUTF("lobby")
    p.sendPluginMessage(ryoServerAssist, "BungeeCord", out.toByteArray)
  }

  def sendSiteURL(p: Player, webType: String): Unit = {
    webType match {
      case "web" =>
        p.sendMessage(ChatColor.UNDERLINE + ryoServerAssist.getConfig.getString("webSite"))
      case "dynmap" =>
        p.sendMessage(ChatColor.UNDERLINE + ryoServerAssist.getConfig.getString("dynmap"))
      case "vote" =>
        p.sendMessage(ChatColor.UNDERLINE + ryoServerAssist.getConfig.getStringList("voteSite").toArray()(0).toString)
        p.sendMessage(ChatColor.UNDERLINE + ryoServerAssist.getConfig.getStringList("voteSite").toArray()(1).toString)
    }
  }

}
