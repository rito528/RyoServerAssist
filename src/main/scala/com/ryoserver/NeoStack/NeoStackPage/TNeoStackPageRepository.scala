package com.ryoserver.NeoStack.NeoStackPage

import com.ryoserver.NeoStack.Category
import org.bukkit.inventory.ItemStack

trait TNeoStackPageRepository {

  def store(category: Category,page: Int): Unit

  def restore(): Unit

  def getCategoryPageBy(category:Category, page: Int): List[ItemStack]

}
