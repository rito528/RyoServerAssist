package com.ryoserver.SkillSystems.SkillMenu

import com.ryoserver.Menu.Menu
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.SkillSystems.Skill.SpecialSkillPlayerData.breakSkillToggle
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.ChatColor._

class BreakSkillMenu extends Menu {

  override var name: String = "破壊系スキル選択"
  override val slot: Int = 6
  override var p: Player = _

  def openBreakSkillMenu(player:Player): Unit = {
    p = player
    setItem(2,1,Material.WOODEN_PICKAXE,effect = false,s"${GREEN}ブレイク・デュオ",
      List(s"${GRAY}1*2の範囲を破壊します。"
        ,s"${GRAY}消費スキルポイント:6"
        ,s"${GRAY}クリックで選択します。"))
    setItem(4,1,Material.STONE_PICKAXE,effect = false,s"${GREEN}アップダウンブレイク",
      List(s"${GRAY}1*3の範囲を破壊します。"
        ,s"${GRAY}消費スキルポイント:9"
        ,s"${GRAY}クリックで選択します。"))
    setItem(6,1,Material.STONE_PICKAXE,effect = false,s"${GREEN}パンチングブレイク",
      List(s"${GRAY}3*2の範囲を破壊します。"
        ,s"${GRAY}消費スキルポイント:18"
        ,s"${GRAY}クリックで選択します。"))
    setItem(8,1,Material.STONE_PICKAXE,effect = false,s"${GREEN}トンネルブレイク",
      List(s"${GRAY}3*3の範囲を破壊します。"
        ,s"${GRAY}消費スキルポイント:27"
        ,s"${GRAY}クリックで選択します。"))
    setItem(2,2,Material.STONE_PICKAXE,effect = false,s"${GREEN}ワイドブレイク",
      List(s"${GRAY}5*3の範囲を破壊します。"
        ,s"${GRAY}消費スキルポイント:45"
        ,s"${GRAY}クリックで選択します。"))
    registerMotion(motion)
    open()
  }

  def motion(p:Player,index:Int): Unit = {
    if (getLayOut(2,1) == index) {
      breakSkillToggle(p,"ブレイク・デュオ")
    } else if (getLayOut(4,1) == index) {
      breakSkillToggle(p,"アップダウンブレイク")
    } else if (getLayOut(6,1) == index) {
      breakSkillToggle(p,"パンチングブレイク")
    } else if (getLayOut(8,1) == index) {
      breakSkillToggle(p,"トンネルブレイク")
    } else if (getLayOut(2,2) == index) {
      breakSkillToggle(p,"ワイドブレイク")
    }
  }

}
