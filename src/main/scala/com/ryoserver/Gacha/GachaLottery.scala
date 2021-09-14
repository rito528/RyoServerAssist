package com.ryoserver.Gacha

import org.bukkit.Material

import java.security.SecureRandom
import scala.util.Random

class GachaLottery extends Rarity {

  /*
    レアリティの抽選を行う
   */
  def lottery(): Rarity = {
    val random = new Random()
    val r = random.nextDouble()
    if ((GachaLoader.special / 100) >= r) rarity.special
    else if ((GachaLoader.bigPer / 100) >= r) rarity.bigPer
    else if ((GachaLoader.per / 100) >= r) rarity.per
    else rarity.miss
  }

  /*
  アイテムの抽選を行う
   */
  private val random = SecureRandom.getInstance("SHA1PRNG")
  def itemLottery(rarity:Int): Material = {
    rarity match {
      case 1 =>
        val r = random.nextInt(GachaLoader.missItemList.length)
        Material.matchMaterial(GachaLoader.missItemList(r))
      case 2 =>
        val r = random.nextInt(GachaLoader.perItemList.length)
        Material.matchMaterial(GachaLoader.perItemList(r))
      case 3 =>
        val r = random.nextInt(GachaLoader.perItemList.length)
        Material.matchMaterial(GachaLoader.bigPerItemList(r))
      case 4 =>
        val r = random.nextInt(GachaLoader.perItemList.length)
        Material.matchMaterial(GachaLoader.specialItemList(r))
    }
  }

}
