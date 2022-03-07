package com.ryoserver.Quest

import enumeratum.{Enum, EnumEntry}
import org.bukkit.Material
import org.bukkit.entity.EntityType

sealed trait MaterialOrEntityType {
  val material: Material = null
  val entityType: EntityType = null
}

object MaterialOrEntityType {
  case class material(override val material: Material) extends MaterialOrEntityType
  case class entityType(override val entityType: EntityType) extends MaterialOrEntityType
}

case class QuestDataContext(questName: String,
                            questType: QuestType,
                            minLevel: Int,
                            maxLevel: Int,
                            exp: Double,
                            requireList: Map[MaterialOrEntityType, Int])

case class PlayerQuestDataContext(selectedQuest: Option[String],
                                  progress: Option[Map[MaterialOrEntityType, Int]],
                                  bookmarks: List[String]) {

  def selectQuest(questName: Option[String]): PlayerQuestDataContext = {
    this.copy(selectedQuest = questName)
  }

  def setProgress(progress: Map[MaterialOrEntityType,Int]): PlayerQuestDataContext = {
    this.copy(progress = Option(progress))
  }

  def changeProgress(materialOrEntityType: MaterialOrEntityType,amount: Int): PlayerQuestDataContext = {
    this.copy(progress = Option(progress.getOrElse(Map.empty) ++ Map(materialOrEntityType -> amount)))
  }

  def addBookmarkQuest(questName: String): PlayerQuestDataContext = {
    this.copy(bookmarks = bookmarks ++ List(questName))
  }

  def removeBookmarkQuest(questName: String): PlayerQuestDataContext = {
    this.copy(bookmarks = bookmarks.filterNot(_ == questName))
  }

}

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

