package com.ryoserver.Menu

import org.bukkit.Material
import org.bukkit.entity.Player

case class MenuButton(x: Int,
                      y: Int,
                      material: Material,
                      title: String,
                      lore: List[String]) {

  var rightFunc: Player => Unit = _
  var leftFunc: Player => Unit = _
  var effect: Boolean = false

  def setEffect(): Unit = {
    effect = true
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
