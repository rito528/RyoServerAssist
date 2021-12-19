package com.ryoserver

import com.ryoserver.Commands._
import com.ryoserver.Distribution.Distribution
import com.ryoserver.DustBox.DustBoxInventoryEvent
import com.ryoserver.Elevator.ElevatorEvent
import com.ryoserver.File.{CreateFiles, Patch}
import com.ryoserver.Gacha.{Gacha, GachaAddItemInventoryEvent, GachaItemChangeGUI, GachaLoader}
import com.ryoserver.Home.Home
import com.ryoserver.Menu.{MenuEvent, MenuHandler}
import com.ryoserver.NeoStack._
import com.ryoserver.Notification.Notification
import com.ryoserver.OriginalItem.{RepairEvent, TotemEffect}
import com.ryoserver.Player.{FirstJoinSettingEvent, LoadPlayerData, PlayerDataLoader, PlayerEvents, SavePlayerData}
import com.ryoserver.Profile.ProfileSettingCommands
import com.ryoserver.Quest.Event.{EventDeliveryMenu, EventGateway, EventLoader}
import com.ryoserver.Quest.{LoadQuests, QuestSelectMenuEvent, SuppressionEvent}
import com.ryoserver.Security.{Config, Operator, SecurityEvent}
import com.ryoserver.SkillSystems.Skill.BreakSkill.BreakSkillAction
import com.ryoserver.SkillSystems.Skill.FarmSkill
import com.ryoserver.SkillSystems.Skill.FarmSkill.{GrowSkillAction, HarvestSkillAction}
import com.ryoserver.SkillSystems.SkillPoint.RecoverySkillPointEvent
import com.ryoserver.Storage.StorageEvent
import com.ryoserver.Tips.Tips
import com.ryoserver.Title.TitleLoader
import com.ryoserver.Vote.Vote
import com.ryoserver.World.Regeneration.Regeneration
import com.ryoserver.World.SimpleRegion.RegionCommand
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
      "security" -> new SecurityCommand(this)
    ).foreach({ case (cmd, executor) =>
      getCommand(cmd).setExecutor(executor)
    })

    /*
      Bukkitイベントの有効化
     */
    List(
      new Home(this),
      new Gacha(this),
      new PlayerEvents(this),
      new MenuEvent(this),
      new StorageEvent(this),
      new QuestSelectMenuEvent(),
      new SuppressionEvent(this),
      new Notification,
      new RecoverySkillPointEvent(this),
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
      new EventDeliveryMenu(this)
    ).foreach(listener => this.getServer.getPluginManager.registerEvents(listener, this))

    /*
      スキルの有効化
     */
    BreakSkillAction.values.foreach(skill => {
      this.getServer.getPluginManager.registerEvents(skill,this)
    })
    GrowSkillAction.values.foreach(skill => {
      this.getServer.getPluginManager.registerEvents(skill,this)
    })
    HarvestSkillAction.values.foreach(skill => {
      this.getServer.getPluginManager.registerEvents(skill,this)
    })
    /*
      各種ロード処理
     */
    Config.config = this.getConfig
    getServer.getMessenger.registerOutgoingPluginChannel(this, "BungeeCord")
    GachaLoader.load(this)
    new Distribution(this).createDistributionTable()
    LoadQuests.checkQuest(this)
    new Notification().createFile()
    new CreateFiles().createResourcesFile()
    new Tips(this).sendTips()
    new Regeneration(this).regeneration()
    new TitleLoader().loadTitle()
    new TableCheck(this).stackTableCheck()
    ItemList.loadItemList(this)
    NeoStack.PlayerData.runnableSaver(this)
    new LoadNeoStackPage(this).loadStackPage()
    Operator.checkOp(this)
    new SavePlayerData(this).autoSave()
    new EventLoader().loadEvent()
    new EventGateway(this).autoSaveEvent()
    new EventGateway(this).loadEventData()
    new EventGateway(this).loadEventRanking()

    /*
     パッチの実行
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
    getLogger.info("RyoServerAssist disabled.")
  }

}
