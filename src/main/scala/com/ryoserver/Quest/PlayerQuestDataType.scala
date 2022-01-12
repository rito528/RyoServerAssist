package com.ryoserver.Quest

case class PlayerQuestDataType(selectedQuestName: Option[String], progress: Map[String, Int], bookmarks: List[String])
