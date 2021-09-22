package com.ryoserver.SkillSystems.Skill

import com.ryoserver.Inventory.Item.{getItem, getPlayerSkull}
import org.bukkit.{Bukkit, Material}
import org.bukkit.entity.Player

import scala.jdk.CollectionConverters._

class SelectSkillMenu {

  def openMenu(p:Player): Unit = {
    val inv = Bukkit.createInventory(null,54,"スキル選択")
    inv.setItem(10,getItem(Material.SHIELD,"[基本スキル]耐性1",List("耐性１の効果が付与されます。",
      "解放条件:スキル開放ポイント10を消費",
      "スキルポイントコスト:300").asJava))
    inv.setItem(12,getItem(Material.IRON_BOOTS,"[基本スキル]よーいドン",List("移動速度上昇1の効果が付与されます。",
      "解放条件:スキル開放ポイント10を消費",
      "スキルポイントコスト:300").asJava))
    inv.setItem(14,getItem(Material.RABBIT_FOOT,"[基本スキル]跳躍力上昇1",List("跳躍力上昇1の効果が付与されます。",
      "解放条件:スキル開放ポイント10を消費",
      "スキルポイントコスト:300").asJava))
    inv.setItem(16,getItem(Material.IRON_SWORD,"[基本スキル]攻撃力上昇1",List("攻撃力上昇1の効果が付与されます。",
      "解放条件:スキル開放ポイント10を消費",
      "スキルポイントコスト:300").asJava))
    inv.setItem(28,getItem(Material.IRON_PICKAXE,"[基本スキル]採掘速度上昇1",List("採掘速度上昇1の効果が付与されます。",
      "解放条件:スキル開放ポイント10を消費",
      "スキルポイントコスト:300").asJava))
    inv.setItem(30,getItem(Material.ENCHANTED_GOLDEN_APPLE,"[基本スキル]再生能力1",List("再生能力1の効果が付与されます。",
      "解放条件:スキル開放ポイント10を消費",
      "スキルポイントコスト:300").asJava))
    inv.setItem(32,getItem(Material.ELYTRA,"低速落下",List("低速落下の効果が付与されます。",
      "解放条件:スキル開放ポイント10を消費",
      "基本スキルをすべて開放",
      "スキルポイントコスト:600").asJava))
    inv.setItem(34,getItem(Material.ENDER_EYE,"暗視",List("暗視の効果が付与されます。",
      "解放条件:スキル開放ポイント10を消費",
      "基本スキルをすべて開放",
      "スキルポイントコスト:600").asJava))
    inv.setItem(46,getItem(Material.LAVA_BUCKET,"耐火",List("耐火の効果が付与されます。",
      "解放条件:スキル開放ポイント10を消費",
      "基本スキルをすべて開放",
      "スキルポイントコスト:600").asJava))
    inv.setItem(48,getItem(Material.WATER_BUCKET,"水中呼吸",List("水中呼吸の効果が付与されます。",
      "解放条件:スキル開放ポイント10を消費",
      "基本スキルをすべて開放",
      "スキルポイントコスト:600").asJava))
    inv.setItem(50,getPlayerSkull(p,"情報",List("テスト")))
    inv.setItem(52,getItem(Material.MAGENTA_GLAZED_TERRACOTTA,"メニューに戻る",List("メニューに戻ります。").asJava))
    p.openInventory(inv)
  }

}
