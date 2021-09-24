package com.ryoserver.SkillSystems.Skill

import com.ryoserver.Inventory.Item.{getItem, getPlayerSkull}
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.Skill.SkillData.SkillNames
import com.ryoserver.SkillSystems.SkillOpens.{SkillOpenCheck, SkillOpenData}
import org.bukkit.{Bukkit, Material}
import org.bukkit.entity.Player

import scala.jdk.CollectionConverters._

class SelectSkillMenu(ryoServerAssist: RyoServerAssist) {

  def openMenu(p:Player): Unit = {
    val check = new SkillOpenCheck(ryoServerAssist)
    val skillData = new SkillOpenData(ryoServerAssist)
    val inv = Bukkit.createInventory(null,54,"スキル選択")
    inv.setItem(10,getItem(Material.SHIELD,s"[基本スキル]${SkillNames.head}",List("耐性１の効果が付与されます。",
      if (check.isOpened(SkillNames.head,p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費" ,
      "スキルポイントコスト:300").asJava))
    inv.setItem(12,getItem(Material.IRON_BOOTS,s"[基本スキル]${SkillNames(1)}",List("移動速度上昇1の効果が付与されます。",
      if (check.isOpened(SkillNames(1),p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費" ,
      "スキルポイントコスト:300").asJava))
    inv.setItem(14,getItem(Material.RABBIT_FOOT,s"[基本スキル]${SkillNames(2)}",List("跳躍力上昇1の効果が付与されます。",
      if (check.isOpened(SkillNames(2),p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費" ,
      "スキルポイントコスト:300").asJava))
    inv.setItem(16,getItem(Material.IRON_SWORD,s"[基本スキル]${SkillNames(3)}",List("攻撃力上昇1の効果が付与されます。",
      if (check.isOpened(SkillNames(3),p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費" ,
      "スキルポイントコスト:300").asJava))
    inv.setItem(28,getItem(Material.IRON_PICKAXE,s"[基本スキル]${SkillNames(4)}",List("採掘速度上昇1の効果が付与されます。",
      if (check.isOpened(SkillNames(4),p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費" ,
      "スキルポイントコスト:300").asJava))
    inv.setItem(30,getItem(Material.ENCHANTED_GOLDEN_APPLE,s"[基本スキル]${SkillNames(5)}",List("再生能力1の効果が付与されます。",
      if (check.isOpened(SkillNames(5),p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費" ,
      "スキルポイントコスト:300").asJava))
    inv.setItem(32,getItem(Material.ELYTRA,s"${SkillNames(6)}",List("低速落下の効果が付与されます。",
      if (check.isOpened(SkillNames(6),p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費" ,
      if (!check.isOpened(SkillNames(6),p)) "基本スキルをすべて開放" else "",
      "スキルポイントコスト:600").asJava))
    inv.setItem(34,getItem(Material.ENDER_EYE,s"${SkillNames(7)}",List("暗視の効果が付与されます。",
      if (check.isOpened(SkillNames(7),p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費" ,
      if (!check.isOpened(SkillNames(7),p)) "基本スキルをすべて開放" else "",
      "スキルポイントコスト:600").asJava))
    inv.setItem(46,getItem(Material.LAVA_BUCKET,s"${SkillNames(8)}",List("耐火の効果が付与されます。",
      if (check.isOpened(SkillNames(8),p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費" ,
      if (!check.isOpened(SkillNames(8),p)) "基本スキルをすべて開放" else "",
      "スキルポイントコスト:600").asJava))
    inv.setItem(48,getItem(Material.WATER_BUCKET,s"${SkillNames(9)}",List("水中呼吸の効果が付与されます。",
      if (check.isOpened(SkillNames(9),p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費" ,
      if (!check.isOpened(SkillNames(9),p)) "基本スキルをすべて開放" else "",
      "スキルポイントコスト:600").asJava))
    inv.setItem(50,getPlayerSkull(p,"クリックですべてのスキル選択を解除できます。",List(
      "現在保有中のスキル開放ポイント:" + skillData.getSkillOpenPoint(p)
    )))
    inv.setItem(52,getItem(Material.MAGENTA_GLAZED_TERRACOTTA,"メニューに戻る",List("メニューに戻ります。").asJava))
    p.openInventory(inv)
  }

}