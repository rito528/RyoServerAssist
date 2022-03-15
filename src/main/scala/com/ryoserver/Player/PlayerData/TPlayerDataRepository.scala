package com.ryoserver.Player.PlayerData

import com.ryoserver.Player.PlayerDataType

import java.util.UUID

trait TPlayerDataRepository {

  def store(uuid: UUID): Unit

  def restore(uuid: UUID): Unit

  def findBy(uuid: UUID): Option[PlayerDataType]

}
