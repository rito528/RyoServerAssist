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

sealed trait QuestSortType extends EnumEntry

object QuestSortType extends Enum[QuestSortType] {

  case object normal extends QuestSortType
  case object neoStack extends QuestSortType
  case object bookMark extends QuestSortType

  override def values: IndexedSeq[QuestSortType] = findValues
}
