package com.ryoserver

import com.ryoserver.AdminStorage.AdminStorageEvent
import com.ryoserver.Commands._
import com.ryoserver.Config.ConfigData
import com.ryoserver.DataBase.{CreateTables, UpdateContinueVoteNumber}
import com.ryoserver.Distribution.{LoadDistribution, SaveDistribution}
import com.ryoserver.DustBox.DustBoxInventoryEvent
import com.ryoserver.Elevator.ElevatorEvent
import com.ryoserver.ExpBottle.UseExpBottle
import com.ryoserver.File.CreateFiles
import com.ryoserver.Gacha.{Gacha, GachaItemChangeGUI, GachaLoader}
import com.ryoserver.Home.HomeData
import com.ryoserver.Menu.{MenuEvent, MenuHandler}
import com.ryoserver.NeoStack.Menu.SelectStackMenu
import com.ryoserver.NeoStack._
import com.ryoserver.Notification.Notification
import com.ryoserver.OriginalItem.{PlayEffect, RepairEvent, TotemEffect}
import com.ryoserver.Player._
import com.ryoserver.Quest.Event.{EventDeliveryMenu, EventGateway, EventLoader}
import com.ryoserver.Quest._
import com.ryoserver.Security.{Operator, SecurityEvent}
import com.ryoserver.SkillSystems.Skill.BreakSkill.BreakSkillAction
import com.ryoserver.SkillSystems.Skill.FarmSkill.{GrowSkillAction, HarvestSkillAction}
import com.ryoserver.SkillSystems.SkillPoint.RecoverySkillPointEvent
import com.ryoserver.Storage.StorageEvent
import com.ryoserver.Tips.Tips
import com.ryoserver.Title.TitleLoader
import com.ryoserver.Vote.Vote
import com.ryoserver.World.GuardMessage.EditEvent
import com.ryoserver.World.Regeneration.Regeneration
import com.ryoserver.World.SimpleRegion.RegionCommand
import com.ryoserver.util.{SQL, Translate}
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class RyoServerAssist extends JavaPlugin {

  private implicit val ryoServerAssist: RyoServerAssist = this

  override def onEnable(): Unit = {
    super.onEnable()
    saveDefaultConfig()

    /*
      configのロード
      configにはMySQLの接続情報も含まれているため、最初にロードする必要がある
     */
    ConfigData.loadConfig

    /*
      MySQL接続テスト
     */
    val sql = new SQL()
    if (!sql.connectionTest()) {
      getLogger.severe("MySQLに接続できませんでした！")
      getLogger.severe("サーバを終了します...")
      sql.close()
      Bukkit.shutdown()
      return
    }
    sql.close()

    /*
      テーブルの作成
     */
    new CreateTables().execute()

    /*
      連続投票日数を更新
     */
    new UpdateContinueVoteNumber().update()

    /*
      コマンドの登録
     */
    Map(
      "home" -> HomeCommand.executer,
      "gacha" -> GachaCommand.executer,
      "distribution" -> DistributionCommand.executer,
      "menu" -> MenuCommand.executer,
//      "stick" -> new StickCommand,
      "level" -> LevelCommand.executer,
//      "tpa" -> new TpaCommand,
//      "sr" -> new RegionCommand,
      "hat" -> HatCommand.executer,
//      "spawn" -> new SpawnCommand,
      "player" -> PlayerCommand.executor,
//      "title" -> new TitleCommand,
      "regeneration" -> RegenerationCommand.executer,
      "getoriginalitem" -> OriginalItemCommand.executer,
      //"profile" -> new ProfileSettingCommands(this),
      "ryoserverassist" -> RyoServerAssistCommand.executer,
      "security" -> SecurityCommand.executer,
      "skillPoint" -> SkillPointCommand.executer,
//      "vote" -> new VoteCommand,
      "getgachaitem" -> GetGachaItemCommand.executor,
      "adminStorage" -> AdminStorageCommand.executer
    ).foreach({ case (cmd, executor) =>
      getCommand(cmd).setExecutor(executor)
    })

    /*
      Bukkitイベントの登録
     */
    List(
      new Gacha,
      new PlayerEvents,
      new MenuEvent,
      new StorageEvent,
      new QuestSelectMenuEvent,
      new SuppressionEvent,
      new Notification,
      new RecoverySkillPointEvent,
      new DustBoxInventoryEvent,
      new FirstJoinSettingEvent,
      new ElevatorEvent,
      new TotemEffect,
      new RepairEvent,
      new PickEvent,
      new GachaItemChangeGUI,
      new Vote,
      new SecurityEvent,
      new MenuHandler,
      new EventDeliveryMenu,
      new PlayEffect,
      new EditEvent,
      new UseExpBottle,
      new AdminStorageEvent,
      new SelectStackMenu,
    ).foreach(listener => this.getServer.getPluginManager.registerEvents(listener, this))

    /*
      BungeeCordとの連携
     */
    getServer.getMessenger.registerOutgoingPluginChannel(this, "BungeeCord")

    /*
      特殊スキルの有効化
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
      必要なファイルを作成する
     */
    new CreateFiles().createResourcesFile()

    /*
      様々なロード処理
     */
    new LoadAllPlayerData().load()
    GachaLoader.load
    LoadQuests.loadQuest
    new TitleLoader().loadTitle()
    ItemList.loadItemList
    new LoadNeoStackPage().loadStackPage()
    Operator.checkOp
    new EventLoader().loadEvent()
    new EventGateway().loadEventData()
    new EventGateway().loadEventRanking()
    new EventGateway().loadBeforeEvents()
    new LoadDistribution().load()
    Translate.loadLangFile()
    HomeData.loadHomeData()


    /*
      オートセーブの実行
     */
    NeoStack.PlayerData.autoSave
    new SavePlayerData().autoSave()
    new EventGateway().autoSaveEvent()
    new SaveDistribution().autoSave()
    PlayerQuestData.autoSave
    HomeData.saveHomeData

    /*
      ワールドの再生成を行う
     */
    new Regeneration().regeneration

    /*
      サーバーに入っているプレイヤーにデータを適用する
     */
    Bukkit.getOnlinePlayers.forEach(p => new PlayerDataLoader().load(p))

    /*
      TipsSenderの起動
     */
    new Tips().sendTips()

    /*
     イベント開催確認用のタイマー
     */
    new EventGateway().autoCheckEvent()

    getLogger.info("RyoServerAssist enabled.")
  }

  override def onDisable(): Unit = {
    super.onDisable()
    new EventGateway().saveEvent()
    new EventGateway().saveRanking()
    Bukkit.getOnlinePlayers.forEach(p => new PlayerDataLoader().unload(p))
    NeoStack.PlayerData.save()
    new SavePlayerData().save()
    new SaveDistribution().save()
    PlayerQuestData.save()
    HomeData.save()
    getLogger.info("RyoServerAssist disabled.")
  }

}
