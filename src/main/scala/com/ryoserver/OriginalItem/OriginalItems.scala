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
      new AttributeModifier(UUID.fromString("e9bb5f81-21b1-4677-8eec-36ebffc9148b"), "damage", 3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.OFF_HAND))
    itemStack.setItemMeta(itemMeta)
    itemStack
  }

  val oretaEiyuNoKen: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.IRON_SWORD)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$YELLOW${BOLD}折れた英雄の剣")
    itemMeta.setLore(List(s"${WHITE}オフハンドに持つとトーテム効果が発動します。", s"${WHITE}耐久値分だけ使えます。").asJava)
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

  val flacPickaxe: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.NETHERITE_PICKAXE)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$YELLOW${BOLD}ふらっくぴっける")
    itemMeta.addEnchant(Enchantment.DIG_SPEED, 5, false)
    itemMeta.addEnchant(Enchantment.MENDING, 1, false)
    itemMeta.addItemFlags(ItemFlag.HIDE_DYE)
    itemStack.setItemMeta(itemMeta)
    itemStack.addUnsafeEnchantment(Enchantment.DURABILITY,5)
    itemStack.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 4)
    itemStack
  }

  val ryoNoKen: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.DIAMOND_SWORD)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$GREEN${BOLD}りょうの剣")
    itemMeta.addEnchant(Enchantment.DURABILITY, 1, false)
    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
    itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED,
      new AttributeModifier(UUID.fromString("bf0a5a0e-9580-4fd8-84f6-023de2020988"), "attack_speed", 0.8, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.OFF_HAND))
    itemMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH,
      new AttributeModifier(UUID.fromString("a77b08cf-c745-4c23-9ddd-b77906b98194"), "health", 10.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.OFF_HAND))
    itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE,
      new AttributeModifier(UUID.fromString("adfff983-4217-49bf-9b43-dd23f9aad6dd"), "damage", 2.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.OFF_HAND))
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

  val harvestStarKai: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.NETHERITE_HOE)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$GOLD${BOLD}ハーベストスター・改")
    itemMeta.setUnbreakable(true)
    itemStack.setItemMeta(itemMeta)
    itemStack.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS,4)
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

  val sugoiTurizaokai: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.FISHING_ROD)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$AQUA${BOLD}すごい釣り竿・改")
    itemMeta.setUnbreakable(true)
    itemStack.setItemMeta(itemMeta)
    itemStack.addUnsafeEnchantment(Enchantment.LUCK, 4)
    itemStack.addUnsafeEnchantment(Enchantment.LURE, 3)
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

  val kabutoKai: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.NETHERITE_HELMET)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"${YELLOW}テクト -兜-・改")
    itemMeta.addItemFlags(ItemFlag.HIDE_DYE)
    itemMeta.addEnchant(Enchantment.WATER_WORKER, 1, false)
    itemMeta.addEnchant(Enchantment.OXYGEN, 3, false)
    itemMeta.addEnchant(Enchantment.MENDING, 1, false)
    itemStack.setItemMeta(itemMeta)
    itemStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,5)
    itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 10)
    itemStack
  }

  val yoroi: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.NETHERITE_CHESTPLATE)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"${YELLOW}テクト -鎧-")
    itemMeta.addItemFlags(ItemFlag.HIDE_DYE)
    itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, false)
    itemMeta.addEnchant(Enchantment.MENDING, 1, false)
    itemStack.setItemMeta(itemMeta)
    itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 10)
    itemStack
  }

  val yoroiKai: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.NETHERITE_CHESTPLATE)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"${YELLOW}テクト -鎧-・改")
    itemMeta.addItemFlags(ItemFlag.HIDE_DYE)
    itemMeta.addEnchant(Enchantment.MENDING, 1, false)
    itemStack.setItemMeta(itemMeta)
    itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 10)
    itemStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,5)
    itemStack
  }

  val asi: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.NETHERITE_LEGGINGS)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"${YELLOW}テクト -脚-")
    itemMeta.addItemFlags(ItemFlag.HIDE_DYE)
    itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, false)
    itemMeta.addEnchant(Enchantment.MENDING, 1, false)
    itemStack.setItemMeta(itemMeta)
    itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 10)
    itemStack
  }

  val asiKai: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.NETHERITE_LEGGINGS)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"${YELLOW}テクト -脚-・改")
    itemMeta.addItemFlags(ItemFlag.HIDE_DYE)
    itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, false)
    itemMeta.addEnchant(Enchantment.MENDING, 1, false)
    itemStack.setItemMeta(itemMeta)
    itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 10)
    itemStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,5)
    itemStack
  }

  val boots: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.NETHERITE_BOOTS)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"${YELLOW}テクト -靴-")
    itemMeta.addItemFlags(ItemFlag.HIDE_DYE)
    itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, false)
    itemMeta.addEnchant(Enchantment.PROTECTION_FALL, 4, false)
    itemMeta.addEnchant(Enchantment.MENDING, 1, false)
    itemStack.setItemMeta(itemMeta)
    itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 10)
    itemStack
  }

  val bootsKai: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.NETHERITE_BOOTS)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"${YELLOW}テクト -靴-・改")
    itemMeta.addItemFlags(ItemFlag.HIDE_DYE)
    itemMeta.addEnchant(Enchantment.PROTECTION_FALL, 4, false)
    itemMeta.addEnchant(Enchantment.MENDING, 1, false)
    itemStack.setItemMeta(itemMeta)
    itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 10)
    itemStack.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,5)
    itemStack
  }

  val gungunil: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.TRIDENT)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$RED${BOLD}グングニル")
    itemMeta.addItemFlags(ItemFlag.HIDE_DYE)
    itemMeta.addEnchant(Enchantment.DURABILITY,3,false)
    itemMeta.addEnchant(Enchantment.RIPTIDE,3,false)
    itemMeta.addEnchant(Enchantment.MENDING, 1, false)
    itemStack.setItemMeta(itemMeta)
    itemStack
  }

  val battleAxe: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.DIAMOND_AXE)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$RED${BOLD}バトルアックス")
    itemMeta.addItemFlags(ItemFlag.HIDE_DYE)
    itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 5, false)
    itemMeta.addEnchant(Enchantment.DIG_SPEED, 4, false)
    itemMeta.addEnchant(Enchantment.DURABILITY,3,false)
    itemMeta.addEnchant(Enchantment.MENDING, 1, false)
    itemStack.setItemMeta(itemMeta)
    itemStack
  }

  val rifle: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.CROSSBOW)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$YELLOW${BOLD}ライフル")
    itemMeta.addItemFlags(ItemFlag.HIDE_DYE)
    itemMeta.addEnchant(Enchantment.PIERCING, 4, false)
    itemMeta.addEnchant(Enchantment.DURABILITY,3,false)
    itemMeta.addEnchant(Enchantment.MENDING, 1, false)
    itemStack.setItemMeta(itemMeta)
    itemStack.addUnsafeEnchantment(Enchantment.QUICK_CHARGE,4)
    itemStack
  }

  val kikorinoOno: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.DIAMOND_AXE)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$AQUA${BOLD}木こりの斧")
    itemMeta.addItemFlags(ItemFlag.HIDE_DYE)
    itemMeta.addEnchant(Enchantment.DIG_SPEED, 5, false)
    itemMeta.addEnchant(Enchantment.DURABILITY,3,false)
    itemMeta.addEnchant(Enchantment.MENDING, 1, false)
    itemStack.setItemMeta(itemMeta)
    itemStack
  }

  val humetunotubasa: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.ELYTRA)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$GOLD${BOLD}不滅の翼")
    itemMeta.addItemFlags(ItemFlag.HIDE_DYE)
    itemMeta.addEnchant(Enchantment.MENDING, 1, false)
    itemStack.setItemMeta(itemMeta)
    itemStack.addUnsafeEnchantment(Enchantment.DURABILITY,4)
    itemStack
  }

  val hyouketunokutu: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.DIAMOND_BOOTS)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$AQUA${BOLD}氷結の靴")
    itemMeta.addItemFlags(ItemFlag.HIDE_DYE)
    itemMeta.addEnchant(Enchantment.FROST_WALKER, 2, false)
    itemMeta.addEnchant(Enchantment.PROTECTION_FALL,4,false)
    itemMeta.addEnchant(Enchantment.DURABILITY,3,false)
    itemMeta.addEnchant(Enchantment.MENDING, 1, false)
    itemStack.setItemMeta(itemMeta)
    itemStack
  }

  val namonakiken: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.DIAMOND_SWORD)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$YELLOW${BOLD}名もなき剣")
    itemMeta.addItemFlags(ItemFlag.HIDE_DYE)
    itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 5, false)
    itemMeta.addEnchant(Enchantment.FIRE_ASPECT, 2, false)
    itemMeta.addEnchant(Enchantment.LOOT_BONUS_MOBS,2,false)
    itemMeta.addEnchant(Enchantment.DURABILITY,3,false)
    itemMeta.addEnchant(Enchantment.MENDING, 1, false)
    itemStack.setItemMeta(itemMeta)
    itemStack
  }

  val yuusyanotate: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.SHIELD)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$AQUA${BOLD}勇者の盾")
    itemMeta.addItemFlags(ItemFlag.HIDE_DYE)
    itemMeta.addEnchant(Enchantment.DURABILITY,3,false)
    itemMeta.addEnchant(Enchantment.MENDING, 1, false)
    itemMeta.setLore(List(s"${GRAY}手に持つと耐性2のエフェクトが付きます。").asJava)
    itemStack.setItemMeta(itemMeta)
    itemStack
  }

  val syokuninnnoturuhasi: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.DIAMOND_PICKAXE)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$AQUA${BOLD}職人のツルハシ")
    itemMeta.addItemFlags(ItemFlag.HIDE_DYE)
    itemMeta.addEnchant(Enchantment.SILK_TOUCH,1,false)
    itemMeta.addEnchant(Enchantment.DIG_SPEED,5,false)
    itemMeta.addEnchant(Enchantment.DURABILITY,3,false)
    itemMeta.addEnchant(Enchantment.MENDING, 1, false)
    itemStack.setItemMeta(itemMeta)
    itemStack
  }

  val kinnunnnoturuhasi: ItemStack = {
    val itemStack: ItemStack = new ItemStack(Material.DIAMOND_PICKAXE)
    val itemMeta: ItemMeta = itemStack.getItemMeta
    itemMeta.setDisplayName(s"$YELLOW${BOLD}金運のツルハシ")
    itemMeta.addItemFlags(ItemFlag.HIDE_DYE)
    itemMeta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS,3,false)
    itemMeta.addEnchant(Enchantment.DIG_SPEED,5,false)
    itemMeta.addEnchant(Enchantment.DURABILITY,3,false)
    itemMeta.addEnchant(Enchantment.MENDING, 1, false)
    itemStack.setItemMeta(itemMeta)
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
    harvestStarKai,
    penginNoHane,
    sugoiTurizao,
    homura,
    hienNoYumi
  )

  val metaList = List(
    tiguruinoyaiba,
    oretaEiyuNoKen,
    flacPickaxe,
    ryoNoKen,
    kabuto,
    yoroi,
    asi,
    boots,
    kabutoKai,
    yoroiKai,
    asiKai,
    bootsKai,
    gungunil,
    battleAxe,
    rifle,
    kikorinoOno,
    humetunotubasa,
    hyouketunokutu,
    namonakiken
  ).map(_.getItemMeta.getItemFlags)

}
