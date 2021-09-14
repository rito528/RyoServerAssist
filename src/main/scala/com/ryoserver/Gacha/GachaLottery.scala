package com.ryoserver.Gacha

import scala.util.Random

class GachaLottery extends Rarity {

  def lottery(): Rarity = {
    val random = new Random()
    val r = random.nextDouble()
    if ((GachaLoader.special / 100) >= r) rarity.special
    else if ((GachaLoader.bigPer / 100) >= r) rarity.bigPer
    else if ((GachaLoader.per / 100) >= r) rarity.per
    else rarity.miss
  }


}
