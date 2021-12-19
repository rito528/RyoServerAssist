package com.ryoserver.SkillSystems.SkillMenu

import com.ryoserver.Menu.{Menu, RyoServerMenu1}
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.RyoServerAssist
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player

class SkillCategoryMenu(ryoServerAssist: RyoServerAssist) extends Menu {
  override var name: String = "スキルカテゴリ選択"
  override val slot: Int = 3
  override var p: Player = _

  def openSkillCategoryMenu(player: Player): Unit = {
    p = player
    setItem(3,2,Material.POTION,effect = false,s"${GREEN}通常スキル",List(s"${GRAY}クリックで開きます。"))
    setItem(5,2,Material.GOLDEN_PICKAXE,effect = true,s"${GREEN}[特殊] 破壊系スキル",List(s"${GRAY}クリックで開きます。"))
    setItem(7,2,Material.GOLDEN_HOE,effect = true,s"${GREEN}[特殊] 農業系スキル",List(s"${GRAY}クリックで開きます。"))
    setItem(1,3,Material.MAGENTA_GLAZED_TERRACOTTA,effect = false,s"${GREEN}メニューに戻ります。",List(s"${GRAY}クリックで戻ります。"))
    registerMotion(motion)
    open()
  }

  def motion(p:Player,index:Int): Unit = {
    if (index == getLayOut(3,2)) {
      new SelectSkillMenu(ryoServerAssist).openMenu(p)
    } else if (index == getLayOut(5,2)) {
      new BreakSkillMenu(ryoServerAssist).openBreakSkillMenu(p)
    } else if (index == getLayOut(7,2)) {
      new FarmSkillMenu(ryoServerAssist).openFarmSkillMenu(p)
    } else if (index == getLayOut(1,3)) {
      new RyoServerMenu1(ryoServerAssist).menu(p)
    }
  }


}
