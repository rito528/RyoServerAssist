package com.ryoserver.Menu

import org.bukkit.entity.Player

trait Button {

  var rightFunc: Player => Unit = _
  var leftFunc: Player => Unit = _
  var effect: Boolean = false
  var reload = false

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
