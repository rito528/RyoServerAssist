package com.ryoserver.World.SimpleRegion

import com.ryoserver.Menu.Button.{Button, ButtonMotion}
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{Menu, MenuFrame}
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.{ItemStackBuilder, WorldGuardWrapper}
import com.sk89q.worldguard.protection.flags.{Flags, StateFlag}
import com.sk89q.worldguard.protection.regions.ProtectedRegion
import org.bukkit.ChatColor._
import org.bukkit._
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class RegionSettingMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  override val frame: MenuFrame = MenuFrame(6, "保護設定メニュー")

  override def openMotion(player: Player): Boolean = {
    super.openMotion(player)
    val worldGuard = new WorldGuardWrapper
    val loc = player.getLocation()
    if (!worldGuard.isProtected(loc) || !worldGuard.isOwner(player, loc)) {
      player.sendMessage(s"${RED}この場所はあなたの保護ではありません！")
      return false
    }
    true
  }

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    val compute = computeRegionSettingButton(player, ryoServerAssist, this)
    import compute._
    Map(
      getLayOut(2, 1) -> deleteRegion,
      getLayOut(4, 1) -> use,
      getLayOut(6, 1) -> interact,
      getLayOut(8, 1) -> chestAccess,
      getLayOut(2, 2) -> sleep,
      getLayOut(4, 2) -> vehiclePlace,
      getLayOut(6, 2) -> vehicleDestroy,
      getLayOut(8, 2) -> checkRegion,
      getLayOut(2, 4) -> blockPlace,
      getLayOut(4, 4) -> blockBreak
    )
  }

}

