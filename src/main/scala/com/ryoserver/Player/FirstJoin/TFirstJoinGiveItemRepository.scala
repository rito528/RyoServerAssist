package com.ryoserver.Player.FirstJoin

import org.bukkit.inventory.{Inventory, ItemStack}

trait TFirstJoinGiveItemRepository {

  def store(inv: Inventory): Unit

  def restore(): Unit

  def get(): List[ItemStack]

}
