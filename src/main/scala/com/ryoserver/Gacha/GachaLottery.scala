package com.ryoserver.Gacha

import org.bukkit.inventory.ItemStack

import java.security.SecureRandom

class GachaLottery {

  private val random = SecureRandom.getInstance("SHA1PRNG")

  /*
    レアリティの抽選を行う
   */
  def rarityLottery(): Rarity = {
    val r = random.nextDouble()
    if ((GachaLoader.special / 100) >= r) Rarity.special
    else if ((GachaLoader.bigPer / 100) >= r) Rarity.bigPer
    else if ((GachaLoader.per / 100) >= r) Rarity.per
    else Rarity.miss
  }

  /*
  アイテムの抽選を行う
   */
  def itemLottery(rarity: Rarity): ItemStack = {
    val r = (Math.random() * GachaLoader.getGachaItemData.count(_._2 == rarity)).toInt
    GachaLoader.getGachaItemData.filter(_._2 == rarity).keysIterator.toList(r)
  }

}
