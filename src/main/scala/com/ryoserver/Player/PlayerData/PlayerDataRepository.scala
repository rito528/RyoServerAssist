package com.ryoserver.Player.PlayerData
import com.ryoserver.Player.PlayerDataType
import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

import java.util.UUID

class PlayerDataRepository extends TPlayerDataRepository {

  private implicit val session: AutoSession.type = AutoSession

  override def store(): Unit = {

  }

  override def restore(uuid: UUID): Unit = {

  }

}
