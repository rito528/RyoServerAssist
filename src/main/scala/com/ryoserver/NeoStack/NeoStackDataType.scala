package com.ryoserver.NeoStack

import org.bukkit.inventory.ItemStack

import java.util.UUID

case class NeoStackDataType(uuid: UUID, savingItemStack: ItemStack, displayItemStack: ItemStack, amount: Int)

case class NeoStackPlayerItemData(itemStack: ItemStack,amount:Int)