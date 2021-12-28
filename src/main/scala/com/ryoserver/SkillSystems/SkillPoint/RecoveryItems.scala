package com.ryoserver.SkillSystems.SkillPoint

import com.ryoserver.util.Item.{getEnchantEffectItem, getItem}
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

import scala.jdk.CollectionConverters._

object RecoveryItems {

  val min: ItemStack = {
    val Lore = List(
      s"${WHITE}飲むと300だけスキルポイントが回復します。"
    ).asJava
    getItem(Material.HONEY_BOTTLE, "スキル回復(小)", Lore)
  }

  val mid: ItemStack = {
    val Lore = List(
      s"${WHITE}飲むと3000スキルポイントが回復します。"
    ).asJava
    getEnchantEffectItem(Material.HONEY_BOTTLE, "スキル回復(中)", Lore)
  }

  val max: ItemStack = {
    val Lore = List(
      s"${WHITE}飲むとスキルポイントが全回復します。"
    ).asJava
    getEnchantEffectItem(Material.HONEY_BOTTLE, "スキル回復(大)", Lore)
  }

}
