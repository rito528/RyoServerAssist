package com.ryoserver.Home

import com.ryoserver.Menu.{Menu, MenuButton}
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.ChatColor._

class HomeMenu extends Menu {

  override val slot: Int = 6
  override var name: String = "ホームメニュー"
  override var p: Player = _

  def openHomeMenu(player: Player): Unit = {
    p = player
    for (x <- 3 to 7 by 2) {
      setButton(MenuButton(x,1,Material.WHITE_BED,s"$RESET${WHITE}ホーム${x}を設定します。",List(s"${GRAY}クリックでホーム${x}を設定します。")))
      setButton(MenuButton(x,2,Material.COMPASS,s"${WHITE}ホーム${x}にテレポートします。",List(s"${GRAY}テレポート地点: ")))
      setButton(MenuButton(x,3,Material.BLUE_WOOL,s"$RESET${WHITE}ホーム${x}をロックします。",List(s"${GRAY}クリックでホーム${x}をロックします。")))
    }
    build(new HomeMenu().openHomeMenu)
    open()
  }

}
