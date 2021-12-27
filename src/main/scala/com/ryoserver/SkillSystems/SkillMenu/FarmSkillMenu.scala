package com.ryoserver.SkillSystems.SkillMenu

import com.ryoserver.Menu.Menu
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Player.Data
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
      List(s"${GRAY}1*3の範囲の作物を成長させます。"
        , s"${GRAY}消費スキルポイント:15"
        , s"${GRAY} ${if (isSkillOpened(p, "アップダウンブレイク")) "クリックで選択します。" else "クリックで開放します。"}"))
    setItem(4, 1, if (isSkillOpened(p, "ワイドグロー")) Material.STONE_HOE else Material.BEDROCK, effect = false, s"${GREEN}ワイドグロー",
      List(s"${GRAY}1*5の範囲の作物を成長させます。"
        , s"${GRAY}消費スキルポイント:30"
        , s"${GRAY} ${if (isSkillOpened(p, "アップダウンブレイク")) "クリックで選択します。" else "クリックで開放します。"}"))
    setItem(6, 1, if (isSkillOpened(p, "ラウンドグロー")) Material.IRON_HOE else Material.BEDROCK, effect = false, s"${GREEN}ラウンドグロー",
      List(s"${GRAY}3*3の範囲の作物を成長させます。"
        , s"${GRAY}消費スキルポイント:55"
        , s"${GRAY} ${if (isSkillOpened(p, "アップダウンブレイク")) "クリックで選択します。" else "クリックで開放します。"}"))
    setItem(2, 3, if (isSkillOpened(p, "ウイングハーベスト")) Material.WOODEN_HOE else Material.BEDROCK, effect = false, s"${GREEN}ウイングハーベスト",
      List(s"${GRAY}1*3の範囲の作物を収穫します。"
        , s"${GRAY}消費スキルポイント:9"
        , s"${GRAY} ${if (isSkillOpened(p, "アップダウンブレイク")) "クリックで選択します。" else "クリックで開放します。"}"))
    setItem(4, 3, if (isSkillOpened(p, "ワイドハーベスト")) Material.STONE_HOE else Material.BEDROCK, effect = false, s"${GREEN}ワイドハーベスト",
      List(s"${GRAY}1*5の範囲の作物を収穫します。"
        , s"${GRAY}消費スキルポイント:15"
        , s"${GRAY} ${if (isSkillOpened(p, "アップダウンブレイク")) "クリックで選択します。" else "クリックで開放します。"}"))
    setItem(6, 3, if (isSkillOpened(p, "ラウンドハーベスト")) Material.IRON_HOE else Material.BEDROCK, effect = false, s"${GREEN}ラウンドハーベスト",
      List(s"${GRAY}3*3の範囲の作物を収穫します。"
        , s"${GRAY}消費スキルポイント:21"
        , s"${GRAY} ${if (isSkillOpened(p, "アップダウンブレイク")) "クリックで選択します。" else "クリックで開放します。"}"))
    setItem(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, effect = false, s"${GREEN}スキルカテゴリ選択画面に戻ります。", List(s"${GRAY}クリックで戻ります。"))
    setSkullItem(5, 6, p, s"${GREEN}スキル選択を解除します。", List(s"${GRAY}現在保有中の特殊スキル解放ポイント:" + Data.playerData(p.getUniqueId).specialSkillOpenPoint))
    registerMotion(motion)
    open()
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
