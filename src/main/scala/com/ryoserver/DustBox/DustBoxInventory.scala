package com.ryoserver.DustBox

import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{Menu, MenuButton}
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.{Material, Sound}

class DustBoxInventory extends Menu {

  override val slot: Int = 6
  override var name: String = "ゴミ箱"
  override var p: Player = _

  def openDustBox(player: Player): Unit = {
    p = player
    setButton(MenuButton(5, 6, Material.LAVA_BUCKET, s"$RED$BOLD[取扱注意！]${RESET}捨てる", List(s"${GRAY}クリックで捨てます。"))
      .setLeftClickMotion(dispose)
      .setReload())
    partButton = true
    buttons :+= getLayOut(5, 6)
    build(new DustBoxInventory().openDustBox)
    open()
    p.playSound(p.getLocation, Sound.BLOCK_BARREL_OPEN, 1, 1)
  }

  private def dispose(p: Player): Unit = {
    inv.get.clear()
    p.playSound(p.getLocation, Sound.ITEM_BUCKET_FILL_LAVA, 1, 1)
  }

}
