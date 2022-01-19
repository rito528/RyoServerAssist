package com.ryoserver.Menu

import org.bukkit.{Material, OfflinePlayer}
import org.bukkit.entity.Player

case class MenuButton(x: Int,
                      y: Int,
                      material: Material,
                      title: String,
                      lore: List[String]) {

  var rightFunc: Player => Unit = _
  var leftFunc: Player => Unit = _
  var effect: Boolean = false

  def setEffect(): MenuButton = {
    effect = true
    this
  }

  def setRightClickMotion(func: Player => Unit): MenuButton = {
    rightFunc = func
    this
  }

  def setLeftClickMotion(func: Player => Unit): MenuButton = {
    leftFunc = func
    this
  }

}

case class MenuSkull(x: Int,
                     y: Int,
                     offlinePlayer: OfflinePlayer,
                     title: String,
                     lore: List[String]) {

  var rightFunc: Player => Unit = _
  var leftFunc: Player => Unit = _
  var effect: Boolean = false

  def setRightClickMotion(func: Player => Unit): MenuSkull = {
    rightFunc = func
    this
  }

  def setLeftClickMotion(func: Player => Unit): MenuSkull = {
    leftFunc = func
    this
  }

}
