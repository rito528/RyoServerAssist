package com.ryoserver.Quest

object QuestSortTypeDependency {

  val dependency: Map[QuestSortContext, QuestSortContext] = Map(
    QuestSortContext.normal -> QuestSortContext.neoStack,
    QuestSortContext.neoStack -> QuestSortContext.bookMark,
    QuestSortContext.bookMark -> QuestSortContext.normal
  )

}
