package com.ryoserver.Menu

import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.RyoServerAssist
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player

class RyoServerMenu2(ryoServerAssist: RyoServerAssist) extends Menu {

  override var name: String = "りょう鯖メニュー"
  override val slot: Int = 6
  override var p: Player = _

  def openPage2(player:Player): Unit = {
    p = player
    setItem(7, 1, Material.FLOWER_BANNER_PATTERN, effect = false, s"${GREEN}Webサイトのリンクを表示します。", List(s"${GRAY}クリックで表示します。"))
    setItem(8, 1, Material.FLOWER_BANNER_PATTERN, effect = false, s"${GREEN}Dynmapサイトのリンクを表示します。", List(s"${GRAY}クリックで表示します。"))
    setItem(9, 1, Material.FLOWER_BANNER_PATTERN, effect = false, s"${GREEN}投票サイトのリンクを表示します。", List(s"${GRAY}クリックで表示します。"))
    setItem(1,6,Material.MAGENTA_GLAZED_TERRACOTTA,effect = false,s"${GREEN}前のページに移動します。",List(s"${GRAY}クリックで移動します。"))
    registerMotion(motion)
    open()
  }

  def motion(p:Player,index:Int): Unit = {
    val menuMotion = new MenuMotion(ryoServerAssist)
    val motions = Map(
    getLayOut(7, 1) -> {
      menuMotion.sendSiteURL(_: Player, "web")
    },
    getLayOut(8, 1) -> {
      menuMotion.sendSiteURL(_: Player, "dynmap")
    },
    getLayOut(9, 1) -> {
      menuMotion.sendSiteURL(_: Player, "vote")
    },
    getLayOut(1,6) -> {
      new RyoServerMenu1(ryoServerAssist).menu _
    })
    if (motions.contains(index)) {
      motions(index)(p)
    }

  }
}
