package com.ryoserver.Quest.Suppression

import java.util.UUID

case class SuppressionDataContext(uuid: UUID,isStarted: Boolean)

object SuppressionDataContext {

  def start(uuid: UUID): Unit = {
    SuppressionDataContext(uuid,isStarted = true)
  }

}
