package com.ryoserver.Gacha

import enumeratum.{Enum, EnumEntry}

sealed abstract class Rarity extends EnumEntry

object Rarity extends Enum[Rarity] {

  override def values: IndexedSeq[Rarity] = findValues

  case object special extends Rarity

  case object bigPer extends Rarity

  case object per extends Rarity

  case object miss extends Rarity
}
