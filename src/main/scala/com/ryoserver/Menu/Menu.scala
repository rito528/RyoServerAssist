package com.ryoserver.Menu

import com.ryoserver.Menu.Button.Button
import com.ryoserver.Menu.Contexts.MenuFrame
import com.ryoserver.Menu.session.MenuSession
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

trait Menu {

  val frame: MenuFrame

  def settingMenuLayout(player: Player): Map[Int,Button]

  def open(player: Player): Unit = {
    val layout = settingMenuLayout(player)
    val session = new MenuSession(frame) {
      override val currentLayout: Map[Int, Button] = layout
    }
    session.setLayout(layout)
    session.openInventory(player)
  }

}
