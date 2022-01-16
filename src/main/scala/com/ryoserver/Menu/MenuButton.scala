package com.ryoserver.Menu

import org.bukkit.Material

case class MenuButton(x: Int,
                      y: Int,
                      material: Material,
                      title: String,
                      lore: List[String]) {

  var rightFunc: () => Unit = _
  var leftFunc: () => Unit = _

  def setRightClickMotion(func: () => Unit): MenuButton = {
    rightFunc = func
    this
  }

  def setLeftClickMotion(func: () => Unit): MenuButton = {
    leftFunc = func
    this
  }


}
