package com.ryoserver.Menu

import org.bukkit.entity.Player

import scala.collection.mutable

object MenuData {

  val data:mutable.Map[String,(Player,Int) => Unit] = mutable.Map.empty

}
