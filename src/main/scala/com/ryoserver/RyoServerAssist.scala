package com.ryoserver

import com.ryoserver.Commands._
import com.ryoserver.Config.ConfigData
import com.ryoserver.DataBase.{CreateTables, UpdateContinueVoteNumber}
import com.ryoserver.Distribution.{LoadDistribution, SaveDistribution}
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

  override def onEnable(): Unit = {
    super.onEnable()
    saveDefaultConfig()

    /*
      configのロード
      configにはMySQLの接続情報も含まれているため、最初にロードする必要がある
     */
    ConfigData.loadConfig(this)

    /*
      MySQL接続テスト
     */
    val sql = new SQL()
    if (!sql.connectionTest()) {
      getLogger.severe("MySQLに接続できませんでした！")
      getLogger.severe("サーバを終了します...")
      Bukkit.shutdown()
      return
    }
    sql.close()

    /*
      テーブルの作成
     */
    new CreateTables().execute()

    /*
     パッチの実行
     */
//    new Patch(this).getAndExecutePatch()

    /*
      連続投票日数を更新
     */
    new UpdateContinueVoteNumber().update()

    /*
      コマンドの登録
     */
    Map(
      "home" -> new HomeCommand(this),
      "gacha" -> new GachaCommand(),
      "distribution" -> new DistributionCommand(),
      "menu" -> new MenuCommand(this),
      "stick" -> new StickCommand,
      "level" -> new LevelCommand(this),
      "tpa" -> new TpaCommand(this),
      "sr" -> new RegionCommand,
      "hat" -> new HatCommand,
      "spawn" -> new SpawnCommand,
      "player" -> new PlayerCommand(),
      "title" -> new TitleCommand(this),
      "regeneration" -> new RegenerationCommand(this),
      "getoriginalitem" -> new OriginalItemCommand,
      //"profile" -> new ProfileSettingCommands(this),
      "ryoserverassist" -> new RyoServerAssistCommand(this),
      "security" -> new SecurityCommand(this),
      "skillPoint" -> new SkillPointCommand,
      "vote" -> new VoteCommand
    ).foreach({ case (cmd, executor) =>
      getCommand(cmd).setExecutor(executor)
    })

    /*
      Bukkitイベントの登録
     */
    List(
      new Home(this),
      new Gacha(this),
      new PlayerEvents(this),
      new MenuEvent(this),
      new StorageEvent(),
      new QuestSelectMenuEvent(this),
      new SuppressionEvent(this),
      new Notification,
      new RecoverySkillPointEvent,
      new GachaAddItemInventoryEvent(this),
      new DustBoxInventoryEvent,
      new FirstJoinSettingEvent(),
      new ElevatorEvent,
      new TotemEffect,
      new RepairEvent,
      new PickEvent(this),
      new GachaItemChangeGUI(this),
      new Vote(),
      new SecurityEvent(this),
      new MenuHandler(this),
      new EventDeliveryMenu(this),
      new PlayEffect(this),
      new EditEvent
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
    GachaLoader.load(this)
    LoadQuests.loadQuest(this)
    new TitleLoader().loadTitle()
    ItemList.loadItemList(this)
    new LoadNeoStackPage(this).loadStackPage()
    Operator.checkOp(this)
    new EventLoader().loadEvent()
    new EventGateway(this).loadEventData()
    new EventGateway(this).loadEventRanking()
    new EventGateway(this).loadBeforeEvents()
    new LoadDistribution().load()
    Translate.loadLangFile()


    /*
      オートセーブの実行
     */
    NeoStack.PlayerData.autoSave(this)
    new SavePlayerData(this).autoSave()
    new EventGateway(this).autoSaveEvent()
    new SaveDistribution(this).autoSave()
    PlayerQuestData.autoSave(this)

    /*
      ワールドの再生成を行う
     */
    new Regeneration(this).regeneration()

    /*
      サーバーに入っているプレイヤーにデータを適用する
     */
    Bukkit.getOnlinePlayers.forEach(p => new PlayerDataLoader(this).load(p))

    /*
      TipsSenderの起動
     */
    new Tips(this).sendTips()

    /*
     イベント開催確認用のタイマー
     */
    new EventGateway(this).autoCheckEvent()

    getLogger.info("RyoServerAssist enabled.")
  }

  override def onDisable(): Unit = {
    super.onDisable()
    new EventGateway(this).saveEvent()
    new EventGateway(this).saveRanking()
    Bukkit.getOnlinePlayers.forEach(p => new PlayerDataLoader(this).unload(p))
    NeoStack.PlayerData.save()
    new SavePlayerData(this).save()
    new SaveDistribution(this).save()
    PlayerQuestData.save()
    getLogger.info("RyoServerAssist disabled.")
  }

}
