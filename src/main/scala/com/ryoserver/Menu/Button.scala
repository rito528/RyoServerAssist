package com.ryoserver.Menu

import org.bukkit.{Material, OfflinePlayer}
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

trait Button {

  var rightFunc: Player => Unit = _
  var leftFunc: Player => Unit = _
  var effect: Boolean = false
  var reload = false

  val x: Int
  val y: Int
  val title: String
  val lore: List[String]

  var material: Material
  var itemStack: ItemStack
  var offlinePlayer: OfflinePlayer


  def setEffect(): this.type = {
    effect = true
    this
  }

  def setRightClickMotion(func: Player => Unit): this.type = {
    rightFunc = func
    this
  }

  def setLeftClickMotion(func: Player => Unit): this.type = {
    leftFunc = func
    this
  }

  def setReload(): this.type = {
    reload = true
    this
  }


}
