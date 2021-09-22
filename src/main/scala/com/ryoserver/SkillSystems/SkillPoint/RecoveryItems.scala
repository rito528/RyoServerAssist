package com.ryoserver.SkillSystems.SkillPoint

import com.ryoserver.Inventory.Item.{getGachaItem, getItem}
import org.bukkit.{ChatColor, Material}
import org.bukkit.inventory.ItemStack

import scala.jdk.CollectionConverters._

object RecoveryItems {

  val min:ItemStack = {
    val Lore = List(
      ChatColor.WHITE + "飲むと少しスキルポイントが回復します。"
    ).asJava
    getItem(Material.HONEY_BOTTLE,"スキル回復",Lore)
  }

  val max:ItemStack = {
    val Lore = List(
      ChatColor.WHITE + "飲むとスキルポイントが全回復します。"
    ).asJava
    getGachaItem(Material.HONEY_BOTTLE,"スキル回復",Lore)
  }

}
