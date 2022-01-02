package com.ryoserver.Quest

import enumeratum._

/*
  このcaseClassはクエストのデータ用の型です。
  第1引数にはMaterialNameもしくはEntityTypeNameを入れてください。
 */

case class QuestType(questName: String,
                     questType: String,
                     minLevel: Int,
                     maxLevel: Int,
                     exp: Double,
                     requireList: Map[String, Int],
                    )

/*
  このobjectはクエスト表示順序を宣言するためのobjectです。
 */

sealed trait questSortType extends EnumEntry

object questSortType extends Enum[questSortType] {

  case object normal extends questSortType
  case object neoStack extends questSortType
  case object bookMark extends questSortType

  override def values: IndexedSeq[questSortType] = findValues
}
