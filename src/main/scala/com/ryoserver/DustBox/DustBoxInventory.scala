package com.ryoserver.DustBox

import com.ryoserver.Inventory.Item.getItem
import org.bukkit.{Bukkit, Material, Sound}
import org.bukkit.entity.Player

import scala.jdk.CollectionConverters._

class DustBoxInventory {

  def openDustBox(p:Player): Unit = {
    val inv = Bukkit.createInventory(null, 54, "ゴミ箱")
    inv.setItem(49, getItem(Material.LAVA_BUCKET, "[取扱注意！]捨てる", List("クリックで捨てます。").asJava))
    p.openInventory(inv)
    p.playSound(p.getLocation,Sound.BLOCK_BARREL_OPEN,1,1)
  }

}