private case class computeRegionSettingButton(p: Player, ryoServerAssist: RyoServerAssist, regionSettingMenu: RegionSettingMenu) {
  private val worldGuard = new WorldGuardWrapper
  private val region = worldGuard.getRegion(p.getLocation()).head

  val deleteRegion: Button = Button(
    ItemStackBuilder
      .getDefault(Material.TNT)
      .title(s"$RED${BOLD}保護を削除します。")
      .lore(List(s"$RED${BOLD}取扱注意！", s"$RED${BOLD}保護範囲を削除します。"))
      .build(),
    ButtonMotion { _ =>
      worldGuard.removeRegion(p)
      p.sendMessage(s"${AQUA}保護:${region.getId}を削除しました。")
      p.playSound(p.getLocation, Sound.ITEM_BUCKET_FILL_LAVA, 1, 1)
      p.closeInventory()
    }
  )
  val use: Button = Button(
    ItemStackBuilder
      .getDefault(Material.OAK_DOOR)
      .title(s"${GREEN}フラグ:useを切り替えます。")
      .lore(List(
        s"${GRAY}ドアやボタンの使用を許可します。",
        s"${GRAY}状態:${if (getFlagStatus(region, Flags.USE)) s"${AQUA}許可" else s"${RED}拒否"}")
      )
      .build(),
    ButtonMotion { _ =>
      worldGuard.toggleFlag(region, Flags.USE, p)
      regionSettingMenu.open(p)
    }
  )
  val interact: Button = Button(
    ItemStackBuilder
      .getDefault(Material.OAK_BUTTON)
      .title(s"${GREEN}フラグ:interactを切り替えます。")
      .lore(List(
        s"${GRAY}スイッチの使用を許可します。",
        s"${GRAY}状態:${if (getFlagStatus(region, Flags.INTERACT)) s"${AQUA}許可" else s"${RED}拒否"}")
      )
      .build(),
    ButtonMotion { _ =>
      worldGuard.toggleFlag(region, Flags.INTERACT, p)
      regionSettingMenu.open(p)
    }
  )
  val chestAccess: Button = Button(
    ItemStackBuilder
      .getDefault(Material.CHEST)
      .title(s"${GREEN}フラグ:chest-accessを切り替えます。")
      .lore(List(
        s"${GRAY}チェストの使用を許可します。",
        s"${GRAY}状態:${if (getFlagStatus(region, Flags.CHEST_ACCESS)) s"${AQUA}許可" else s"${RED}拒否"}")
      )
      .build(),
    ButtonMotion { _ =>
      worldGuard.toggleFlag(region, Flags.CHEST_ACCESS, p)
      regionSettingMenu.open(p)
    }
  )
  val sleep: Button = Button(
    ItemStackBuilder
      .getDefault(Material.WHITE_BED)
      .title(s"${GREEN}フラグ:sleepを切り替えます。")
      .lore(List(
        s"${GRAY}ベットで眠る許可をします。",
        s"${GRAY}状態:${if (getFlagStatus(region, Flags.SLEEP)) s"${AQUA}許可" else s"${RED}拒否"}")
      )
      .build(),
    ButtonMotion { _ =>
      worldGuard.toggleFlag(region, Flags.SLEEP, p)
      regionSettingMenu.open(p)
    }
  )
  val vehiclePlace: Button = Button(
    ItemStackBuilder
      .getDefault(Material.MINECART)
      .title(s"${GREEN}フラグ:vehicle-placeを許可します。")
      .lore(List(
        s"${GRAY}トロッコ、ボードの設置を許可します。",
        s"${GRAY}状態:${if (getFlagStatus(region, Flags.PLACE_VEHICLE)) s"${AQUA}許可" else s"${RED}拒否"}")
      )
      .build(),
    ButtonMotion { _ =>
      worldGuard.toggleFlag(region, Flags.PLACE_VEHICLE, p)
      regionSettingMenu.open(p)
    }
  )
  val vehicleDestroy: Button = Button(
    ItemStackBuilder
      .getDefault(Material.OAK_BOAT)
      .title(s"${GREEN}フラグ:vehicle-destroyを許可します。")
      .lore(List(
        s"${GRAY}トロッコ、ボードの破壊を許可します。",
        s"${GRAY}状態:${if (getFlagStatus(region, Flags.DESTROY_VEHICLE)) s"${AQUA}許可" else s"${RED}拒否"}")
      )
      .build(),
    ButtonMotion { _ =>
      worldGuard.toggleFlag(region, Flags.DESTROY_VEHICLE, p)
      regionSettingMenu.open(p)
    }
  )
  val checkRegion: Button = Button(
    ItemStackBuilder
      .getDefault(Material.ENDER_EYE)
      .title(s"${GREEN}保護範囲の2点を確認します。")
      .lore(List(s"${GRAY}クリックでエフェクトを再生します。"))
      .build(),
    ButtonMotion { _ =>
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
  )
  val blockPlace: Button = Button(
    ItemStackBuilder
      .getDefault(Material.GRASS_BLOCK)
      .title(s"${GREEN}ブロックの設置を許可します。")
      .lore(List(
        s"${GRAY}ブロックの設置を許可します。",
        s"${GRAY}状態:${if (getFlagStatus(region, Flags.BLOCK_PLACE)) s"${AQUA}許可" else s"${RED}拒否"}",
        s"$RED$BOLD[取扱注意] このフラグを設定することはおすすめしません！")
      )
      .build(),
    ButtonMotion { _ =>
      worldGuard.toggleFlag(region, Flags.BLOCK_PLACE, p)
      regionSettingMenu.open(p)
    }
  )
  val blockBreak: Button = Button(
    ItemStackBuilder
      .getDefault(Material.STONE_PICKAXE)
      .title(s"${GREEN}ブロックの破壊を許可します。")
      .lore(List(
        s"${GRAY}ブロックの破壊を許可します。",
        s"${GRAY}状態:${if (getFlagStatus(region, Flags.BLOCK_BREAK)) s"${AQUA}許可" else s"${RED}拒否"}",
        s"$RED$BOLD[取扱注意] このフラグを設定することはおすすめしません！")
      )
      .build(),
    ButtonMotion { _ =>
      worldGuard.toggleFlag(region, Flags.BLOCK_BREAK, p)
      regionSettingMenu.open(p)
    }
  )

  private def getFlagStatus(set: ProtectedRegion, flag: StateFlag): Boolean = {
    set.getFlags.getOrDefault(flag, "DENY").toString == "ALLOW"
  }
}
