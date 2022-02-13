package com.ryoserver.Gacha

import enumeratum.{Enum, EnumEntry}

sealed abstract class Rarity(val id: Int) extends EnumEntry

object Rarity extends Enum[Rarity] {

  override def values: IndexedSeq[Rarity] = findValues

  case object special extends Rarity(3)

  case object bigPer extends Rarity(2)

  case object per extends Rarity(1)

  case object miss extends Rarity(0)
}
