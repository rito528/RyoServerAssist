package com.ryoserver.OriginalItem

import org.bukkit.command.{Command, CommandExecutor, CommandSender}
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
        p.getWorld.dropItem(p.getLocation(),OriginalItems.oretaEiyuNoKen)
        return true
      } else if (args(0).equalsIgnoreCase("kabuto")) {
        p.getWorld.dropItem(p.getLocation(),OriginalItems.kabuto)
        return true
      } else if (args(0).equalsIgnoreCase("yoroi")) {
        p.getWorld.dropItem(p.getLocation(),OriginalItems.yoroi)
        return true
      } else if (args(0).equalsIgnoreCase("asi")) {
        p.getWorld.dropItem(p.getLocation(),OriginalItems.asi)
        return true
      } else if (args(0).equalsIgnoreCase("kutu")) {
        p.getWorld.dropItem(p.getLocation(),OriginalItems.boots)
        return true
      }
    }
    false
  }

}
