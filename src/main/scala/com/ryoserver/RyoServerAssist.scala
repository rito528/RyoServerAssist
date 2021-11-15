package com.ryoserver

import com.ryoserver.Distribution.{Distribution, DistributionCommand}
import com.ryoserver.DustBox.DustBoxInventoryEvent
import com.ryoserver.Elevator.ElevatorEvent
import com.ryoserver.File.{CreateFiles, Patch}
import com.ryoserver.Gacha.{Gacha, GachaAddItemInventoryEvent, GachaCommand, GachaItemChangeGUI, GachaLoader}
import com.ryoserver.Home.Home
import com.ryoserver.Level.LevelCommand
import com.ryoserver.Menu.{MenuCommand, MenuEvent, MenuHandler}
import com.ryoserver.NeoStack._
import com.ryoserver.Notification.Notification
import com.ryoserver.OriginalItem.{AnvilRepairEvent, OriginalItemCommands, TotemEffect}
import com.ryoserver.Player.{FirstJoinSettingCommand, FirstJoinSettingEvent, LoadPlayerData, PlayerDataLoader, PlayerEvents}
import com.ryoserver.Profile.ProfileSettingCommands
import com.ryoserver.Quest.Event.{EventDeliveryMenu, EventGateway, EventLoader}
import com.ryoserver.Quest.{LoadQuests, QuestSelectMenuEvent, SuppressionEvent}
import com.ryoserver.Security.{Config, Operator, SecurityCommands, SecurityEvent}
import com.ryoserver.SkillSystems.Skill.DestructionSkill
import com.ryoserver.SkillSystems.SkillCommands
import com.ryoserver.SkillSystems.SkillPoint.RecoverySkillPointEvent
import com.ryoserver.Storage.StorageEvent
import com.ryoserver.Tips.Tips
import com.ryoserver.Title.{TitleCommands, TitleLoader}
import com.ryoserver.Vote.Vote
import com.ryoserver.World.Regeneration.{Regeneration, RegenerationCommand}
import com.ryoserver.World.SimpleRegion.RegionCommand
import com.ryoserver.tpa.TpaCommand
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
      "tpa" -> new TpaCommand(this),
      "skill" -> new SkillCommands,
      "sr" -> new RegionCommand,
      "hat" -> new SubCommands,
      "spawn" -> new SubCommands,
      "player" -> new FirstJoinSettingCommand(this),
      "title" -> new TitleCommands(this),
      "regeneration" -> new RegenerationCommand(this),
      "getoriginalitem" -> new OriginalItemCommands,
      "profile" -> new ProfileSettingCommands(this)
    ).foreach({ case (cmd, executor) =>
      getCommand(cmd).setExecutor(executor)
    })

    List(
      "playerStatus",
      "openInventory",
      "openEnderChest",
      "hide",
      "show",
      "freeze",
      "unfreeze",
      "security"
    ).foreach(cmd =>
      getCommand(cmd).setExecutor(new SecurityCommands(this))
    )

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
      new AnvilRepairEvent,
      new PickEvent(this),
      new GachaItemChangeGUI(this),
      new Vote(this),
      new SecurityEvent(this),
      new MenuHandler(this),
      new EventDeliveryMenu(this),
      //new DestructionSkill
    ).foreach(listener => this.getServer.getPluginManager.registerEvents(listener, this))

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
    Bukkit.getOnlinePlayers.forEach(p => new PlayerDataLoader(this).load(p))
    new TitleLoader().loadTitle()
    new TableCheck(this).stackTableCheck()
    ItemList.loadItemList(this)
    NeoStack.PlayerData.runnableSaver(this)
    new LoadNeoStackPage(this).loadStackPage()
    Operator.checkOp(this)
    new LoadPlayerData(this).autoLoad()
    new EventLoader().loadEvent()
    new EventGateway(this).autoSaveEvent()
    new EventGateway(this).loadEventData()
    new EventGateway(this).loadEventRanking()
    /*
      パッチの実行
    */
    new Patch(this).getAndExecutePatch()
    getLogger.info("RyoServerAssist enabled.")
  }

  override def onDisable(): Unit = {
    super.onDisable()
    new EventGateway(this).saveEvent()
    new EventGateway(this).saveRanking()
    Bukkit.getOnlinePlayers.forEach(p => new PlayerDataLoader(this).unload(p))
    NeoStack.PlayerData.save(this)
    getLogger.info("RyoServerAssist disabled.")
  }

}
