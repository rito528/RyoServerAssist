package com.ryoserver.Player.FirstJoin

import org.bukkit.inventory.Inventory

trait TFirstJoinGiveItemRepository {

  def store(inv: Inventory): Unit

  def restore(): Unit

}
