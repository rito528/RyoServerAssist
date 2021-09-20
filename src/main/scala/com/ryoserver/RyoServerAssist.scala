package com.ryoserver

import com.ryoserver.Chat.JapaneseChat
import com.ryoserver.Distribution.{Distribution, DistributionCommand}
import com.ryoserver.Gacha.{Gacha, GachaCommand, GachaLoader}
import com.ryoserver.Home.Home
import com.ryoserver.Level.LevelCommand
import com.ryoserver.Menu.{MenuCommand, MenuEvent}
import com.ryoserver.Player.JoinEvents
import com.ryoserver.Quest.{LotteryQuest, QuestSelectInventoryEvent, loadQuests}
import com.ryoserver.Storage.StorageEvent
import com.ryoserver.util.SQL
import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.plugin.java.JavaPlugin

class RyoServerAssist extends JavaPlugin {

  override def onEnable(): Unit = {
    super.onEnable()
    saveDefaultConfig()
    /*
      MySQL接続チェック
     */
    val sql = new SQL(this)
    if (!sql.connectionTest()) {
      getLogger.severe("MySQLに接続できませんでした！")
      getLogger.severe("サーバを終了します...")
      Bukkit.shutdown()
      return
    }
    sql.close()
    /*
      コマンドの有効化
     */
    Map(
      "home" -> new Home(this),
      "gacha" -> new GachaCommand(),
      "distribution" -> new DistributionCommand(this),
      "menu" -> new MenuCommand(),
      "level" -> new LevelCommand(this)
    ).foreach({case (cmd,executor) =>
      getCommand(cmd).setExecutor(executor)
    })

    /*
      イベントの有効化
     */
    List(
      new Home(this),
      new JapaneseChat(this),
      new Gacha(this),
      new JoinEvents(this),
      new MenuEvent(this),
      new StorageEvent(this),
      new QuestSelectInventoryEvent(this)
    ).foreach(listener => this.getServer.getPluginManager.registerEvents(listener,this))

    /*
      各種ロード処理
     */
    GachaLoader.load(this)
    new Distribution(this).createDistributionTable()
    loadQuests.createSetQuestFile()
    loadQuests.checkQuest(this)
    getLogger.info("RyoServerAssist enabled.")
  }

  override def onDisable(): Unit = {
    super.onDisable()
    getLogger.info("RyoServerAssist disabled.")
  }

}
