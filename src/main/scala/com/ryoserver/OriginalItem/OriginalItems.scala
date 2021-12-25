package com.ryoserver.OriginalItem

import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.attribute.{Attribute, AttributeModifier}
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.meta.{Damageable, ItemMeta}
import org.bukkit.inventory.{EquipmentSlot, ItemFlag, ItemStack}

import java.util.UUID
import scala.jdk.CollectionConverters._

object OriginalItems {

  val tiguruinoyaiba: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.IRON_SWORD)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$RED${BOLD}血狂の刃")
    itemMeta.addEnchant(Enchantment.DURABILITY, 1, false)
    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
    itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE,
      new AttributeModifier(UUID.randomUUID(), "damage", 3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.OFF_HAND))
    itemStack.setItemMeta(itemMeta)
    itemStack
  }

  val oretaEiyuNoKen: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.IRON_SWORD)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$YELLOW${BOLD}折れた英雄の剣")
    itemMeta.setLore(List(WHITE + "オフハンドに持つとトーテム効果が発動します。", WHITE + "耐久値分だけ使えます。").asJava)
    itemMeta.addEnchant(Enchantment.DURABILITY, 1, false)
    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
    itemMeta.asInstanceOf[Damageable].setDamage(240)
    itemStack.setItemMeta(itemMeta)
    itemStack
  }

  val elementalPickaxe: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.NETHERITE_PICKAXE)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$AQUA${BOLD}エレメンタルピッケル")
    itemMeta.setUnbreakable(true)
    itemMeta.addEnchant(Enchantment.DIG_SPEED, 5, false)
    itemMeta.addEnchant(Enchantment.SILK_TOUCH, 1, false)
    itemStack.setItemMeta(itemMeta)
    itemStack
  }

  val blessingPickaxe: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.NETHERITE_PICKAXE)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$YELLOW${BOLD}ブレッシングピッケル")
    itemMeta.setUnbreakable(true)
    itemMeta.addEnchant(Enchantment.DIG_SPEED, 5, false)
    itemMeta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 3, false)
    itemStack.setItemMeta(itemMeta)
    itemStack
  }

  val ryoNoKen: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.DIAMOND_SWORD)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$GREEN${BOLD}りょうの剣")
    itemMeta.addEnchant(Enchantment.DURABILITY, 1, false)
    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
    itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED,
      new AttributeModifier(UUID.randomUUID(), "attack_speed", 0.8, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.OFF_HAND))
    itemMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH,
      new AttributeModifier(UUID.randomUUID(), "health", 10.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.OFF_HAND))
    itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE,
      new AttributeModifier(UUID.randomUUID(), "damage", 2.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.OFF_HAND))
    itemStack.setItemMeta(itemMeta)
    itemStack
  }

  val gram: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.NETHERITE_SWORD)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$RED${BOLD}グラム")
    itemMeta.setUnbreakable(true)
    itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 5, false)
    itemMeta.addEnchant(Enchantment.FIRE_ASPECT, 2, false)
    itemStack.setItemMeta(itemMeta)
    itemStack.addUnsafeEnchantments(Map(Enchantment.LOOT_BONUS_MOBS -> 5.asInstanceOf[Integer]).asJava)
    itemStack
  }

  val seikenTengeki: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.NETHERITE_SWORD)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$YELLOW${BOLD}聖剣・天撃")
    itemMeta.setUnbreakable(true)
    itemMeta.addEnchant(Enchantment.DAMAGE_UNDEAD, 5, false)
    itemMeta.addEnchant(Enchantment.FIRE_ASPECT, 2, false)
    itemStack.setItemMeta(itemMeta)
    itemStack.addUnsafeEnchantments(Map(Enchantment.LOOT_BONUS_MOBS -> 5.asInstanceOf[Integer]).asJava)
    itemStack
  }

  val blackAxe: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.NETHERITE_AXE)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$RED${BOLD}ブラックアックス")
    itemMeta.setUnbreakable(true)
    itemMeta.addEnchant(Enchantment.DIG_SPEED, 5, false)
    itemStack.setItemMeta(itemMeta)
    itemStack
  }

  val dignity: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.NETHERITE_SHOVEL)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$GOLD${BOLD}ディグニティー")
    itemMeta.setUnbreakable(true)
    itemMeta.addEnchant(Enchantment.DIG_SPEED, 5, false)
    itemStack.setItemMeta(itemMeta)
    itemStack
  }

  val harvestStar: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.NETHERITE_HOE)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$GOLD${BOLD}ハーベストスター")
    itemMeta.setUnbreakable(true)
    itemMeta.addEnchant(Enchantment.DURABILITY, 1, false)
    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
    itemStack.setItemMeta(itemMeta)
    itemStack
  }

  val penginNoHane: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.ELYTRA)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$BLUE${BOLD}ペンギンの羽")
    itemMeta.setUnbreakable(true)
    itemMeta.addEnchant(Enchantment.DURABILITY, 1, false)
    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
    itemStack.setItemMeta(itemMeta)
    itemStack
  }

  val sugoiTurizao: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.FISHING_ROD)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$AQUA${BOLD}すごい釣り竿")
    itemMeta.setUnbreakable(true)
    itemMeta.addEnchant(Enchantment.LUCK, 3, false)
    itemMeta.addEnchant(Enchantment.LURE, 2, false)
    itemStack.setItemMeta(itemMeta)
    itemStack
  }

  val homura: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.BOW)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$RED${BOLD}無極の弓 -炎-")
    itemMeta.setUnbreakable(true)
    itemMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, false)
    itemMeta.addEnchant(Enchantment.ARROW_FIRE, 1, false)
    itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 5, false)
    itemMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, 2, false)
    itemStack.setItemMeta(itemMeta)
    itemStack
  }

  val hienNoYumi: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.BOW)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$RED${BOLD}飛炎の弓")
    itemMeta.setUnbreakable(true)
    itemMeta.addEnchant(Enchantment.ARROW_FIRE, 1, false)
    itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 5, false)
    itemMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, 2, false)
    itemStack.setItemMeta(itemMeta)
    itemStack
  }

  val kabuto: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.NETHERITE_HELMET)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$RED${YELLOW}テクト -兜-")
    itemMeta.addItemFlags(ItemFlag.HIDE_DYE)
    itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, false)
    itemMeta.addEnchant(Enchantment.WATER_WORKER, 1, false)
    itemMeta.addEnchant(Enchantment.OXYGEN, 3, false)
    itemMeta.addEnchant(Enchantment.MENDING, 1, false)
    itemStack.setItemMeta(itemMeta)
    itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 10)
    itemStack
  }

  val yoroi: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.NETHERITE_CHESTPLATE)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$RED${YELLOW}テクト -鎧-")
    itemMeta.addItemFlags(ItemFlag.HIDE_DYE)
    itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, false)
    itemMeta.addEnchant(Enchantment.MENDING, 1, false)
    itemStack.setItemMeta(itemMeta)
    itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 10)
    itemStack
  }

  val asi: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.NETHERITE_LEGGINGS)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$RED${YELLOW}テクト -脚-")
    itemMeta.addItemFlags(ItemFlag.HIDE_DYE)
    itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, false)
    itemMeta.addEnchant(Enchantment.MENDING, 1, false)
    itemStack.setItemMeta(itemMeta)
    itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 10)
    itemStack
  }

  val boots: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.NETHERITE_BOOTS)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$RED${YELLOW}テクト -靴-")
    itemMeta.addItemFlags(ItemFlag.HIDE_DYE)
    itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, false)
    itemMeta.addEnchant(Enchantment.PROTECTION_FALL, 4, false)
    itemMeta.addEnchant(Enchantment.MENDING, 1, false)
    itemStack.setItemMeta(itemMeta)
    itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 10)
    itemStack
  }

  val itemList = List(
    tiguruinoyaiba,
    oretaEiyuNoKen,
    elementalPickaxe,
    blessingPickaxe,
    ryoNoKen,
    gram,
    seikenTengeki,
    blackAxe,
    dignity,
    harvestStar,
    penginNoHane,
    sugoiTurizao,
    homura,
    hienNoYumi
  )

  val metaList = List(
    tiguruinoyaiba.getItemMeta.getItemFlags,
    oretaEiyuNoKen.getItemMeta.getItemFlags,
    ryoNoKen.getItemMeta.getItemFlags,
    kabuto.getItemMeta.getItemFlags,
    yoroi.getItemMeta.getItemFlags,
    asi.getItemMeta.getItemFlags,
    boots.getItemMeta.getItemFlags
  )

}
