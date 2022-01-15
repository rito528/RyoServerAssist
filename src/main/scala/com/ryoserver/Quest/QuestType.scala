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

sealed abstract class QuestSortType(val name: String) extends EnumEntry

object QuestSortType extends Enum[QuestSortType] {

  override def values: IndexedSeq[QuestSortType] = findValues

  case object normal extends QuestSortType("通常")

  case object neoStack extends QuestSortType("ネオスタックにあるアイテム")

  case object bookMark extends QuestSortType("ブックマーク")
}
