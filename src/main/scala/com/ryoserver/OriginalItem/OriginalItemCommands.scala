package com.ryoserver.OriginalItem

import net.minecraft.network.protocol.game.PacketPlayOutEntityStatus
import org.bukkit.command.{Command, CommandExecutor, CommandSender}
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer
import org.bukkit.entity.Player

class OriginalItemCommands extends CommandExecutor {

  override def onCommand(commandSender: CommandSender, command: Command, label: String, args: Array[String]): Boolean = {
    if (label.equalsIgnoreCase("getOriginalItem")) {
      val p = commandSender.asInstanceOf[Player]
      if (args(0).equalsIgnoreCase("tiguruinoyaiba")) {
        p.getWorld.dropItem(p.getLocation,OriginalItems.tiguruinoyaiba)
        return true
      } else if (args(0).equalsIgnoreCase("elementalPickaxe")) {
        p.getWorld.dropItem(p.getLocation(),OriginalItems.elementalPickaxe)
        return true
      } else if (args(0).equalsIgnoreCase("blessingPickaxe")) {
        p.getWorld.dropItem(p.getLocation(),OriginalItems.blessingPickaxe)
        return true
      } else if (args(0).equalsIgnoreCase("ryoNoKen")) {
        p.getWorld.dropItem(p.getLocation(),OriginalItems.ryoNoKen)
        return true
      } else if (args(0).equalsIgnoreCase("gram")) {
        p.getWorld.dropItem(p.getLocation(),OriginalItems.gram)
        return true
      } else if (args(0).equalsIgnoreCase("seikenTengeki")) {
        p.getWorld.dropItem(p.getLocation(),OriginalItems.seikenTengeki)
        return true
      } else if (args(0).equalsIgnoreCase("blackAxe")) {
        p.getWorld.dropItem(p.getLocation(),OriginalItems.blackAxe)
        return true
      } else if (args(0).equalsIgnoreCase("dignity")) {
        p.getWorld.dropItem(p.getLocation(),OriginalItems.dignity)
        return true
      } else if (args(0).equalsIgnoreCase("harvestStar")) {
        p.getWorld.dropItem(p.getLocation(),OriginalItems.harvestStar)
        return true
      } else if (args(0).equalsIgnoreCase("penginNoHane")) {
        p.getWorld.dropItem(p.getLocation(),OriginalItems.penginNoHane)
        return true
      } else if (args(0).equalsIgnoreCase("sugoiTurizao")) {
        p.getWorld.dropItem(p.getLocation(),OriginalItems.sugoiTurizao)
        return true
      } else if (args(0).equalsIgnoreCase("homura")) {
        p.getWorld.dropItem(p.getLocation(),OriginalItems.homura)
        return true
      } else if (args(0).equalsIgnoreCase("hienNoYumi")) {
        p.getWorld.dropItem(p.getLocation(),OriginalItems.hienNoYumi)
        return true
      } else if (args(0).equalsIgnoreCase("oretaEiyuNoKen")) {
//        val ep = p.asInstanceOf[CraftPlayer].getHandle
//        val status = new PacketPlayOutEntityStatus(ep,35)
//        ep.b.sendPacket(status)
        p.getWorld.dropItem(p.getLocation(),OriginalItems.oretaEiyuNoKen)
        return true
      }
    }
    false
  }

}
