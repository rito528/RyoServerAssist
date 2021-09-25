package com.ryoserver

import com.ryoserver.Chat.JapaneseChat
import com.ryoserver.Distribution.{Distribution, DistributionCommand}
import com.ryoserver.File.createFiles
import com.ryoserver.Gacha.{Gacha, GachaAddItemInventoryEvent, GachaCommand, GachaLoader}
import com.ryoserver.Home.Home
import com.ryoserver.Level.LevelCommand
import com.ryoserver.Menu.{MenuCommand, MenuEvent}
import com.ryoserver.Notification.Notification
import com.ryoserver.Player.{PlayerEvents, playerDataLoader}
import com.ryoserver.Quest.{QuestSelectInventoryEvent, loadQuests, suppressionEvent}
import com.ryoserver.SimpleRegion.{RegionMenuEvent, RegionSettingMenuEvent}
import com.ryoserver.SkillSystems.Skill.SelectSkillEvent
import com.ryoserver.SkillSystems.SkillCommands
import com.ryoserver.SkillSystems.SkillPoint.RecoverySkillPointEvent
import com.ryoserver.Storage.StorageEvent
import com.ryoserver.Tips.Tips
import com.ryoserver.tpa.tpaCommand
import com.ryoserver.util.SQL
import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.util.Location
import com.sk89q.worldguard.WorldGuard
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion
import org.bukkit.command.{Command, CommandSender}
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.{Bukkit, ChatColor}

import java.util.UUID

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
      "menu" -> new MenuCommand(this),
      "stick" -> new MenuCommand(this),
      "level" -> new LevelCommand(this),
      "tpa" -> new tpaCommand(this),
      "skill" -> new SkillCommands
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
      new RegionMenuEvent,
      new RegionSettingMenuEvent,
      new GachaAddItemInventoryEvent(this)
    ).foreach(listener => this.getServer.getPluginManager.registerEvents(listener,this))

    /*
      各種ロード処理
     */
    GachaLoader.load(this)
    new Distribution(this).createDistributionTable()
    loadQuests.createSetQuestFile()
    loadQuests.checkQuest(this)
    new Notification().createFile()
    new createFiles().createResourcesFile()
    new Tips(this).sendTips()

    Bukkit.getOnlinePlayers.forEach(p => new playerDataLoader(this).load(p))
    getLogger.info("RyoServerAssist enabled.")
  }

  override def onDisable(): Unit = {
    super.onDisable()
    Bukkit.getOnlinePlayers.forEach(p => new playerDataLoader(this).unload(p))
    getLogger.info("RyoServerAssist disabled.")
  }

  @Override
  override def onCommand(sender: CommandSender, command: Command, label: String, args: Array[String]): Boolean = {
    super.onCommand(sender, command, label, args)
    if (label.equalsIgnoreCase("test2")) {
      val container = WorldGuard.getInstance().getPlatform.getRegionContainer
      val regions = container.get(BukkitAdapter.adapt(Bukkit.getWorld("world")))
      val set = regions.getApplicableRegions(new Location(BukkitAdapter.adapt(Bukkit.getWorld("world")),-262,67,94).toVector.toBlockPoint)
      set.forEach(e => println("a:" + e.getId))
      val session = WorldEdit.getInstance().getSessionManager.get(BukkitAdapter.adapt(sender.asInstanceOf[Player]))
      val min = session.getSelection.getMinimumPoint.toVector3.withY(0)
      val max = session.getSelection().getMaximumPoint.toVector3.withY(256)
      val region = new ProtectedCuboidRegion("testRegion",min.toBlockPoint,max.toBlockPoint)
      val overlapping = region.getIntersectingRegions(regions.getRegions.values())
      if (overlapping.size() > 0) {
        sender.sendMessage(ChatColor.RED + "保護がかぶっています！")
      } else {
        val owners = region.getOwners
        owners.addPlayer(UUID.fromString(sender.asInstanceOf[Player].getUniqueId.toString))
        regions.addRegion(region)
        sender.sendMessage(ChatColor.AQUA + "保護が完了しました！")
      }
      return true
    }
    false
  }

}
