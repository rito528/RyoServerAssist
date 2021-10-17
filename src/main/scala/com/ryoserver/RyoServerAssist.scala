package com.ryoserver

import com.ryoserver.Chat.JapaneseChat
import com.ryoserver.Distribution.{Distribution, DistributionCommand}
import com.ryoserver.DustBox.DustBoxInventoryEvent
import com.ryoserver.Elevator.ElevatorEvent
import com.ryoserver.File.createFiles
import com.ryoserver.Gacha.{Gacha, GachaAddItemInventoryEvent, GachaCommand, GachaItemChangeGUI, GachaLoader}
import com.ryoserver.Home.Home
import com.ryoserver.Level.LevelCommand
import com.ryoserver.Menu.{MenuCommand, MenuEvent}
import com.ryoserver.Notification.Notification
import com.ryoserver.OriginalItem.{AnvilRepairEvent, OriginalItemCommands, totemEffect}
import com.ryoserver.Player.{FirstJoinSettingCommand, FirstJoinSettingEvent, PlayerEvents, playerDataLoader}
import com.ryoserver.Quest.Event.LoadEvent
import com.ryoserver.Quest.{QuestSelectInventoryEvent, loadQuests, suppressionEvent}
import com.ryoserver.World.SimpleRegion.{RegionCommand, RegionMenuEvent, RegionSettingMenuEvent}
import com.ryoserver.SkillSystems.Skill.SelectSkillEvent
import com.ryoserver.SkillSystems.SkillCommands
import com.ryoserver.SkillSystems.SkillPoint.RecoverySkillPointEvent
import com.ryoserver.NeoStack.{ItemList, PickEvent, StackGUI, StackGUIEvent, TableCheck}
import com.ryoserver.Storage.StorageEvent
import com.ryoserver.Tips.Tips
import com.ryoserver.Title.{TitleCommands, TitleInventoryEvent, TitleLoader}
import com.ryoserver.World.Regeneration.{Regeneration, RegenerationCommand}
import com.ryoserver.tpa.tpaCommand
import com.ryoserver.util.SQL
import org.bukkit.Bukkit
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
      "gacha" -> new GachaCommand(this),
      "distribution" -> new DistributionCommand(this),
      "menu" -> new MenuCommand(this),
      "stick" -> new MenuCommand(this),
      "level" -> new LevelCommand(this),
      "tpa" -> new tpaCommand(this),
      "skill" -> new SkillCommands,
      "sr" -> new RegionCommand,
      "hat" -> new SubCommands,
      "spawn" -> new SubCommands,
      "player" -> new FirstJoinSettingCommand(this),
      "title" -> new TitleCommands(this),
      "regeneration" -> new RegenerationCommand(this),
      "getoriginalitem" -> new OriginalItemCommands
    ).foreach({case (cmd,executor) =>
      getCommand(cmd).setExecutor(executor)
    })

    /*
      Bukkitイベントの有効化
     */
    List(
      new Home(this),
      new JapaneseChat(this),
      new Gacha(this),
      new PlayerEvents(this),
      new MenuEvent(this),
      new StorageEvent(this),
      new QuestSelectInventoryEvent(this),
      new suppressionEvent(this),
      new Notification,
      new RecoverySkillPointEvent(this),
      new SelectSkillEvent(this),
      new RegionMenuEvent(this),
      new RegionSettingMenuEvent,
      new GachaAddItemInventoryEvent(this),
      new DustBoxInventoryEvent,
      new FirstJoinSettingEvent(this),
      new ElevatorEvent,
      new totemEffect,
      new TitleInventoryEvent(this),
      new AnvilRepairEvent,
      new StackGUIEvent(this),
      new PickEvent(this),
      new GachaItemChangeGUI(this)
    ).foreach(listener => this.getServer.getPluginManager.registerEvents(listener,this))

    /*
      各種ロード処理
     */
    GachaLoader.load(this)
    new Distribution(this).createDistributionTable()
    loadQuests.createSetQuestFile()
    loadQuests.checkQuest()
    new Notification().createFile()
    new createFiles().createResourcesFile()
    new Tips(this).sendTips()
    new Regeneration(this).regeneration()
    Bukkit.getOnlinePlayers.forEach(p => new playerDataLoader(this).load(p))
    new TitleLoader().loadTitle()
    new TableCheck(this).stackTableCheck()
    ItemList.loadItemList(this)
    NeoStack.PlayerData.runnableSaver(this)
    new StackGUI(this).loadStackPage()
//    new LoadEvent(this).load()
    getLogger.info("RyoServerAssist enabled.")
  }

  override def onDisable(): Unit = {
    super.onDisable()
    Bukkit.getOnlinePlayers.forEach(p => new playerDataLoader(this).unload(p))
    NeoStack.PlayerData.save(this)
    getLogger.info("RyoServerAssist disabled.")
  }

}
