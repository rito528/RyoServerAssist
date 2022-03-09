package com.ryoserver.NeoStack

import enumeratum.{Enum, EnumEntry}

sealed abstract class Category(val name:String) extends EnumEntry

object Category extends Enum[Category] {

  override def values: IndexedSeq[Category] = findValues

  case object block extends Category("block")

  case object item extends Category("item")

  case object gachaItem extends Category("gachaItem")

  case object tool extends Category("tool")

  case object food extends Category("food")

  case object redstone extends Category("redstone")

  case object plant extends Category("plant")

}