package com.ryoserver

import com.ryoserver.AdminStorage.AdminStorageEvent
import com.ryoserver.Commands._
import com.ryoserver.Config.ConfigData
import com.ryoserver.DataBase.UpdateContinueVoteNumber
import com.ryoserver.Distribution.{LoadDistribution, SaveDistribution}
import com.ryoserver.Elevator.ElevatorEvent
import com.ryoserver.ExpBottle.UseExpBottle
import com.ryoserver.File.CreateFiles
import com.ryoserver.Gacha.GachaHandler
import com.ryoserver.Home.HomeData
import com.ryoserver.Maintenance.MaintenanceData
import com.ryoserver.Menu.MenuHandler
import com.ryoserver.NeoStack.NeoStackItem.NeoStackItemRepository
import com.ryoserver.NeoStack.NeoStackPage.NeoStackPageRepository
import com.ryoserver.NeoStack._
import com.ryoserver.Notification.Notification
import com.ryoserver.OriginalItem.{PlayEffect, RepairEvent, TotemEffect}
import com.ryoserver.Player.FirstJoin.FirstJoinSettingEvent
import com.ryoserver.Player.PlayerData.PlayerDataRepository
import com.ryoserver.Player._
import com.ryoserver.Quest.Event.{EventGateway, EventLoader}
import com.ryoserver.Quest.Suppression.{EventSuppression, NormalQuestSuppression}
import com.ryoserver.Quest._
import com.ryoserver.RyoServerMenu.StickEvent
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
import com.ryoserver.util.{ScalikeJDBC, Translate}
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
      マイグレーション用ファイルを作成する
     */
    new CreateFiles().createMigrationFiles()

    /*
      データベースをマイグレーションする
    */
    ScalikeJDBC.migrate()

    /*
     Scalikejdbcをセットアップ
     */
    ScalikeJDBC.setup()

    /*
      連続投票日数を更新
     */
    new UpdateContinueVoteNumber().update()

    /*
      コマンドの登録
     */
    Map(
      "home" -> HomeCommand.executer,
      "gacha" -> new GachaCommand().executer,
      "distribution" -> DistributionCommand.executer,
      "menu" -> new MenuCommand().executer,
      "stick" -> StickCommand.executer,
      "level" -> LevelCommand.executer,
      "tpa" -> new TpaCommand().executer,
      "sr" -> RegionCommand.executer,
      "hat" -> HatCommand.executer,
      "spawn" -> SpawnCommand.executer,
      "player" -> PlayerCommand.executor,
      "title" -> TitleCommand.executer,
      "regeneration" -> new RegenerationCommand().executer,
      "getoriginalitem" -> OriginalItemCommand.executer,
      "ryoserverassist" -> new RyoServerAssistCommand().executer,
      "security" -> new SecurityCommand().executer,
      "skillPoint" -> SkillPointCommand.executer,
      "vote" -> VoteCommand.executer,
      "getgachaitem" -> GetGachaItemCommand.executor,
      "adminStorage" -> AdminStorageCommand.executer,
      "maintenance" -> MaintenanceCommand.executor
    ).foreach({ case (cmd, executor) =>
      getCommand(cmd).setExecutor(executor)
    })

    /*
      Bukkitイベントの登録
     */
    List(
      new GachaHandler,
      new PlayerEvents,
      new StickEvent,
      new StorageEvent,
      new Notification,
      new RecoverySkillPointEvent,
      new FirstJoinSettingEvent,
      new ElevatorEvent,
      new TotemEffect,
      new RepairEvent,
      new PickEvent,
      new Vote,
      new SecurityEvent,
      new MenuHandler,
      new PlayEffect,
      new EditEvent,
      new UseExpBottle,
      new AdminStorageEvent,
      NormalQuestSuppression.executor,
      new EventSuppression().executor
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
      ワールドの再生成を行う
     */
    new Regeneration().regeneration

    /*
      様々なロード処理
     */
    new TitleLoader().loadTitle()
    new NeoStackPageRepository().restore()
    Operator.checkOp
    new EventLoader().loadEvent()
    new EventGateway().loadEventData()
    new EventGateway().loadEventRanking()
    new EventGateway().loadBeforeEvents()
    new LoadDistribution().load()
    Translate.loadLangFile()
    HomeData.loadHomeData()
    MaintenanceData.loadMaintenance()


    /*
      オートセーブの実行
     */
    new NeoStackService().autoStoreItem
    new PlayerService().autoSave
    new EventGateway().autoSaveEvent()
    new SaveDistribution().autoSave()
    val questPlayerData = new QuestPlayerData()
    questPlayerData.saver.autoSavePlayerQuestData
    questPlayerData.saver.autoSavePlayerDailyQuestData
    new PlayerService().autoSave

    HomeData.saveHomeData

    /*
      サーバーに入っているプレイヤーにデータを適用する
     */
    val playerLoader = new PlayerLoader()
    Bukkit.getOnlinePlayers.forEach(p => playerLoader.load(p))

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
    val playerLoader = new PlayerLoader()
    Bukkit.getOnlinePlayers.forEach(p => playerLoader.unload(p))
    new EventGateway().saveEvent()
    new EventGateway().saveRanking()
    new PlayerDataRepository().store()
    new NeoStackItemRepository().store()
    new SaveDistribution().save()
    val questPlayerData = new QuestPlayerData
    questPlayerData.saver.savePlayerQuestData()
    questPlayerData.saver.savePlayerDailyQuestData()
    HomeData.save()
    getLogger.info("RyoServerAssist disabled.")
  }

}
