package com.ryoserver.Config

import com.ryoserver.RyoServerAssist

class LoadConfig(ryoServerAssist: RyoServerAssist) {

  def load(): Unit = {
    ConfigData.configData = Map(
      "maxLv" -> ryoServerAssist.getConfig.getInt("maxLv")
    )
  }

}
