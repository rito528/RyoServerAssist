package com.ryoserver.Player.PlayerData

import com.ryoserver.Player.PlayerDataType

import java.util.UUID

trait TPlayerDataRepository {

  def store(): Unit

  /**
   * プレイヤーデータが存在しなかった場合にfalseを返します。
   */
  def restore(uuid: UUID): Boolean

  def findBy(uuid: UUID): Option[PlayerDataType]

  def updateData(uuid: UUID,playerDataType: PlayerDataType): Unit

}
