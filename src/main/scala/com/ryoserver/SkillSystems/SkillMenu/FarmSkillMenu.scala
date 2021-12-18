package com.ryoserver.SkillSystems.SkillMenu

import com.ryoserver.Menu.Menu
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.SkillSystems.Skill.SpecialSkillPlayerData.breakSkillToggle
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player

class FarmSkillMenu extends Menu {

  override var name: String = "農業系スキル選択"
  override val slot: Int = 6
  override var p: Player = _

  def openFarmSkillMenu(player: Player): Unit = {
    p = player
    setItem(2,1,Material.WOODEN_HOE,effect = false,s"${GREEN}ウインググロー",
      List(s"${GRAY}1*3の範囲の作物を成長させます。"
        ,s"${GRAY}消費スキルポイント:9"
        ,s"${GRAY}クリックで選択します。"))
    setItem(4,1,Material.STONE_HOE,effect = false,s"${GREEN}ワイドグロー",
      List(s"${GRAY}1*5の範囲の作物を成長させます。"
        ,s"${GRAY}消費スキルポイント:15"
        ,s"${GRAY}クリックで選択します。"))
    setItem(6,1,Material.IRON_HOE,effect = false,s"${GREEN}ラウンドグロー",
      List(s"${GRAY}3*3の範囲の作物を成長させます。"
        ,s"${GRAY}消費スキルポイント:21"
        ,s"${GRAY}クリックで選択します。"))
    setItem(2,3,Material.WOODEN_HOE,effect = false,s"${GREEN}ウイングハーベスト",
      List(s"${GRAY}1*3の範囲の作物を収穫します。"
        ,s"${GRAY}消費スキルポイント:9"
        ,s"${GRAY}クリックで選択します。"))
    setItem(4,3,Material.STONE_HOE,effect = false,s"${GREEN}ワイドハーベスト",
      List(s"${GRAY}1*5の範囲の作物を収穫します。"
        ,s"${GRAY}消費スキルポイント:15"
        ,s"${GRAY}クリックで選択します。"))
    setItem(6,3,Material.IRON_HOE,effect = false,s"${GREEN}ラウンドハーベスト",
      List(s"${GRAY}3*3の範囲の作物を収穫します。"
        ,s"${GRAY}消費スキルポイント:21"
        ,s"${GRAY}クリックで選択します。"))
    registerMotion(motion)
    open()
  }

  def motion(p:Player,index:Int): Unit = {
    if (getLayOut(2,1) == index) {
      breakSkillToggle(p,"ウインググロー")
    } else if (getLayOut(4,1) == index) {
      breakSkillToggle(p,"ワイドグロー")
    } else if (getLayOut(6,1) == index) {
      breakSkillToggle(p,"ラウンドグロー")
    } else if (getLayOut(2,3) == index) {
      breakSkillToggle(p,"ウイングハーベスト")
    } else if (getLayOut(4,3) == index) {
      breakSkillToggle(p,"ワイドハーベスト")
    } else if (getLayOut(6,3) == index) {
      breakSkillToggle(p,"ラウンドハーベスト")
    }
  }

}
