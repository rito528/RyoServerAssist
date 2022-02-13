package com.ryoserver.Gacha

import org.bukkit.inventory.ItemStack

import java.security.SecureRandom

class GachaLottery extends Rarity_trait {

  private val random = SecureRandom.getInstance("SHA1PRNG")

  /*
    レアリティの抽選を行う
   */
  def lottery(): Rarity_trait = {
    val r = random.nextDouble()
    if ((GachaLoader.special / 100) >= r) rarity.special
    else if ((GachaLoader.bigPer / 100) >= r) rarity.bigPer
    else if ((GachaLoader.per / 100) >= r) rarity.per
    else rarity.miss
  }

  /*
  アイテムの抽選を行う
   */
  def itemLottery(rarity: Int): ItemStack = {
    rarity match {
      case 1 =>
        val r = (Math.random() * GachaLoader.missItemList.size).toInt
        GachaLoader.missItemList.toList(r)
      case 2 =>
        val r = (Math.random() * GachaLoader.perItemList.size).toInt
        GachaLoader.perItemList.toList(r)
      case 3 =>
        val r = (Math.random() * GachaLoader.bigPerItemList.size).toInt
        GachaLoader.bigPerItemList.toList(r)
      case 4 =>
        val r = (Math.random() * GachaLoader.specialItemList.size).toInt
        GachaLoader.specialItemList.toList(r)
    }
  }

}
