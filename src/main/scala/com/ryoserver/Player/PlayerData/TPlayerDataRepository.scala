package com.ryoserver.Player.PlayerData

import com.ryoserver.Player.PlayerDataType

import java.util.UUID

trait TPlayerDataRepository {

  def store(): Unit

  def restore(uuid: UUID): Unit

  def findBy(uuid: UUID): Option[PlayerDataType]

  def updateData(uuid: UUID,playerDataType: PlayerDataType): Unit

}