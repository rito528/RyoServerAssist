package com.ryoserver

import com.ryoserver.Chat.JapaneseChat
import com.ryoserver.Gacha.Gacha
import com.ryoserver.Home.Home
import org.bukkit.plugin.java.JavaPlugin

class RyoServerAssist extends JavaPlugin {

  override def onEnable(): Unit = {
    super.onEnable()
    saveDefaultConfig()
    /*
      コマンドの有効化
     */
    Map(
      "home" -> new Home(this)
    ).foreach({case (cmd,executor) =>
      getCommand(cmd).setExecutor(executor)
    })
    /*
      イベントの有効化
     */
    new Home(this)
    new JapaneseChat(this)
    new Gacha(this)
    getLogger.info("RyoServerAssist enabled.")
  }

  override def onDisable(): Unit = {
    super.onDisable()
    getLogger.info("RyoServerAssist disabled.")
  }

}
