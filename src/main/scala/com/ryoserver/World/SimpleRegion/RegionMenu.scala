package com.ryoserver.World.SimpleRegion

import com.ryoserver.Menu.Menu
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.RyoServerAssist
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.math.Vector3
import com.sk89q.worldedit.{IncompleteRegionException, WorldEdit}
import com.sk89q.worldguard.WorldGuard
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.{Material, Sound}

import java.util.UUID

class RegionMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  val slot: Int = 1
  var name: String = "保護メニュー"
  var p: Player = _

  def menu(player: Player): Unit = {
    p = player
    setItem(3, 1, Material.WOODEN_AXE, effect = false, s"${GREEN}木の斧を取得します。", List(
      s"${GRAY}保護方法",
      s"${GRAY}木の斧で始点を左クリックします。",
      s"${GRAY}終点を対角で右クリックします。",
      s"${GRAY}その後、ダイヤの斧をクリックします。"))
    setItem(5, 1, Material.DIAMOND_AXE, effect = false, s"${GREEN}保護をします。", List(
      s"${GRAY}クリックで保護を開始します。",
      s"${GRAY}結果がチャットに表示されます。"
    ))
    setItem(7, 1, Material.GOLDEN_AXE, effect = false, s"${GREEN}保護編集メニューを開きます。", List(
      s"${GRAY}クリックで保護編集メニューを開きます。",
      s"${GRAY}自分が管理者の保護範囲内にいる必要があります。"
    ))
    registerMotion(Motion)
    open()
  }

  def Motion(p: Player, index: Int): Unit = {
    Map[Int, (Player) => Unit](
      getLayOut(3, 1) -> giveAxe,
      getLayOut(5, 1) -> createRegion,
      getLayOut(7, 1) -> new RegionSettingMenu(ryoServerAssist).openMenu _
    )(index)(p)
  }

  def giveAxe(p: Player): Unit = {
    p.getInventory.addItem(new ItemStack(Material.WOODEN_AXE, 1))
    p.sendMessage(s"${AQUA}保護用の木の斧を配布しました。")
  }

  def createRegion(p: Player): Unit = {
    val uuid = p.getUniqueId.toString
    if (!ryoServerAssist.getConfig.getStringList("protectionWorlds").contains(p.getWorld.getName.toLowerCase())) {
      p.sendMessage(s"${RED}このワールドでは保護できません！")
      return
    }
    val container = WorldGuard.getInstance().getPlatform.getRegionContainer
    val regions = container.get(BukkitAdapter.adapt(p.getWorld))
    val session = WorldEdit.getInstance().getSessionManager.get(BukkitAdapter.adapt(p))
    var min: Vector3 = null
    var max: Vector3 = null
    try {
      min = session.getSelection.getMinimumPoint.toVector3.withY(0)
      max = session.getSelection().getMaximumPoint.toVector3.withY(256)
    } catch {
      case e: IncompleteRegionException =>
        p.sendMessage(s"${RED}保護範囲が指定されていないため、保護できませんでした。")
        return
    }
    var counter = 1
    while (regions.getRegions.containsKey(p.getName.toLowerCase() + "_" + counter)) {
      counter += 1
    }
    val region = new ProtectedCuboidRegion(p.getName.toLowerCase() + "_" + counter, min.toBlockPoint, max.toBlockPoint)
    val overlapping = region.getIntersectingRegions(regions.getRegions.values())
    if (overlapping.size() > 0) {
      p.sendMessage(s"${RED}他の保護範囲と重なっています！")
    } else {
      val owners = region.getOwners
      owners.addPlayer(UUID.fromString(uuid))
      regions.addRegion(region)
      p.sendMessage(s"${AQUA}保護が完了しました！")
      p.playSound(p.getLocation, Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1)
    }
  }

}
