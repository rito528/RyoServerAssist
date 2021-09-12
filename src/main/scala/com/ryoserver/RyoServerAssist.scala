package com.ryoserver

import com.ryoserver.Home.Home
import org.bukkit.plugin.java.JavaPlugin

class RyoServerAssist extends JavaPlugin {

  override def onEnable(): Unit = {
    super.onEnable()
    saveDefaultConfig()
    //コマンドの登録
    Map(
      "home" -> new Home(this)
    ).foreach({case (cmd,executor) =>
      getCommand(cmd).setExecutor(executor)
    })
    getLogger.info("RyoServerAssist enabled.")
  }

  override def onDisable(): Unit = {
    super.onDisable()
    getLogger.info("RyoServerAssist disabled.")
  }

}
