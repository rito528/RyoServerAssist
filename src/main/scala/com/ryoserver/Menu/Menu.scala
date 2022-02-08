package com.ryoserver.Menu

import com.ryoserver.Menu.Contexts.MenuFrame
import org.bukkit.entity.Player

trait Menu {

  val frame: MenuFrame

  def settingMenuLayout(player: Player): Unit

  def open(player: Player): Unit = {
  }

}
