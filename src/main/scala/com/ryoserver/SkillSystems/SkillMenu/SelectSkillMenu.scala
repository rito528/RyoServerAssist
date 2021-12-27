package com.ryoserver.SkillSystems.SkillMenu

import com.ryoserver.Menu.Menu
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.Skill.EffectSkill.SkillData.SkillNames
import com.ryoserver.SkillSystems.Skill.EffectSkill.skillToggleClass
import com.ryoserver.SkillSystems.SkillOpens.{SkillOpenCheck, SkillOpenData}
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType

class SelectSkillMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  val slot: Int = 6
  var name: String = "通常スキル選択"
  var p: Player = _

  def openMenu(player: Player): Unit = {
    p = player
    val check = new SkillOpenCheck(ryoServerAssist)
    val skillData = new SkillOpenData(ryoServerAssist)
    setItem(1, 1, if (check.isOpened(SkillNames.head, p)) Material.SHIELD else Material.BEDROCK, effect = false,
      s"${GREEN}[基本スキル]${SkillNames.head}", List(s"${GRAY}耐性1の効果が付与されます。",
        s"${GRAY}" + (if (check.isOpened(SkillNames.head, p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        s"${GRAY}スキルポイントコスト:300"))
    setItem(2, 1, if (check.isOpened(SkillNames(1), p)) Material.IRON_BOOTS else Material.BEDROCK, effect = false,
      s"${GREEN}[基本スキル]${SkillNames(1)}", List(s"${GRAY}移動速度上昇1の効果が付与されます。",
        s"${GRAY}" + (if (check.isOpened(SkillNames(1), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        s"${GRAY}スキルポイントコスト:300"))
    setItem(3, 1, if (check.isOpened(SkillNames(2), p)) Material.RABBIT_FOOT else Material.BEDROCK, effect = false,
      s"${GREEN}[基本スキル]${SkillNames(2)}", List(s"${GRAY}跳躍力上昇1の効果が付与されます。",
        s"${GRAY}" + (if (check.isOpened(SkillNames(2), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        s"${GRAY}スキルポイントコスト:300"))
    setItem(4, 1, if (check.isOpened(SkillNames(3), p)) Material.IRON_SWORD else Material.BEDROCK, effect = false,
      s"${GREEN}[基本スキル]${SkillNames(3)}", List(s"${GRAY}攻撃力上昇1の効果が付与されます。",
        s"${GRAY}" + (if (check.isOpened(SkillNames(3), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        s"${GRAY}スキルポイントコスト:300"))
    setItem(5, 1, if (check.isOpened(SkillNames(4), p)) Material.IRON_PICKAXE else Material.BEDROCK, effect = false,
      s"${GREEN}[基本スキル]${SkillNames(4)}", List(s"${GRAY}採掘速度上昇1の効果が付与されます。",
        s"${GRAY}" + (if (check.isOpened(SkillNames(4), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        s"${GRAY}スキルポイントコスト:300"))
    setItem(6, 1, if (check.isOpened(SkillNames(5), p)) Material.ENCHANTED_GOLDEN_APPLE else Material.BEDROCK, effect = false,
      s"${GREEN}[基本スキル]${SkillNames(5)}", List(s"${GRAY}再生能力1の効果が付与されます。",
        s"${GRAY}" + (if (check.isOpened(SkillNames(5), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        s"${GRAY}スキルポイントコスト:300"))
    setItem(1, 2, if (check.isOpened(SkillNames(6), p)) Material.ELYTRA else Material.BEDROCK, effect = true,
      s"${GREEN}${SkillNames(6)}",
      List(s"${GRAY}低速落下の効果が付与されます。",
        s"${GRAY}" + (if (check.isOpened(SkillNames(6), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        if (!check.isOpened(SkillNames(6), p)) s"${GRAY}基本スキルをすべて開放" else "",
        s"${GRAY}スキルポイントコスト:600"))
    setItem(2, 2, if (check.isOpened(SkillNames(7), p)) Material.ENDER_EYE else Material.BEDROCK, effect = true,
      s"${GREEN}${SkillNames(7)}",
      List(s"${GRAY}暗視の効果が付与されます。",
        s"${GRAY}" + (if (check.isOpened(SkillNames(7), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        if (!check.isOpened(SkillNames(7), p)) s"${GRAY}基本スキルをすべて開放" else "",
        s"${GRAY}スキルポイントコスト:600"))
    setItem(3, 2, if (check.isOpened(SkillNames(8), p)) Material.LAVA_BUCKET else Material.BEDROCK, effect = true,
      s"${GREEN}${SkillNames(8)}",
      List(s"${GRAY}耐火の効果が付与されます。",
        s"${GRAY}" + (if (check.isOpened(SkillNames(8), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        if (!check.isOpened(SkillNames(8), p)) s"${GRAY}基本スキルをすべて開放" else "",
        s"${GRAY}スキルポイントコスト:600"))
    setItem(4, 2, if (check.isOpened(SkillNames(9), p)) Material.WATER_BUCKET else Material.BEDROCK, effect = true,
      s"${GREEN}${SkillNames(9)}",
      List(s"${GRAY}水中呼吸の効果が付与されます。",
        s"${GRAY}" + (if (check.isOpened(SkillNames(9), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        if (!check.isOpened(SkillNames(9), p)) s"${GRAY}基本スキルをすべて開放" else "",
        s"${GRAY}スキルポイントコスト:600"))
    setSkullItem(8, 6, p, s"${GREEN}クリックですべてのスキル選択を解除できます。", List(
      s"${GRAY}現在保有中のスキル開放ポイント:" + skillData.getSkillOpenPoint(p)
    ))
    setItem(1, 3, if (check.isOpened(SkillNames(10), p)) Material.SHIELD else Material.BEDROCK, effect = true,
      s"${GREEN}${SkillNames(10)}", List(s"${GRAY}耐性2の効果が付与されます。",
        s"${GRAY}" + (if (check.isOpened(SkillNames(10), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        s"${GRAY}スキルポイントコスト:600"))
    setItem(2, 3, if (check.isOpened(SkillNames(11), p)) Material.IRON_BOOTS else Material.BEDROCK, effect = true,
      s"${GREEN}${SkillNames(11)}", List(s"${GRAY}移動速度上昇2の効果が付与されます。",
        s"${GRAY}" + (if (check.isOpened(SkillNames(11), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        s"${GRAY}スキルポイントコスト:600"))
    setItem(3, 3, if (check.isOpened(SkillNames(12), p)) Material.RABBIT_FOOT else Material.BEDROCK, effect = true,
      s"${GREEN}${SkillNames(12)}", List(s"${GRAY}跳躍力上昇2の効果が付与されます。",
        s"${GRAY}" + (if (check.isOpened(SkillNames(12), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        s"${GRAY}スキルポイントコスト:600"))
    setItem(4, 3, if (check.isOpened(SkillNames(13), p)) Material.IRON_SWORD else Material.BEDROCK, effect = true,
      s"${GREEN}${SkillNames(13)}", List(s"${GRAY}攻撃力上昇2の効果が付与されます。",
        s"${GRAY}" + (if (check.isOpened(SkillNames(13), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        s"${GRAY}スキルポイントコスト:600"))
    setItem(5, 3, if (check.isOpened(SkillNames(14), p)) Material.IRON_PICKAXE else Material.BEDROCK, effect = true,
      s"${GREEN}${SkillNames(14)}", List(s"${GRAY}採掘速度上昇2の効果が付与されます。",
        s"${GRAY}" + (if (check.isOpened(SkillNames(14), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        s"${GRAY}スキルポイントコスト:600"))
    setItem(6, 3, if (check.isOpened(SkillNames(15), p)) Material.ENCHANTED_GOLDEN_APPLE else Material.BEDROCK, effect = true,
      s"${GREEN}${SkillNames(15)}", List(s"${GRAY}再生能力2の効果が付与されます。",
        s"${GRAY}" + (if (check.isOpened(SkillNames(15), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        s"${GRAY}スキルポイントコスト:600"))
    setItem(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, effect = false, s"${GREEN}メニューに戻る", List(s"${GRAY}メニューに戻ります。"))
    registerMotion(motion)
    open()
  }

  def motion(player: Player, index: Int): Unit = {
    val toggle = new skillToggleClass(player, ryoServerAssist)
    index match {
      case 0 => toggle.effect(PotionEffectType.DAMAGE_RESISTANCE, 0, 300, SkillNames.head)
      case 1 => toggle.effect(PotionEffectType.SPEED, 0, 300, SkillNames(1))
      case 2 => toggle.effect(PotionEffectType.JUMP, 0, 300, SkillNames(2))
      case 3 => toggle.effect(PotionEffectType.INCREASE_DAMAGE, 0, 300, SkillNames(3))
      case 4 => toggle.effect(PotionEffectType.FAST_DIGGING, 0, 300, SkillNames(4))
      case 5 => toggle.effect(PotionEffectType.REGENERATION, 0, 300, SkillNames(5))
      case 9 => toggle.effect(PotionEffectType.SLOW_FALLING, 0, 600, SkillNames(6))
      case 10 => toggle.effect(PotionEffectType.NIGHT_VISION, 0, 600, SkillNames(7))
      case 11 => toggle.effect(PotionEffectType.FIRE_RESISTANCE, 0, 600, SkillNames(8))
      case 12 => toggle.effect(PotionEffectType.WATER_BREATHING, 0, 600, SkillNames(9))
      case 18 => toggle.effect(PotionEffectType.DAMAGE_RESISTANCE, 1, 600, SkillNames(10))
      case 19 => toggle.effect(PotionEffectType.SPEED, 1, 600, SkillNames(11))
      case 20 => toggle.effect(PotionEffectType.JUMP, 1, 600, SkillNames(12))
      case 21 => toggle.effect(PotionEffectType.INCREASE_DAMAGE, 1, 600, SkillNames(13))
      case 22 => toggle.effect(PotionEffectType.FAST_DIGGING, 1, 600, SkillNames(14))
      case 23 => toggle.effect(PotionEffectType.REGENERATION, 1, 600, SkillNames(15))
      case 45 => new SkillCategoryMenu(ryoServerAssist).openSkillCategoryMenu(player)
      case 52 =>
        toggle.allEffectClear(p)
        p.sendMessage(s"${AQUA}スキルをすべて無効化しました。")
      case _ =>
    }
  }

}
