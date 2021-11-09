package com.ryoserver.SkillSystems.Skill

import com.ryoserver.Menu.{Menu, createMenu}
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.Skill.SkillData.SkillNames
import com.ryoserver.SkillSystems.SkillOpens.{SkillOpenCheck, SkillOpenData}
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType

class SelectSkillMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  var name: String = "スキル選択"
  val slot: Int = 6
  var p: Player = _

  def openMenu(player: Player): Unit = {
    p = player
    val check = new SkillOpenCheck(ryoServerAssist)
    val skillData = new SkillOpenData(ryoServerAssist)
    setItem(2, 2, if (check.isOpened(SkillNames.head, p)) Material.SHIELD else Material.BEDROCK, effect = false,
      s"${GREEN}[基本スキル]${SkillNames.head}", List(s"${GRAY}耐性１の効果が付与されます。",
        GRAY + (if (check.isOpened(SkillNames.head, p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        s"${GRAY}スキルポイントコスト:300"))
    setItem(4, 2, if (check.isOpened(SkillNames(1), p)) Material.IRON_BOOTS else Material.BEDROCK, effect = false,
      s"${GREEN}[基本スキル]${SkillNames(1)}", List(s"${GRAY}移動速度上昇1の効果が付与されます。",
        GRAY + (if (check.isOpened(SkillNames(1), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        s"${GRAY}スキルポイントコスト:300"))
    setItem(6, 2, if (check.isOpened(SkillNames(2), p)) Material.RABBIT_FOOT else Material.BEDROCK, effect = false,
      s"${GREEN}[基本スキル]${SkillNames(2)}", List(s"${GRAY}跳躍力上昇1の効果が付与されます。",
        GRAY + (if (check.isOpened(SkillNames(2), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        s"${GRAY}スキルポイントコスト:300"))
    setItem(8, 2, if (check.isOpened(SkillNames(3), p)) Material.IRON_SWORD else Material.BEDROCK, effect = false,
      s"${GREEN}[基本スキル]${SkillNames(3)}", List(s"${GRAY}攻撃力上昇1の効果が付与されます。",
        GRAY + (if (check.isOpened(SkillNames(3), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        s"${GRAY}スキルポイントコスト:300"))
    setItem(2, 4, if (check.isOpened(SkillNames(4), p)) Material.IRON_PICKAXE else Material.BEDROCK, effect = false,
      s"${GREEN}[基本スキル]${SkillNames(4)}", List(s"${GRAY}採掘速度上昇1の効果が付与されます。",
        GRAY + (if (check.isOpened(SkillNames(4), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        s"${GRAY}スキルポイントコスト:300"))
    setItem(4, 4, if (check.isOpened(SkillNames(5), p)) Material.ENCHANTED_GOLDEN_APPLE else Material.BEDROCK, effect = false,
      s"${GREEN}[基本スキル]${SkillNames(5)}", List(s"${GRAY}再生能力1の効果が付与されます。",
        GRAY + (if (check.isOpened(SkillNames(5), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        s"${GRAY}スキルポイントコスト:300"))
    setItem(6, 4, if (check.isOpened(SkillNames(6), p)) Material.ELYTRA else Material.BEDROCK, effect = false,
      s"${GREEN}${SkillNames(6)}",
      List(s"${GRAY}低速落下の効果が付与されます。",
        GRAY + (if (check.isOpened(SkillNames(6), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        if (!check.isOpened(SkillNames(6), p)) s"${GRAY}基本スキルをすべて開放" else "",
        s"${GRAY}スキルポイントコスト:600"))
    setItem(8, 4, if (check.isOpened(SkillNames(7), p)) Material.ENDER_EYE else Material.BEDROCK, effect = false,
      s"${GREEN}${SkillNames(7)}",
      List(s"${GRAY}暗視の効果が付与されます。",
        GRAY + (if (check.isOpened(SkillNames(7), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        if (!check.isOpened(SkillNames(7), p)) s"${GRAY}基本スキルをすべて開放" else "",
        s"${GRAY}スキルポイントコスト:600"))
    setItem(2, 6, if (check.isOpened(SkillNames(8), p)) Material.LAVA_BUCKET else Material.BEDROCK, effect = false,
      s"${GREEN}${SkillNames(8)}",
      List(s"${GRAY}耐火の効果が付与されます。",
        GRAY + (if (check.isOpened(SkillNames(8), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        if (!check.isOpened(SkillNames(8), p)) s"${GRAY}基本スキルをすべて開放" else "",
        s"${GRAY}スキルポイントコスト:600"))
    setItem(4, 6, if (check.isOpened(SkillNames(9), p)) Material.WATER_BUCKET else Material.BEDROCK, effect = false,
      s"${GREEN}${SkillNames(9)}",
      List(s"${GRAY}水中呼吸の効果が付与されます。",
        GRAY + (if (check.isOpened(SkillNames(9), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        if (!check.isOpened(SkillNames(9), p)) s"${GRAY}基本スキルをすべて開放" else "",
        s"${GRAY}スキルポイントコスト:600"))
    setSkullItem(6, 6, p, s"${GREEN}クリックですべてのスキル選択を解除できます。", List(
      s"${GRAY}現在保有中のスキル開放ポイント:" + skillData.getSkillOpenPoint(p)
    ))
    setItem(8, 6, Material.MAGENTA_GLAZED_TERRACOTTA, effect = false, s"${GREEN}メニューに戻る", List(s"${GRAY}メニューに戻ります。"))
    registerMotion(motion)
    open()
  }

  def motion(player: Player, index: Int): Unit = {
    val toggle = new skillToggleClass(player, ryoServerAssist)
    index match {
      case 10 => toggle.effect(PotionEffectType.DAMAGE_RESISTANCE, 0, 300, SkillNames.head)
      case 12 => toggle.effect(PotionEffectType.SPEED, 0, 300, SkillNames(1))
      case 14 => toggle.effect(PotionEffectType.JUMP, 0, 300, SkillNames(2))
      case 16 => toggle.effect(PotionEffectType.INCREASE_DAMAGE, 0, 300, SkillNames(3))
      case 28 => toggle.effect(PotionEffectType.FAST_DIGGING, 0, 300, SkillNames(4))
      case 30 => toggle.effect(PotionEffectType.REGENERATION, 0, 300, SkillNames(5))
      case 32 => toggle.effect(PotionEffectType.SLOW_FALLING, 0, 600, SkillNames(6))
      case 34 => toggle.effect(PotionEffectType.NIGHT_VISION, 0, 600, SkillNames(7))
      case 46 => toggle.effect(PotionEffectType.FIRE_RESISTANCE, 0, 600, SkillNames(8))
      case 48 => toggle.effect(PotionEffectType.WATER_BREATHING, 0, 600, SkillNames(9))
      case 50 =>
        toggle.allEffectClear(p)
        p.sendMessage(AQUA + "スキルをすべて無効化しました。")
      case 52 => new createMenu(ryoServerAssist).menu(p)
      case _ =>
    }
  }

}
