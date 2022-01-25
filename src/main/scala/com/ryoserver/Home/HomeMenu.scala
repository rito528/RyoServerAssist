package com.ryoserver.Home

import com.ryoserver.Menu.{Menu, MenuButton}
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.ChatColor._

class HomeMenu extends Menu {

  override val slot: Int = 3
  override var name: String = "ホームメニュー"
  override var p: Player = _

  def openHomeMenu(player: Player): Unit = {
    p = player
    val beds = List(
      Material.WHITE_BED,
      Material.BLUE_BED,
      Material.BLACK_BED
    )
    for (x <- 3 to 7 by 2) {
      val index = (x - 1) / 2
      val homeGateway = new HomeGateway(_)
      setButton(MenuButton(x,1,beds(index - 1),s"$RESET${WHITE}ホーム${index}を設定します。",List(s"${GRAY}クリックでホーム${index}を設定します。"))
      .setLeftClickMotion(player => homeGateway(player).setHomePoint(index,player.getLocation)))
      setButton(MenuButton(x,2,Material.COMPASS,s"${WHITE}ホーム${index}にテレポートします。",
        List(s"${GRAY}テレポート地点: ${homeGateway(p).getLocationString(index).getOrElse("設定されていません")}"))
      .setLeftClickMotion(homeGateway(_).teleportHome(index)))
      setButton(MenuButton(x,3,if (homeGateway(p).isHomeLocked(index)) Material.RED_WOOL else Material.LIGHT_BLUE_WOOL,
        s"$RESET${WHITE}ホーム${index}${if (homeGateway(p).isHomeLocked(index)) "のロックを解除します。" else "をロックします。"}",
        List(s"${GRAY}クリックでホーム${index}をロックを切り替えます。"))
      .setLeftClickMotion(homeGateway(_).toggleLock(index))
      .setReload())
    }
    build(new HomeMenu().openHomeMenu)
    open()
  }

}
