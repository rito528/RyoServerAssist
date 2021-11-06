package com.ryoserver.SkillSystems.SkillPoint

import com.ryoserver.Inventory.Item.{getEnchantEffectItem, getItem}
import org.bukkit.{ChatColor, Material}
import org.bukkit.inventory.ItemStack

import scala.jdk.CollectionConverters._

object RecoveryItems {

  val min: ItemStack = {
    val Lore = List(
      ChatColor.WHITE + "飲むと300だけスキルポイントが回復します。"
    ).asJava
    getItem(Material.HONEY_BOTTLE, "スキル回復(小)", Lore)
  }

  val max: ItemStack = {
    val Lore = List(
      ChatColor.WHITE + "飲むとスキルポイントが全回復します。"
    ).asJava
    getEnchantEffectItem(Material.HONEY_BOTTLE, "スキル回復(大)", Lore)
  }

}
