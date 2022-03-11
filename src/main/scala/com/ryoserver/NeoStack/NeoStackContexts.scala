package com.ryoserver.NeoStack

import org.bukkit.inventory.ItemStack

case class RawNeoStackPageContext(category: Category,page: Int) {
  require(page >= 0)
}

case class RawNeoStackItemAmountContext(itemStack: ItemStack,amount: Int) {
  require(itemStack != null && itemStack.getAmount == 1 && amount >= 0)
}
