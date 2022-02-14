package com.ryoserver.Quest

import enumeratum.{Enum, EnumEntry}
import org.bukkit.Material
import org.bukkit.entity.EntityType

case class QuestDataContext(questName: String,
                             questType: QuestType,
                             minLevel: Int,
                             maxLevel: Int,
                             exp: Double,
                             requireList: Map[String, Int])

trait MaterialOrEntityType[T]

object MaterialOrEntityType {
  implicit object materialInstance extends MaterialOrEntityType[Material]
  implicit object entityTypeInstance extends MaterialOrEntityType[EntityType]
}

case class PlayerQuestDataContext[materialOrEntityType: MaterialOrEntityType](selectedQuest: Option[String],
                                                                              progress: Option[Map[materialOrEntityType, Int]],
                                                                              bookmarks: List[String])

/*
  クエストタイプの型
 */
sealed abstract class QuestType(val name: String,val dataName: String) extends EnumEntry

object QuestType extends Enum[QuestType] {

  override def values: IndexedSeq[QuestType] = findValues

  case object delivery extends QuestType("納品","delivery")

  case object suppression extends QuestType("討伐","suppression")
}

/*
  クエストのソートタイプ
 */
sealed abstract class QuestSortContext(val name: String) extends EnumEntry

object QuestSortContext extends Enum[QuestSortContext] {

  override def values: IndexedSeq[QuestSortContext] = findValues

  case object normal extends QuestSortContext("通常")

  case object neoStack extends QuestSortContext("ネオスタックにあるアイテム")

  case object bookMark extends QuestSortContext("ブックマーク")
}

