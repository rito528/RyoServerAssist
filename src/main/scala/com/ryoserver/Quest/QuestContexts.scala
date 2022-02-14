package com.ryoserver.Quest

case class QuestDataContext(questName: String,
                             questType: String,
                             minLevel: Int,
                             maxLevel: Int,
                             exp: Double,
                             requireList: Map[String, Int])

case class PlayerQuestDataContext(selectedQuestName: Option[String],
                                  progress: Map[String, Int],
                                  bookmarks: List[String])


