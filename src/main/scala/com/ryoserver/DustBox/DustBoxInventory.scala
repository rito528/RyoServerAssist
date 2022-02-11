package com.ryoserver.DustBox

import com.ryoserver.Menu.Button.{Button, ButtonMotion}
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{Menu, MenuFrame}
import com.ryoserver.util.ItemStackBuilder
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.{Material, Sound}

class DustBoxInventory extends Menu {

  override val frame: MenuFrame = MenuFrame(6,"ゴミ箱")
  override val partButton: Boolean = true

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    Map(
      getLayOut(5,6) -> computeDustBoxButton(player).execute
    )
  }

}

private case class computeDustBoxButton(player: Player) {
  val execute: Button = Button(
    ItemStackBuilder
      .getDefault(Material.LAVA_BUCKET)
      .title(s"$RED$BOLD[取扱注意！]${RESET}捨てる")
      .lore(List(s"${GRAY}クリックで捨てます。"))
      .build(),
    ButtonMotion{ _ =>
      player.playSound(player.getLocation, Sound.ITEM_BUCKET_FILL_LAVA, 1, 1)
      player.getOpenInventory.getTopInventory.clear()
      new DustBoxInventory().open(player)
    }
  )
}
