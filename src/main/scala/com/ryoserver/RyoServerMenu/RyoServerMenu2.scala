package com.ryoserver.RyoServerMenu

import com.ryoserver.Config.ConfigData.{getConfig, loadConfig}
import com.ryoserver.Menu.Button.{Button, ButtonMotion}
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{Menu, MenuButton, MenuFrame, MenuOld}
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.ItemStackBuilder
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.{Material, Sound}

class RyoServerMenu2() extends Menu {

  override val frame: MenuFrame = MenuFrame(6,"りょう鯖メニュー2")

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    val compute = computeRyoServerMenu2(player)
    import compute._
    Map(
      getLayOut(7,1) -> webSite,
      getLayOut(8,1) -> dynmap,
      getLayOut(9,1) -> voteSite
    )
  }

}

private case class computeRyoServerMenu2(player: Player) {
  val webSite: Button = Button(
    ItemStackBuilder
      .getDefault(Material.FLOWER_BANNER_PATTERN)
      .title(s"${GREEN}Webサイトのリンクを表示します。")
      .lore(List(s"${GRAY}クリックで表示します。"))
      .build(),
    ButtonMotion{_ =>
      player.sendMessage(getConfig.webSite)
    }
  )

  val dynmap: Button = Button(
    ItemStackBuilder
      .getDefault(Material.FLOWER_BANNER_PATTERN)
      .title(s"${GREEN}Dynmapのリンクを表示します。")
      .lore(List(s"${GRAY}クリックで表示します。"))
      .build(),
    ButtonMotion{_ =>
      player.sendMessage(getConfig.dynmap)
    }
  )

  val voteSite: Button = Button(
    ItemStackBuilder
      .getDefault(Material.FLOWER_BANNER_PATTERN)
      .title(s"${GREEN}投票サイトのリンクを表示します。")
      .lore(List(s"${GRAY}クリックで表示します。"))
      .build(),
    ButtonMotion{_ =>
      player.sendMessage(getConfig.monocraft)
      player.sendMessage(getConfig.JapanMinecraftServers)
    }
  )
}
