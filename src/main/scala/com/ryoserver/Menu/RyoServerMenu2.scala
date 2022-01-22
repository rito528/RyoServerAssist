package com.ryoserver.Menu

import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.RyoServerAssist
import org.bukkit.ChatColor._
import org.bukkit.{Material, Sound}
import org.bukkit.entity.Player

class RyoServerMenu2(ryoServerAssist: RyoServerAssist) extends Menu {

  override val slot: Int = 6
  override var name: String = "りょう鯖メニュー2"
  override var p: Player = _

  def openPage2(player: Player): Unit = {
    p = player
    val menuMotion = new MenuMotion(ryoServerAssist)
    setButton(MenuButton(7, 1, Material.FLOWER_BANNER_PATTERN, s"${GREEN}Webサイトのリンクを表示します。", List(s"${GRAY}クリックで表示します。"))
    .setLeftClickMotion(menuMotion.sendSiteURL(_, "web")))
    setButton(MenuButton(8, 1, Material.FLOWER_BANNER_PATTERN, s"${GREEN}Dynmapサイトのリンクを表示します。", List(s"${GRAY}クリックで表示します。"))
    .setLeftClickMotion(menuMotion.sendSiteURL(_, "dynmap")))
    setButton(MenuButton(9, 1, Material.FLOWER_BANNER_PATTERN, s"${GREEN}投票サイトのリンクを表示します。", List(s"${GRAY}クリックで表示します。"))
    .setLeftClickMotion(menuMotion.sendSiteURL(_, "vote")))
    setButton(MenuButton(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, s"${GREEN}前のページに移動します。", List(s"${GRAY}クリックで移動します。"))
    .setLeftClickMotion(new RyoServerMenu1(ryoServerAssist).menu _))
    p.playSound(p.getLocation,Sound.BLOCK_IRON_TRAPDOOR_OPEN,1,1)
    build(new RyoServerMenu2(ryoServerAssist).openPage2)
    open()
  }

}
