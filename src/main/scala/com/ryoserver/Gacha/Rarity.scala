package com.ryoserver.Gacha

import enumeratum.{Enum, EnumEntry}

sealed abstract class Rarity(val id: Int,val name:String) extends EnumEntry

object Rarity extends Enum[Rarity] {

  override def values: IndexedSeq[Rarity] = findValues

  case object special extends Rarity(3,"特等")

  case object bigPer extends Rarity(2,"大当たり")

  case object per extends Rarity(1,"あたり")

  case object miss extends Rarity(0,"はずれ")
}
