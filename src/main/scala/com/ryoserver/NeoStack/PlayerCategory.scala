package com.ryoserver.NeoStack

import org.bukkit.entity.Player

import scala.collection.mutable

object PlayerCategory {

  private var selectedCategory = mutable.Map.empty[String, String]

  def getSelectedCategory(p: Player): String = selectedCategory(p.getName)

  def setSelectedCategory(p: Player, category: String): Unit = {
    val pName = p.getName
    selectedCategory = selectedCategory
      .filterNot { case (name, _) => name == pName }
    selectedCategory += (pName -> category)
  }

}
