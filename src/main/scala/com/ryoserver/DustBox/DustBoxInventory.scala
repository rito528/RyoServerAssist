package com.ryoserver.DustBox

import com.ryoserver.Menu.Menu
import com.ryoserver.Menu.MenuLayout.getLayOut
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.{Material, Sound}

class DustBoxInventory extends Menu {

  override val slot: Int = 6
  override var name: String = "ゴミ箱"
  override var p: Player = _

  def openDustBox(player: Player): Unit = {
    p = player
    setItem(5, 6, Material.LAVA_BUCKET, effect = false, s"$RED$BOLD[取扱注意！]${RESET}捨てる", List(s"${GRAY}クリックで捨てます。"))
    partButton = true
    buttons :+= getLayOut(5, 6)
    registerMotion(motion)
    open()
    p.playSound(p.getLocation, Sound.BLOCK_BARREL_OPEN, 1, 1)
  }

  def motion(p: Player, index: Int): Unit = {
    if (index == getLayOut(5, 6)) {
      inv.get.clear()
      p.playSound(p.getLocation, Sound.ITEM_BUCKET_FILL_LAVA, 1, 1)
      new DustBoxInventory().openDustBox(p)
    }
  }

}
