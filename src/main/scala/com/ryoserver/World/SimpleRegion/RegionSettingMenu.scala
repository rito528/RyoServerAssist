package com.ryoserver.World.SimpleRegion

import com.ryoserver.Menu.Menu
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.WorldGuardWrapper
import com.sk89q.worldguard.protection.flags.{Flags, StateFlag}
import com.sk89q.worldguard.protection.regions.ProtectedRegion
import org.bukkit.ChatColor._
import org.bukkit._
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class RegionSettingMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  val slot: Int = 6
  var name: String = "保護設定メニュー"
  var p: Player = _

  def openMenu(player: Player): Unit = {
    p = player
    val worldGuard = new WorldGuardWrapper
    val loc = p.getLocation()
    if (!worldGuard.isProtected(loc) || !worldGuard.isOwner(p, loc)) {
      p.sendMessage(ChatColor.RED + "この場所はあなたの保護ではありません！")
      return
    }
    val set = worldGuard.getRegion(loc).head
    setItem(2, 1, Material.TNT, effect = false, s"$RED${BOLD}保護を削除します。", List(s"$RED${BOLD}取扱注意！", s"$RED${BOLD}保護範囲を削除します。"))
    setItem(4, 1, Material.OAK_DOOR, effect = false, s"${GREEN}フラグ:useを切り替えます。", List(s"${GRAY}ドアやボタンの使用を許可します。",
      s"${GRAY}状態:${if (getFlagStatus(set, Flags.USE)) s"${AQUA}許可" else s"${RED}拒否"}"))
    setItem(6, 1, Material.OAK_BUTTON, effect = false, s"${GREEN}フラグ:interactを切り替えます。",
      List(s"${GRAY}スイッチの使用を許可します。",
        s"${GRAY}状態:${if (getFlagStatus(set, Flags.INTERACT)) s"${AQUA}許可" else s"${RED}拒否"}"))
    setItem(8, 1, Material.CHEST, effect = false, s"${GREEN}フラグ:chest-accessを切り替えます。",
      List(s"${GRAY}チェストの使用を許可します。",
        s"${GRAY}状態:${if (getFlagStatus(set, Flags.CHEST_ACCESS)) s"${AQUA}許可" else s"${RED}拒否"}"))
    setItem(2, 2, Material.WHITE_BED, effect = false, s"${GREEN}フラグ:sleepを許可します。",
      List(s"${GRAY}ベットで眠る許可をします。",
        s"${GRAY}状態:${if (getFlagStatus(set, Flags.SLEEP)) s"${AQUA}許可" else s"${RED}拒否"}"))
    setItem(4, 2, Material.MINECART, effect = false, s"${GREEN}フラグ:vehicle-placeを許可します。",
      List(s"${GRAY}トロッコ、ボードの設置を許可します。",
        s"${GRAY}状態:${if (getFlagStatus(set, Flags.PLACE_VEHICLE)) s"${AQUA}許可" else s"${RED}拒否"}"))
    setItem(6, 2, Material.OAK_BOAT, effect = false, s"${GREEN}フラグ:vehicle-destroyを許可します。",
      List(s"${GRAY}トロッコ、ボードの破壊を許可します。",
        s"${GRAY}状態:${if (getFlagStatus(set, Flags.DESTROY_VEHICLE)) s"${AQUA}許可" else s"${RED}拒否"}"))
    setItem(8, 2, Material.ENDER_EYE, effect = false, s"${GREEN}保護範囲の2点を確認します。", List(s"${GRAY}クリックでエフェクトを再生します。"))
    registerMotion(motion)
    open()
  }

  def deleteRegion(p: Player): Unit = {
    val worldGuard = new WorldGuardWrapper
    val region = worldGuard.getRegion(p.getLocation()).head
    worldGuard.removeRegion(p)
    p.sendMessage(AQUA + "保護:" + region.getId + "を削除しました。")
    p.playSound(p.getLocation, Sound.ITEM_BUCKET_FILL_LAVA, 1, 1)
  }

  def checkRegion(p: Player): Unit = {
    val worldGuard = new WorldGuardWrapper
    val region = worldGuard.getRegion(p.getLocation).head
    val min = region.getMinimumPoint
    val max = region.getMaximumPoint
    var counter = 0
    new BukkitRunnable {
      override def run(): Unit = {
        counter += 1
        if (counter > 30) this.cancel()
        for (y <- 0 to 256) {
          p.getWorld.playEffect(new Location(p.getWorld, min.getX, y, min.getZ), Effect.SMOKE, 0, 1000)
        }
        for (y <- 0 to 256) {
          p.getWorld.playEffect(new Location(p.getWorld, max.getX, y, max.getZ), Effect.SMOKE, 0, 1000)
        }
      }
    }.runTaskTimerAsynchronously(ryoServerAssist, 10, 10)
  }

  def motion(p: Player, index: Int): Unit = {
    val worldGuard = new WorldGuardWrapper
    val region = worldGuard.getRegion(p.getLocation()).head
    val motions = Map[Int, Player => Unit](
      getLayOut(2, 1) -> deleteRegion,
      getLayOut(4, 1) -> {
        worldGuard.toggleFlag(region, Flags.USE, _)
      },
      getLayOut(6, 1) -> {
        worldGuard.toggleFlag(region, Flags.INTERACT, _)
      },
      getLayOut(8, 1) -> {
        worldGuard.toggleFlag(region, Flags.CHEST_ACCESS, _)
      },
      getLayOut(2, 2) -> {
        worldGuard.toggleFlag(region, Flags.SLEEP, _)
      },
      getLayOut(4, 2) -> {
        worldGuard.toggleFlag(region, Flags.PLACE_VEHICLE, _)
      },
      getLayOut(6, 2) -> {
        worldGuard.toggleFlag(region, Flags.DESTROY_VEHICLE, _)
      },
      getLayOut(8, 2) -> checkRegion
    )
    if (motions.contains(index)) {
      motions(index)(p)
      openMenu(p)
    }
  }

  def getFlagStatus(set: ProtectedRegion, flag: StateFlag): Boolean = {
    set.getFlags.getOrDefault(flag, "DENY").toString == "ALLOW"
  }

}
