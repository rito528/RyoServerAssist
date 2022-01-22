package com.ryoserver.SkillSystems.SkillMenu

import com.ryoserver.Menu.{Menu, MenuButton, RyoServerMenu1}
import com.ryoserver.RyoServerAssist
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player

class SkillCategoryMenu(ryoServerAssist: RyoServerAssist) extends Menu {
  override val slot: Int = 3
  override var name: String = "スキルカテゴリ選択"
  override var p: Player = _

  def openSkillCategoryMenu(player: Player): Unit = {
    p = player
    setButton(MenuButton(3, 2, Material.POTION, s"${GREEN}通常スキル", List(s"${GRAY}クリックで開きます。"))
    .setLeftClickMotion(openSkillMenu))
    setButton(MenuButton(5, 2, Material.GOLDEN_PICKAXE, s"$GREEN[特殊] 破壊系スキル", List(s"${GRAY}クリックで開きます。"))
    .setLeftClickMotion(openBreakSkillMenu))
    setButton(MenuButton(7, 2, Material.GOLDEN_HOE, s"$GREEN[特殊] 農業系スキル", List(s"${GRAY}クリックで開きます。"))
    .setLeftClickMotion(openFarmSkillMenu))
    setButton(MenuButton(1, 3, Material.MAGENTA_GLAZED_TERRACOTTA, s"${GREEN}メニューに戻ります。", List(s"${GRAY}クリックで戻ります。"))
    .setLeftClickMotion(openMenu))
    build(new SkillCategoryMenu(ryoServerAssist).openSkillCategoryMenu)
    open()
  }

  private def openSkillMenu(p: Player): Unit = {
    new SelectSkillMenu(ryoServerAssist).openMenu(p)
  }

  private def openBreakSkillMenu(p: Player): Unit = {
    new BreakSkillMenu(ryoServerAssist).openBreakSkillMenu(p)
  }

  private def openFarmSkillMenu(p: Player): Unit = {
    new FarmSkillMenu(ryoServerAssist).openFarmSkillMenu(p)
  }

  private def openMenu(p: Player): Unit = {
    new RyoServerMenu1(ryoServerAssist).menu(p)
  }

}
