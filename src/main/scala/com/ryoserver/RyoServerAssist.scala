package com.ryoserver

import com.ryoserver.Commands._
import com.ryoserver.Distribution.{Distribution, LoadDistribution, SaveDistribution}
import com.ryoserver.DustBox.DustBoxInventoryEvent
import com.ryoserver.Elevator.ElevatorEvent
import com.ryoserver.File.{CreateFiles, Patch}
import com.ryoserver.Gacha.{Gacha, GachaAddItemInventoryEvent, GachaItemChangeGUI, GachaLoader}
import com.ryoserver.Home.Home
import com.ryoserver.Menu.{MenuEvent, MenuHandler}
import com.ryoserver.NeoStack._
import com.ryoserver.Notification.Notification
import com.ryoserver.OriginalItem.{PlayEffect, RepairEvent, TotemEffect}
import com.ryoserver.Player._
import com.ryoserver.Quest.Event.{EventDeliveryMenu, EventGateway, EventLoader}
import com.ryoserver.Quest._
import com.ryoserver.Security.{Config, Operator, SecurityEvent}
import com.ryoserver.SkillSystems.Skill.BreakSkill.BreakSkillAction
import com.ryoserver.SkillSystems.Skill.FarmSkill.{GrowSkillAction, HarvestSkillAction}
import com.ryoserver.SkillSystems.SkillPoint.RecoverySkillPointEvent
import com.ryoserver.Storage.StorageEvent
import com.ryoserver.Tips.Tips
import com.ryoserver.Title.TitleLoader
import com.ryoserver.Vote.Vote
import com.ryoserver.World.Regeneration.Regeneration
import com.ryoserver.World.SimpleRegion.RegionCommand
import com.ryoserver.util.{SQL, Translate}
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class RyoServerAssist extends JavaPlugin {

  override def onEnable(): Unit = {
    super.onEnable()
    saveDefaultConfig()
    /*
      MySQL connection test
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
      Load config
     */
    new LoadConfig(this).load()

    /*
      CreatePlayerTable
     */
    new CreateData(this).createPlayerTable()

    /*
      Enabling command
     */
    Map(
      "home" -> new HomeCommand(this),
      "gacha" -> new GachaCommand(this),
      "distribution" -> new DistributionCommand(this),
      "menu" -> new MenuCommand(this),
      "stick" -> new StickCommand,
      "level" -> new LevelCommand(this),
      "tpa" -> new TpaCommand(this),
      "sr" -> new RegionCommand,
      "hat" -> new HatCommand,
      "spawn" -> new SpawnCommand,
      "player" -> new PlayerCommand(this),
      "title" -> new TitleCommand(this),
      "regeneration" -> new RegenerationCommand(this),
      "getoriginalitem" -> new OriginalItemCommand,
      //"profile" -> new ProfileSettingCommands(this),
      "security" -> new SecurityCommand(this),
      "skillPoint" -> new SkillPointCommand
    ).foreach({ case (cmd, executor) =>
      getCommand(cmd).setExecutor(executor)
    })

    /*
      Enabling bukkit event
     */
    List(
      new Home(this),
      new Gacha(this),
      new PlayerEvents(this),
      new MenuEvent(this),
      new StorageEvent(this),
      new QuestSelectMenuEvent(this),
      new SuppressionEvent(this),
      new Notification,
      new RecoverySkillPointEvent,
      new GachaAddItemInventoryEvent(this),
      new DustBoxInventoryEvent,
      new FirstJoinSettingEvent(this),
      new ElevatorEvent,
      new TotemEffect,
      new RepairEvent,
      new PickEvent(this),
      new GachaItemChangeGUI(this),
      new Vote(this),
      new SecurityEvent(this),
      new MenuHandler(this),
      new EventDeliveryMenu(this),
      new PlayEffect(this)
    ).foreach(listener => this.getServer.getPluginManager.registerEvents(listener, this))

    /*
      Skill activation
     */
    BreakSkillAction.values.foreach(skill => {
      this.getServer.getPluginManager.registerEvents(skill, this)
    })
    GrowSkillAction.values.foreach(skill => {
      this.getServer.getPluginManager.registerEvents(skill, this)
    })
    HarvestSkillAction.values.foreach(skill => {
      this.getServer.getPluginManager.registerEvents(skill, this)
    })

    /*
      Other loads
     */
    Config.config = this.getConfig
    getServer.getMessenger.registerOutgoingPluginChannel(this, "BungeeCord")
    new LoadAllPlayerData(this).load()
    GachaLoader.load(this)
    new Distribution(this).createDistributionTable()
    LoadQuests.loadQuest(this)
    new Notification().createFile()
    new CreateFiles().createResourcesFile()
    new Tips(this).sendTips()
    new Regeneration(this).regeneration()
    new TitleLoader().loadTitle()
    new TableCheck(this).stackTableCheck()
    ItemList.loadItemList(this)
    NeoStack.PlayerData.autoSave(this)
    new LoadNeoStackPage(this).loadStackPage()
    Operator.checkOp(this)
    new SavePlayerData(this).autoSave()
    new EventLoader().loadEvent()
    new EventGateway(this).autoSaveEvent()
    new EventGateway(this).loadEventData()
    new EventGateway(this).loadEventRanking()
    new EventGateway(this).loadBeforeEvents()
    new SaveDistribution(this).autoSave()
    new LoadDistribution(this).load()
    Translate.loadLangFile()
    PlayerQuestData.autoSave(this)
    new DataBaseTable(this).createQuestTable()

    /*
     Execute patch
      */
    new Patch(this).getAndExecutePatch()

    getLogger.info("RyoServerAssist enabled.")

    Bukkit.getOnlinePlayers.forEach(p => new PlayerDataLoader(this).load(p))
  }

  override def onDisable(): Unit = {
    super.onDisable()
    new EventGateway(this).saveEvent()
    new EventGateway(this).saveRanking()
    Bukkit.getOnlinePlayers.forEach(p => new PlayerDataLoader(this).unload(p))
    NeoStack.PlayerData.save(this)
    new SavePlayerData(this).save()
    new SaveDistribution(this).save()
    PlayerQuestData.save(this)
    getLogger.info("RyoServerAssist disabled.")
  }

}
