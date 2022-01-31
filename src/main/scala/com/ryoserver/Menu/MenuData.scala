package com.ryoserver.Menu

import org.bukkit.entity.Player

import java.util.UUID
import scala.collection.mutable

object MenuData {

  val partButton: mutable.Map[String, Boolean] = mutable.Map.empty
  val Buttons: mutable.Map[String, Array[Int]] = mutable.Map.empty

  var rightClickButtons: Map[String, Map[Int, Player => Unit]] = Map.empty
  var leftClickButtons: Map[String, Map[Int, Player => Unit]] = Map.empty
  var reloadButtons: Map[String, Set[Int]] = Map.empty
  var openedInv: Map[UUID, Player => Unit] = Map.empty

}
