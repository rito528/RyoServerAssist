package com.ryoserver.SkillSystems.SkillMenu

import com.ryoserver.Menu.{Menu, MenuButton, MenuSkull}
import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.RyoServerAssist
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType

class EffectSkillMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  val slot: Int = 6
  var name: String = "通常スキル選択"
  var p: Player = _

  def openMenu(player: Player): Unit = {
    p = player
    setButton(MenuButton(1, 1, if (check.isOpened(SkillNames.head, p)) Material.SHIELD else Material.BEDROCK,
      s"${GREEN}[基本スキル]${SkillNames.head}", List(s"${GRAY}耐性1の効果が付与されます。",
        s"${GRAY}" + (if (check.isOpened(SkillNames.head, p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        s"${GRAY}スキルポイントコスト:300"))
      .setLeftClickMotion(nankurunaisa))
    setButton(MenuButton(2, 1, if (check.isOpened(SkillNames(1), p)) Material.IRON_BOOTS else Material.BEDROCK,
      s"${GREEN}[基本スキル]${SkillNames(1)}", List(s"${GRAY}移動速度上昇1の効果が付与されます。",
        s"${GRAY}" + (if (check.isOpened(SkillNames(1), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        s"${GRAY}スキルポイントコスト:300"))
      .setLeftClickMotion(yoidon))
    setButton(MenuButton(3, 1, if (check.isOpened(SkillNames(2), p)) Material.RABBIT_FOOT else Material.BEDROCK,
      s"${GREEN}[基本スキル]${SkillNames(2)}", List(s"${GRAY}跳躍力上昇1の効果が付与されます。",
        s"${GRAY}" + (if (check.isOpened(SkillNames(2), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        s"${GRAY}スキルポイントコスト:300"))
      .setLeftClickMotion(takaminokenbutu))
    setButton(MenuButton(4, 1, if (check.isOpened(SkillNames(3), p)) Material.IRON_SWORD else Material.BEDROCK,
      s"${GREEN}[基本スキル]${SkillNames(3)}", List(s"${GRAY}攻撃力上昇1の効果が付与されます。",
        s"${GRAY}" + (if (check.isOpened(SkillNames(3), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        s"${GRAY}スキルポイントコスト:300"))
      .setLeftClickMotion(tuyonaru))
    setButton(MenuButton(5, 1, if (check.isOpened(SkillNames(4), p)) Material.IRON_PICKAXE else Material.BEDROCK,
      s"${GREEN}[基本スキル]${SkillNames(4)}", List(s"${GRAY}採掘速度上昇1の効果が付与されます。",
        s"${GRAY}" + (if (check.isOpened(SkillNames(4), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        s"${GRAY}スキルポイントコスト:300"))
      .setLeftClickMotion(horida))
    setButton(MenuButton(6, 1, if (check.isOpened(SkillNames(5), p)) Material.ENCHANTED_GOLDEN_APPLE else Material.BEDROCK,
      s"${GREEN}[基本スキル]${SkillNames(5)}", List(s"${GRAY}再生能力1の効果が付与されます。",
        s"${GRAY}" + (if (check.isOpened(SkillNames(5), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        s"${GRAY}スキルポイントコスト:300"))
      .setLeftClickMotion(zikahu))
    setButton(MenuButton(1, 2, if (check.isOpened(SkillNames(6), p)) Material.ELYTRA else Material.BEDROCK,
      s"${GREEN}${SkillNames(6)}",
      List(s"${GRAY}低速落下の効果が付与されます。",
        s"${GRAY}" + (if (check.isOpened(SkillNames(6), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        if (!check.isOpened(SkillNames(6), p)) s"${GRAY}基本スキルをすべて開放" else "",
        s"${GRAY}スキルポイントコスト:600"))
      .setLeftClickMotion(antibekutoru)
      .setEffect())
    setButton(MenuButton(2, 2, if (check.isOpened(SkillNames(7), p)) Material.ENDER_EYE else Material.BEDROCK,
      s"${GREEN}${SkillNames(7)}",
      List(s"${GRAY}暗視の効果が付与されます。",
        s"${GRAY}" + (if (check.isOpened(SkillNames(7), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        if (!check.isOpened(SkillNames(7), p)) s"${GRAY}基本スキルをすべて開放" else "",
        s"${GRAY}スキルポイントコスト:600"))
      .setLeftClickMotion(nekonome)
      .setEffect())
    setButton(MenuButton(3, 2, if (check.isOpened(SkillNames(8), p)) Material.LAVA_BUCKET else Material.BEDROCK,
      s"${GREEN}${SkillNames(8)}",
      List(s"${GRAY}耐火の効果が付与されます。",
        s"${GRAY}" + (if (check.isOpened(SkillNames(8), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        if (!check.isOpened(SkillNames(8), p)) s"${GRAY}基本スキルをすべて開放" else "",
        s"${GRAY}スキルポイントコスト:600"))
      .setLeftClickMotion(homutekuto)
      .setEffect())
    setButton(MenuButton(4, 2, if (check.isOpened(SkillNames(9), p)) Material.WATER_BUCKET else Material.BEDROCK,
      s"${GREEN}${SkillNames(9)}",
      List(s"${GRAY}水中呼吸の効果が付与されます。",
        s"${GRAY}" + (if (check.isOpened(SkillNames(9), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        if (!check.isOpened(SkillNames(9), p)) s"${GRAY}基本スキルをすべて開放" else "",
        s"${GRAY}スキルポイントコスト:600"))
      .setLeftClickMotion(mizunokokyuu)
      .setEffect())
    setSkull(MenuSkull(8, 6, p, s"${GREEN}クリックですべてのスキル選択を解除できます。", List(
      s"${GRAY}現在保有中のスキル開放ポイント:" + p.getSkillOpenPoint
    ))
      .setLeftClickMotion(clear))
    setButton(MenuButton(1, 3, if (check.isOpened(SkillNames(10), p)) Material.SHIELD else Material.BEDROCK,
      s"${GREEN}${SkillNames(10)}", List(s"${GRAY}耐性2の効果が付与されます。",
        s"${GRAY}" + (if (check.isOpened(SkillNames(10), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        s"${GRAY}スキルポイントコスト:600"))
      .setLeftClickMotion(haganenomentaru)
      .setEffect())
    setButton(MenuButton(2, 3, if (check.isOpened(SkillNames(11), p)) Material.IRON_BOOTS else Material.BEDROCK,
      s"${GREEN}${SkillNames(11)}", List(s"${GRAY}移動速度上昇2の効果が付与されます。",
        s"${GRAY}" + (if (check.isOpened(SkillNames(11), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        s"${GRAY}スキルポイントコスト:600"))
      .setLeftClickMotion(sinsoku)
      .setEffect())
    setButton(MenuButton(3, 3, if (check.isOpened(SkillNames(12), p)) Material.RABBIT_FOOT else Material.BEDROCK,
      s"${GREEN}${SkillNames(12)}", List(s"${GRAY}跳躍力上昇2の効果が付与されます。",
        s"${GRAY}" + (if (check.isOpened(SkillNames(12), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        s"${GRAY}スキルポイントコスト:600"))
      .setLeftClickMotion(pyon)
      .setEffect())
    setButton(MenuButton(4, 3, if (check.isOpened(SkillNames(13), p)) Material.IRON_SWORD else Material.BEDROCK,
      s"${GREEN}${SkillNames(13)}", List(s"${GRAY}攻撃力上昇2の効果が付与されます。",
        s"${GRAY}" + (if (check.isOpened(SkillNames(13), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        s"${GRAY}スキルポイントコスト:600"))
      .setLeftClickMotion(mottotuyonaru)
      .setEffect())
    setButton(MenuButton(5, 3, if (check.isOpened(SkillNames(14), p)) Material.IRON_PICKAXE else Material.BEDROCK,
      s"${GREEN}${SkillNames(14)}", List(s"${GRAY}採掘速度上昇2の効果が付与されます。",
        s"${GRAY}" + (if (check.isOpened(SkillNames(14), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        s"${GRAY}スキルポイントコスト:600"))
      .setLeftClickMotion(saida)
      .setEffect())
    setButton(MenuButton(6, 3, if (check.isOpened(SkillNames(15), p)) Material.ENCHANTED_GOLDEN_APPLE else Material.BEDROCK,
      s"${GREEN}${SkillNames(15)}", List(s"${GRAY}再生能力2の効果が付与されます。",
        s"${GRAY}" + (if (check.isOpened(SkillNames(15), p)) "開放済みです。" else "解放条件:スキル開放ポイント10を消費"),
        s"${GRAY}スキルポイントコスト:600"))
      .setLeftClickMotion(tiyunokago)
      .setEffect())
    setButton(MenuButton(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, s"${GREEN}メニューに戻る", List(s"${GRAY}メニューに戻ります。"))
      .setLeftClickMotion(backPage))
    build(new EffectSkillMenu(ryoServerAssist).openMenu)
    open()
  }

  private def backPage(p: Player): Unit = {
    new SkillCategoryMenu(ryoServerAssist).openSkillCategoryMenu(p)
  }

}
