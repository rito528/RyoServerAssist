package com.ryoserver.Player.PlayerData

import java.util.UUID

trait TPlayerDataRepository {

  def store(uuid: UUID): Unit

  def restore(uuid: UUID): Unit

}
