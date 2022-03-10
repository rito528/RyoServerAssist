package com.ryoserver.NeoStack

import org.bukkit.inventory.ItemStack

trait TNeoStackPageRepository {

  def store(): Unit

  def restore(): Unit

  def getPageBy(page: Int): List[ItemStack]

}
