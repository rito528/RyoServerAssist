package com.ryoserver.Menu

import org.bukkit.entity.Player

import scala.collection.mutable

object MenuData {

  val data: mutable.Map[String, (Player, Int) => Unit] = mutable.Map.empty
  val dataNeedClick: mutable.Map[String, (Player, Int, Boolean) => Unit] = mutable.Map.empty
  val partButton: mutable.Map[String, Boolean] = mutable.Map.empty
  val Buttons: mutable.Map[String, Array[Int]] = mutable.Map.empty

  var rightClickButtons: Map[String,Map[Int, Player => Unit]] = Map.empty
  var leftClickButtons: Map[String,Map[Int, Player => Unit]] = Map.empty

}
