package com.ryoserver.Home

import com.ryoserver.Inventory.Item.getItem
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.SQL
import org.bukkit.command.{Command, CommandExecutor, CommandSender}
import org.bukkit.entity.Player
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.{Bukkit, ChatColor, Location, Material}

import java.util

class Home(ryoServerAssist: RyoServerAssist) extends CommandExecutor with Listener {
  ryoServerAssist.getServer.getPluginManager.registerEvents(this,ryoServerAssist)

  override def onCommand(sender: CommandSender, command: Command, label: String, args: Array[String]): Boolean = {
    if (label.equalsIgnoreCase("home") && sender.isInstanceOf[Player]) {
      val p: Player = sender.asInstanceOf[Player]
      homeInventory(p)
      return true
    }
    false
  }

  def homeInventory(p:Player): Unit = {
    new BukkitRunnable {
      override def run(): Unit = {
        val uuid = p.getUniqueId.toString.replace("-","")
        val sql = new SQL(ryoServerAssist)
        if (!sql.connectionTest()) {
          p.sendMessage(ChatColor.RED + "現在ホーム機能が利用できません！")
          sql.close()
          return
        }
        val rs = sql.executeQuery(s"SHOW TABLES LIKE 'Homes';")
        if (!rs.next()) sql.executeSQL(s"CREATE TABLE `Homes`(UUID TEXT,point INT,Location TEXT,Locked BOOLEAN);")
        val inv = Bukkit.createInventory(null,27,"HomeSystem")
        inv.setItem(2,getItem(Material.WHITE_BED,"ホーム1を設定します。",util.Arrays.asList()))
        inv.setItem(4,getItem(Material.BLUE_BED,"ホーム2を設定します。",util.Arrays.asList()))
        inv.setItem(6,getItem(Material.BLACK_BED,"ホーム3を設定します。",util.Arrays.asList()))
        for (i <- 11 to 15 by 2) {
          val point = (i-9)/2
          val home_loc_rs = sql.executeQuery(s"SELECT Location FROM `Homes` WHERE point = $point AND UUID='$uuid'")
          var homeLoc = "現在ホームポイントが設定されていません。"
          if (home_loc_rs.next()) homeLoc = home_loc_rs.getString("Location")
          inv.setItem(i,getItem(Material.COMPASS,s"ホーム${point}にテレポートします。",util.Arrays.asList(homeLoc)))
        }
        for (i <- 20 to 24 by 2) {
          val point = (i-18)/2
          val rs = sql.executeQuery(s"SELECT Locked FROM `Homes` WHERE point = $point AND UUID = '$uuid'")
          var wool:Material = Material.LIGHT_BLUE_WOOL
          var msg:String = s"クリックでホーム${point}をロックします。"
          if (rs.next() && rs.getBoolean("Locked")) {
            wool = Material.RED_WOOL
            msg = s"クリックでホーム${point}のロックを解除します。"
          }
          inv.setItem(i,getItem(wool,msg,util.Arrays.asList("")))
        }
        sql.close()
        new BukkitRunnable {
          override def run(): Unit = p.openInventory(inv)
        }.runTask(ryoServerAssist)
      }
    }.runTaskAsynchronously(ryoServerAssist)
  }

  @EventHandler
  def onInventoryClick(e: InventoryClickEvent): Unit = {
    if (e.getView.getTitle != "HomeSystem") {
      return
    }
    e.setCancelled(true)
    val p = e.getWhoClicked.asInstanceOf[Player]
    val index = e.getSlot
    if (index == 2 || index == 4 || index == 6) setHome(p,index / 2)
    if (index == 11 || index == 13 || index == 15) teleportHome(p,(index - 9) / 2)
    if (index == 20 || index == 22 || index == 24) homeLock(p,(index-18)/2)
  }

  def setHome(p: Player,point: Int): Unit = {
    val uuid = p.getUniqueId.toString.replace("-","")
    val locate = p.getLocation()
    val location = s"${locate.getWorld.getName},${locate.getX.toInt},${locate.getY.toInt},${locate.getZ.toInt}"
    val sql = new SQL(ryoServerAssist)
    if (!sql.connectionTest()) {
      p.sendMessage(ChatColor.RED + "現在ホーム機能が利用できません！")
      sql.close()
      return
    }
    val LockCheck = sql.executeQuery(s"SELECT Locked FROM `Homes` WHERE point = $point AND UUID='$uuid'")
    if (LockCheck.next() && LockCheck.getBoolean("Locked")) {
      p.sendMessage(ChatColor.RED + "ホームがロックされています！")
      sql.close()
      return
    }
    val rs = sql.executeQuery(s"SELECT * FROM Homes WHERE UUID='$uuid' AND point=$point")
    if (!rs.next()) sql.executeSQL(s"INSERT INTO `Homes` (UUID,point,Location,Locked) VALUES ('$uuid',$point,'$location',false);")
    else sql.executeSQL(s"UPDATE `Homes` SET Location='$location' WHERE UUID='$uuid' AND point=$point")
    sql.close()
    p.sendMessage(ChatColor.AQUA + "ホームポイント" + point + "を設定しました。")
  }

  def teleportHome(p: Player,point: Int): Unit = {
    val sql = new SQL(ryoServerAssist)
    val uuid = p.getUniqueId.toString.replace("-","")
    if (!sql.connectionTest()) {
      p.sendMessage(ChatColor.RED + "現在ホーム機能を利用できません！")
      sql.close()
      return
    }
    val pointCheck = sql.executeQuery(s"SELECT point FROM `Homes` WHERE point=$point AND UUID='$uuid';")
    if (!pointCheck.next()) {
      p.sendMessage(ChatColor.RED + "ホームが登録されていません！")
      sql.close()
      return
    }

    val rs = sql.executeQuery(s"SELECT * FROM `Homes` WHERE point=$point AND UUID='$uuid'")
    val now_loc = p.getLocation
    while (rs.next()) {
      if (rs.getInt("point") == point) {
        val location = rs.getString("Location").split(",")
        if (ryoServerAssist.getConfig.getBoolean("log")) ryoServerAssist.getLogger.info(
          s"${p.getName}が[${now_loc.getWorld.getName},${now_loc.getX.toInt},${now_loc.getY.toInt},${now_loc.getZ.toInt}]から" +
            s"[${location(0)},${location(1)},${location(2)},${location(3)}]にテレポートしました！")
        p.teleport(new Location(Bukkit.getWorld(location(0)),location(1).toInt,location(2).toInt,location(3).toInt))
        p.sendMessage(ChatColor.AQUA + "ホーム" + point + "にテレポートしました！")
        sql.close()
        return
      }
    }
    sql.close()
  }

  def homeLock(p:Player, index:Int): Unit = {
    val uuid = p.getUniqueId.toString.replace("-","")
    val sql = new SQL(ryoServerAssist)
    if (!sql.connectionTest()) {
      p.sendMessage(ChatColor.RED + "現在ホーム機能が利用できません！")
      return
    }
    val rs = sql.executeQuery(s"SELECT * FROM `Homes` WHERE point=$index AND UUID='$uuid'")
    if (!rs.next()) {
      p.sendMessage(ChatColor.RED + "ステータスが変更できませんでした！")
      return
    }
    sql.executeSQL(s"UPDATE `Homes` SET Locked = !Locked WHERE point=$index AND UUID='$uuid'")
    sql.close()
    homeInventory(p)
  }

}
