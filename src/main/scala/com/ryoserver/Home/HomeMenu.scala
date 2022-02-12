package com.ryoserver.Home

import com.ryoserver.Menu.Button.{Button, ButtonMotion}
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{Menu, MenuFrame}
import com.ryoserver.util.ItemStackBuilder
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player

class HomeMenu extends Menu {

  override val frame: MenuFrame = MenuFrame(3, "ホームメニュー")

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    val layout = for {
      x <- 3 to 7 by 2
    } yield {
      val index = (x - 1) / 2
      val compute = computeHomeMenuButton(player)
      import compute._
      Map(
        getLayOut(x, 1) -> getSetHomeButton(index),
        getLayOut(x, 2) -> getTeleportHomeButton(index),
        getLayOut(x, 3) -> getLockHomeButton(index)
      )
    }
    layout(0) ++ layout(1) ++ layout(2)
  }

}

private case class computeHomeMenuButton(player: Player) {
  private val beds = List(
    Material.WHITE_BED,
    Material.BLUE_BED,
    Material.BLACK_BED
  )
  private val homeGateway = new HomeGateway(player)

  def getSetHomeButton(homeNum: Int): Button = {
    Button(
      ItemStackBuilder
        .getDefault(beds(homeNum - 1))
        .title(s"${GREEN}ホーム${homeNum}を設定します。")
        .lore(List(s"${GRAY}クリックでホーム${homeNum}を設定します。"))
        .build(),
      ButtonMotion { _ =>
        homeGateway.setHomePoint(homeNum, player.getLocation)
        new HomeMenu().open(player)
      }
    )
  }

  def getTeleportHomeButton(homeNum: Int): Button = {
    Button(
      ItemStackBuilder
        .getDefault(Material.COMPASS)
        .title(s"${WHITE}ホーム${homeNum}にテレポートします。")
        .lore(List(s"${GRAY}テレポート地点: ${homeGateway.getLocationString(homeNum).getOrElse("設定されていません")}"))
        .build(),
      ButtonMotion { _ =>
        homeGateway.teleportHome(homeNum)
      }
    )
  }

  def getLockHomeButton(homeNum: Int): Button = {
    Button(
      ItemStackBuilder
        .getDefault(if (homeGateway.isHomeLocked(homeNum)) Material.RED_WOOL else Material.LIGHT_BLUE_WOOL)
        .title(s"$RESET${WHITE}ホーム$homeNum${if (homeGateway.isHomeLocked(homeNum)) "のロックを解除します。" else "をロックします。"}")
        .lore(List(s"${GRAY}クリックでホーム${homeNum}をロックを切り替えます。"))
        .build(),
      ButtonMotion { _ =>
        homeGateway.toggleLock(homeNum)
        new HomeMenu().open(player)
      }
    )
  }
}
