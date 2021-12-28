package com.ryoserver.NeoStack

import org.bukkit.entity.Player

import scala.collection.mutable

object PlayerCategory {

  private val selectedCategory = mutable.Map.empty[String, String]

  def getSelectedCategory(p: Player): String = selectedCategory(p.getName)

  def setSelectedCategory(p: Player, category: String): Unit = selectedCategory += (p.getName -> category)

}
