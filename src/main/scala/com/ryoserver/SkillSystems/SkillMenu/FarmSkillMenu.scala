package com.ryoserver.SkillSystems.SkillMenu

import com.ryoserver.Menu.Menu
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Player.Data
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
    setItem(2, 1, if (isSkillOpened(p, "ウインググロー")) Material.WOODEN_HOE else Material.BEDROCK, effect = false, s"${GREEN}ウインググロー",
      getGrowSkillLore("ウインググロー","1*3",15))
    setItem(4, 1, if (isSkillOpened(p, "ワイドグロー")) Material.STONE_HOE else Material.BEDROCK, effect = false, s"${GREEN}ワイドグロー",
      getGrowSkillLore("ワイドグロー","1*5",30))
    setItem(6, 1, if (isSkillOpened(p, "ラウンドグロー")) Material.IRON_HOE else Material.BEDROCK, effect = false, s"${GREEN}ラウンドグロー",
      getGrowSkillLore("ラウンドグロー","3*3",55))
    setItem(2, 3, if (isSkillOpened(p, "ウイングハーベスト")) Material.WOODEN_HOE else Material.BEDROCK, effect = false, s"${GREEN}ウイングハーベスト",
      getHarvestSkillLore("ウイングハーベスト","1*3",9))
    setItem(4, 3, if (isSkillOpened(p, "ワイドハーベスト")) Material.STONE_HOE else Material.BEDROCK, effect = false, s"${GREEN}ワイドハーベスト",
      getHarvestSkillLore("ワイドハーベスト","1*5",15))
    setItem(6, 3, if (isSkillOpened(p, "ラウンドハーベスト")) Material.IRON_HOE else Material.BEDROCK, effect = false, s"${GREEN}ラウンドハーベスト",
      getHarvestSkillLore("ラウンドハーベスト","3*3",21))
    setItem(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, effect = false, s"${GREEN}スキルカテゴリ選択画面に戻ります。", List(s"${GRAY}クリックで戻ります。"))
    setSkullItem(5, 6, p, s"${GREEN}スキル選択を解除します。", List(s"${GRAY}現在保有中の特殊スキル解放ポイント:" + p.getSpecialSkillOpenPoint))
    registerMotion(motion)
    open()
  }

  private def getGrowSkillLore(skillName:String,range:String,skillPoint:Int): List[String] = {
    List(s"$GRAY${range}の範囲の作物を成長させます。"
      , s"${GRAY}消費スキルポイント:$skillPoint"
      , s"$GRAY ${if (isSkillOpened(p, skillName)) "クリックで選択します。" else "クリックで開放します。"}"
      , s"${if (!isSkillOpened(p, skillName)) s"$GRAY[解放条件]" else ""}"
      , s"${if (!isSkillOpened(p, skillName)) s"$GRAY・特殊スキル解放ポイントを10ポイント消費" else ""}"
      , s"${if (!isSkillOpened(p, skillName) && skillName != "ウインググロー") s"$GRAY・下位スキルをすべて開放" else ""}")
      .filterNot(_ == "")
  }

  private def getHarvestSkillLore(skillName:String,range:String,skillPoint:Int): List[String] = {
    List(s"$GRAY${range}の範囲の作物を収穫します。"
      , s"${GRAY}消費スキルポイント:$skillPoint"
      , s"$GRAY ${if (isSkillOpened(p, skillName)) "クリックで選択します。" else "クリックで開放します。"}"
      , s"${if (!isSkillOpened(p, skillName)) s"$GRAY[解放条件]" else ""}"
      , s"${if (!isSkillOpened(p, skillName)) s"$GRAY・特殊スキル解放ポイントを10ポイント消費" else ""}"
      , s"${if (!isSkillOpened(p, skillName) && skillName != "ウイングハーベスト") s"$GRAY・下位スキルをすべて開放" else ""}")
      .filterNot(_ == "")
  }

  def motion(p: Player, index: Int): Unit = {
    if (getLayOut(2, 1) == index) {
      skillToggle(p, "ウインググロー")
      new FarmSkillMenu(ryoServerAssist).openFarmSkillMenu(p)
    } else if (getLayOut(4, 1) == index) {
      skillToggle(p, "ワイドグロー")
      new FarmSkillMenu(ryoServerAssist).openFarmSkillMenu(p)
    } else if (getLayOut(6, 1) == index) {
      skillToggle(p, "ラウンドグロー")
      new FarmSkillMenu(ryoServerAssist).openFarmSkillMenu(p)
    } else if (getLayOut(2, 3) == index) {
      skillToggle(p, "ウイングハーベスト")
      new FarmSkillMenu(ryoServerAssist).openFarmSkillMenu(p)
    } else if (getLayOut(4, 3) == index) {
      skillToggle(p, "ワイドハーベスト")
      new FarmSkillMenu(ryoServerAssist).openFarmSkillMenu(p)
    } else if (getLayOut(6, 3) == index) {
      skillToggle(p, "ラウンドハーベスト")
      new FarmSkillMenu(ryoServerAssist).openFarmSkillMenu(p)
    } else if (getLayOut(1, 6) == index) {
      new SkillCategoryMenu(ryoServerAssist).openSkillCategoryMenu(p)
    } else if (getLayOut(5, 6) == index) {
      if (SpecialSkillPlayerData.getActivatedSkill(p).isDefined) SpecialSkillPlayerData.skillInvalidation(p, SpecialSkillPlayerData.getActivatedSkill(p).get)
      p.sendMessage(s"${AQUA}スキル選択を解除しました。")
    }
  }

}
