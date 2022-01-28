package com.ryoserver.SkillSystems.SkillMenu

import com.ryoserver.Menu.{Menu, MenuButton, MenuSkull}
import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.Skill.SpecialSkillPlayerData
import com.ryoserver.SkillSystems.Skill.SpecialSkillPlayerData.{isSkillOpened, skillToggle}
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player

class FarmSkillMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  override val slot: Int = 6
  override var name: String = "農業系スキル選択"
  override var p: Player = _

  def openFarmSkillMenu(player: Player): Unit = {
    p = player
    setButton(MenuButton(2, 1, getIcon("ウインググロー",Material.WOODEN_HOE), s"${GREEN}ウインググロー",
      getGrowSkillLore("ウインググロー", "1*3", 15))
      .setLeftClickMotion(toggle(_,"ウインググロー"))
      .setReload())
    setButton(MenuButton(4, 1, getIcon("ワイドグロー",Material.STONE_HOE), s"${GREEN}ワイドグロー",
      getGrowSkillLore("ワイドグロー", "1*5", 30))
      .setLeftClickMotion(toggle(_,"ワイドグロー"))
      .setReload())
    setButton(MenuButton(6, 1, getIcon("ラウンドグロー",Material.IRON_HOE), s"${GREEN}ラウンドグロー",
      getGrowSkillLore("ラウンドグロー", "3*3", 55))
      .setLeftClickMotion(toggle(_,"ラウンドグロー"))
      .setReload())
    setButton(MenuButton(2, 3, getIcon("ウイングハーベスト",Material.WOODEN_HOE), s"${GREEN}ウイングハーベスト",
      getHarvestSkillLore("ウイングハーベスト", "1*3", 9))
      .setLeftClickMotion(toggle(_,"ウイングハーベスト"))
      .setReload())
    setButton(MenuButton(4, 3, getIcon("ワイドハーベスト",Material.STONE_HOE), s"${GREEN}ワイドハーベスト",
      getHarvestSkillLore("ワイドハーベスト", "1*5", 15))
      .setLeftClickMotion(toggle(_,"ワイドハーベスト"))
      .setReload())
    setButton(MenuButton(6, 3, getIcon("ラウンドハーベスト",Material.IRON_HOE), s"${GREEN}ラウンドハーベスト",
      getHarvestSkillLore("ラウンドハーベスト", "3*3", 21))
      .setLeftClickMotion(toggle(_,"ラウンドハーベスト"))
      .setReload())
    setButton(MenuButton(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, s"${GREEN}スキルカテゴリ選択画面に戻ります。", List(s"${GRAY}クリックで戻ります。"))
    .setLeftClickMotion(backPage))
    setSkull(MenuSkull(5, 6, p, s"${GREEN}スキル選択を解除します。", List(s"${GRAY}現在保有中の特殊スキル解放ポイント:" + p.getSpecialSkillOpenPoint))
    .setLeftClickMotion(clear))
    build(new FarmSkillMenu(ryoServerAssist).openFarmSkillMenu)
    open()
  }

  private def getIcon(skillName: String,openedIcon: Material): Material = {
    if (isSkillOpened(p, skillName)) {
      openedIcon
    } else {
      Material.BEDROCK
    }
  }

  private def toggle(p: Player,skillName: String): Unit = {
    skillToggle(p, skillName)
  }

  private def clear(p: Player): Unit = {
    if (SpecialSkillPlayerData.getActivatedSkill(p).isDefined) {
      SpecialSkillPlayerData.skillInvalidation(p, SpecialSkillPlayerData.getActivatedSkill(p).get)
    }
    p.sendMessage(s"${AQUA}スキル選択を解除しました。")
  }

  private def backPage(p: Player): Unit = {
    new SkillCategoryMenu(ryoServerAssist).openSkillCategoryMenu(p)
  }

  private def getGrowSkillLore(skillName: String, range: String, skillPoint: Int): List[String] = {
    List(s"$GRAY${range}の範囲の作物を成長させます。"
      , s"${GRAY}消費スキルポイント:$skillPoint"
      , s"$GRAY ${if (isSkillOpened(p, skillName)) "クリックで選択します。" else "クリックで開放します。"}"
      , s"${if (!isSkillOpened(p, skillName)) s"$GRAY[解放条件]" else ""}"
      , s"${if (!isSkillOpened(p, skillName)) s"$GRAY・特殊スキル解放ポイントを10ポイント消費" else ""}"
      , s"${if (!isSkillOpened(p, skillName) && skillName != "ウインググロー") s"$GRAY・下位スキルをすべて開放" else ""}")
      .filterNot(_ == "")
  }

  private def getHarvestSkillLore(skillName: String, range: String, skillPoint: Int): List[String] = {
    List(s"$GRAY${range}の範囲の作物を収穫します。"
      , s"${GRAY}消費スキルポイント:$skillPoint"
      , s"${GRAY}種植え機能を利用した場合は収穫したブロック分だけスキルポイントを追加で消費します。"
      , s"$GRAY ${if (isSkillOpened(p, skillName)) "クリックで選択します。" else "クリックで開放します。"}"
      , s"${if (!isSkillOpened(p, skillName)) s"$GRAY[解放条件]" else ""}"
      , s"${if (!isSkillOpened(p, skillName)) s"$GRAY・特殊スキル解放ポイントを10ポイント消費" else ""}"
      , s"${if (!isSkillOpened(p, skillName) && skillName != "ウイングハーベスト") s"$GRAY・下位スキルをすべて開放" else ""}")
      .filterNot(_ == "")
  }

}
