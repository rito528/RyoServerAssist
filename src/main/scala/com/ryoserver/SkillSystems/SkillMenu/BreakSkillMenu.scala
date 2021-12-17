package com.ryoserver.SkillSystems.SkillMenu

import com.ryoserver.Menu.Menu
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.ChatColor._

class BreakSkillMenu extends Menu {

  override var name: String = "破壊系スキル選択"
  override val slot: Int = 6
  override var p: Player = _

  def openBreakSkillMenu(player:Player): Unit = {
    p = player
    setItem(2,1,Material.WOODEN_PICKAXE,effect = false,s"${GREEN}スキル1",
      List(s"${GRAY}1*2の範囲を破壊します。"
      ,s"${GRAY}消費スキルポイント:1"
      ,s"${GRAY}クリックで選択します。"))
    registerMotion(motion)
    open()
  }

  def motion(p:Player,index:Int): Unit = {

  }

}
