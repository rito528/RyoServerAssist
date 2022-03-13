package com.ryoserver.Player.PlayerData

import java.util.UUID

trait TPlayerDataRepository {

  def store(): Unit

  def restore(uuid: UUID): Unit

}
