package com.ryoserver.Menu

import com.google.common.io.ByteStreams
import com.ryoserver.Distribution.Distribution
import com.ryoserver.DustBox.DustBoxInventory
import com.ryoserver.Gacha.{GachaItemChangeGUI, getGachaTickets}
import com.ryoserver.Home.Home
import com.ryoserver.Level.Player.getPlayerData
import com.ryoserver.NeoStack.NeoStackGUI
import com.ryoserver.Quest.QuestSelectInventory
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.Skill.SelectSkillMenu
import com.ryoserver.Storage.Storage
import com.ryoserver.Title.TitleInventory
import com.ryoserver.World.SimpleRegion.RegionMenu
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.inventory.ItemStack
import org.bukkit.{Bukkit, ChatColor, Material}

class MenuEvent(ryoServerAssist: RyoServerAssist) extends Listener {

  @EventHandler
  def onClick(e: InventoryClickEvent): Unit = {
    if (!e.getView.getTitle.equalsIgnoreCase("りょう鯖メニュー")) return
    e.setCancelled(true)
    if (e.getClickedInventory != e.getView.getTopInventory) return
    val p = e.getWhoClicked.asInstanceOf[Player]
    val data = new getPlayerData(ryoServerAssist)
    e.getSlot match {
      case 0 => p.openWorkbench(null,true)
      case 2 => new RegionMenu().menu(p)
      case 4 => new QuestSelectInventory(ryoServerAssist).selectInventory(p)
      case 6 => new SelectSkillMenu(ryoServerAssist).openMenu(p)
      case 8 => new TitleInventory(ryoServerAssist).openInv(p,1)
      case 18 =>
        if (data.getPlayerLevel(p) >= 10) {
          new Storage(ryoServerAssist).load(p)
        } else {
          p.sendMessage(ChatColor.RED + "ストレージ機能はLv.10以上になると使うことができます。")
        }
      case 20 => p.openInventory(p.getEnderChest)
      case 22 =>
        if (data.getPlayerLevel(p) >= 20) {
          new NeoStackGUI(ryoServerAssist).openCategorySelectGUI(p)
        } else {
          p.sendMessage(ChatColor.RED + "ネオスタック機能はLv.20以上になると使うことができます。")
        }
      case 24 => new DustBoxInventory().openDustBox(p)
      case 26 => p.getInventory.addItem(new ItemStack(Material.FIREWORK_ROCKET,64))
      case 36 => new Distribution(ryoServerAssist).receipt(p)
      case 38 => new getGachaTickets(ryoServerAssist).receipt(p)
      case 40 => new GachaItemChangeGUI(ryoServerAssist).openChangeGUI(p)
      case 45 =>
        p.teleport(p.getWorld.getSpawnLocation)
        p.sendMessage(ChatColor.AQUA + "スポーン地点にテレポートしました！")
      case 46 =>
        p.teleport(Bukkit.getWorld("world").getSpawnLocation)
        p.sendMessage(ChatColor.AQUA + "worldのスポーン地点にテレポートしました！")
      case 47 =>
        new Home(ryoServerAssist).homeInventory(p)
      case 49 =>
        val out = ByteStreams.newDataOutput
        out.writeUTF("Connect")
        out.writeUTF("lobby")
        p.sendPluginMessage(ryoServerAssist,"BungeeCord",out.toByteArray)
      case 51 =>
        p.sendMessage(ChatColor.UNDERLINE + ryoServerAssist.getConfig.getString("webSite"))
      case 52 =>
        p.sendMessage(ChatColor.UNDERLINE + ryoServerAssist.getConfig.getString("dynmap"))
      case 53 =>
        p.sendMessage(ChatColor.UNDERLINE + ryoServerAssist.getConfig.getStringList("voteSite").toArray()(0).toString)
        p.sendMessage(ChatColor.UNDERLINE + ryoServerAssist.getConfig.getStringList("voteSite").toArray()(1).toString)
      case _ =>
    }
  }

  @EventHandler
  def stickClick(e: PlayerInteractEvent): Unit = {
    if ((e.getAction == Action.RIGHT_CLICK_BLOCK || e.getAction == Action.RIGHT_CLICK_AIR) &&
      e.getPlayer.getInventory.getItemInMainHand.getType == Material.STICK) {
      e.getPlayer.openInventory(createMenu.menu(e.getPlayer,ryoServerAssist))
    }
  }

}
