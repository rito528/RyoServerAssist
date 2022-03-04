package com.ryoserver.RyoServerMenu

import com.google.common.io.ByteStreams
import com.ryoserver.Distribution.Distribution
import com.ryoserver.DustBox.DustBoxInventory
import com.ryoserver.Gacha.SubSystems.{GachaItemChangeGUI, GetGachaTickets}
import com.ryoserver.Home.HomeMenu
import com.ryoserver.Menu.Button.{Button, ButtonMotion}
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu._
import com.ryoserver.NeoStack.Menu.CategorySelectMenu
import com.ryoserver.Player.GetData
import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.Quest.Event.Menu.EventMenu
import com.ryoserver.Quest.Menu.{SelectDailyQuestMenu, SelectQuestMenu}
import com.ryoserver.Quest.QuestSortContext
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.SkillMenu.SkillCategoryMenu
import com.ryoserver.Storage.Storage
import com.ryoserver.Title.TitleMenu
import com.ryoserver.World.SimpleRegion.RegionMenu
import com.ryoserver.util.{Format, Item, ItemStackBuilder}
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.{Bukkit, Material, Sound}

class RyoServerMenu1(ryoServerAssist: RyoServerAssist) extends Menu {

  override val frame: MenuFrame = MenuFrame(6, "りょう鯖メニュー")

  override def openMotion(player: Player): Boolean = {
    super.openMotion(player)
    player.playSound(player.getLocation,Sound.BLOCK_IRON_TRAPDOOR_OPEN,1,1)
    true
  }

  private implicit val plugin: RyoServerAssist = ryoServerAssist

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    val computes = computeButton(player, ryoServerAssist, this)
    import computes._
    Map(
      getLayOut(1, 1) -> craftingButton,
      getLayOut(3, 1) -> regionMenu,
      getLayOut(5, 1) -> selectQuestMenu,
      getLayOut(6, 1) -> selectDailyQuestMenu,
      getLayOut(7, 1) -> selectSkillMenu,
      getLayOut(9, 1) -> selectTitleMenu,
      getLayOut(1, 3) -> openStorage,
      getLayOut(3, 3) -> openEnderChest,
      getLayOut(5, 3) -> openNeoStack,
      getLayOut(7, 3) -> openDustBox,
      getLayOut(9, 3) -> giveFirework,
      getLayOut(1, 5) -> getFromAdminGachaTickets,
      getLayOut(3, 5) -> getGachaTickets,
      getLayOut(5, 5) -> openGachaChangeMenu,
      getLayOut(7, 5) -> playerInformation,
      getLayOut(9, 5) -> openEventMenu,
      getLayOut(1, 6) -> teleportSpawn,
      getLayOut(2, 6) -> teleportWorldSpawn,
      getLayOut(3, 6) -> openHomeMenu,
      getLayOut(5, 6) -> teleportLobby,
      getLayOut(9, 6) -> nextPage
    )
  }

}

private case class computeButton(p: Player, ryoServerAssist: RyoServerAssist, ryoServerMenu1: RyoServerMenu1) {

  private implicit val plugin: RyoServerAssist = ryoServerAssist

  val craftingButton: Button = Button(
    ItemStackBuilder
      .getDefault(Material.CRAFTING_TABLE)
      .title(s"${GREEN}作業台を開きます。")
      .lore(List(s"${GRAY}クリックをすると開きます。"))
      .build(),
    ButtonMotion { _ =>
      p.openWorkbench(null, true)
    }
  )

  val regionMenu: Button = Button(
    ItemStackBuilder
      .getDefault(Material.WOODEN_AXE)
      .title(s"${GREEN}保護メニューを開きます。")
      .lore(List(s"${GRAY}クリックすると開きます。"))
      .build(),
    ButtonMotion { _ =>
      new RegionMenu().open(p)
    }
  )

  val selectQuestMenu: Button = Button(
    ItemStackBuilder
      .getDefault(Material.BOOK)
      .title(s"${GREEN}クエスト選択画面を開きます。")
      .lore(List(s"${GRAY}クリックで開きます。"))
      .build(),
    ButtonMotion { _ =>
      new SelectQuestMenu(ryoServerAssist,1,QuestSortContext.normal).open(p)
    }
  )

  val selectDailyQuestMenu: Button = Button(
    ItemStackBuilder
      .getDefault(Material.BOOK)
      .title("デイリークエスト選択画面を開きます。")
      .lore(List(s"${GRAY}クリックで開きます。"))
      .build(),
    ButtonMotion { _ =>
      new SelectDailyQuestMenu(ryoServerAssist,1).open(p)
    }
  )

  val selectSkillMenu: Button = Button(
    ItemStackBuilder
      .getDefault(Material.BEACON)
      .title(s"${GREEN}スキルを選択します。")
      .lore(List(s"${GRAY}クリックで開きます。"))
      .build(),
    ButtonMotion { _ =>
      new SkillCategoryMenu().open(p)
    }
  )

  val selectTitleMenu: Button = Button(
    ItemStackBuilder
      .getDefault(Material.NAME_TAG)
      .title(s"${GREEN}称号一覧を開きます。")
      .lore(List(s"${GRAY}クリックで開きます。"))
      .build(),
    ButtonMotion { _ =>
      new TitleMenu(1, ryoServerAssist).open(p)
    }
  )

  val openStorage: Button = Button(
    ItemStackBuilder
      .getDefault(Material.CHEST)
      .title(s"${GREEN}ストレージを開きます。")
      .lore(List(s"${GRAY}クリックで開きます。"))
      .build(),
    ButtonMotion { _ =>
      new Storage().load(p)
    }
  )

  val openEnderChest: Button = Button(
    ItemStackBuilder
      .getDefault(Material.ENDER_CHEST)
      .title(s"${GREEN}エンダーチェストを開きます。")
      .lore(List(s"${GRAY}クリックで開きます。"))
      .build(),
    ButtonMotion { _ =>
      p.openInventory(p.getEnderChest)
      p.playSound(p.getLocation, Sound.BLOCK_ENDER_CHEST_OPEN, 1, 1)
    }
  )

  val openNeoStack: Button = Button(
    ItemStackBuilder
      .getDefault(Material.SHULKER_BOX)
      .title(s"${GREEN}ネオスタックを開きます。")
      .lore(List(s"${GRAY}クリックで開きます。"))
      .build(),
    ButtonMotion { _ =>
      new CategorySelectMenu(ryoServerAssist).open(p)
    }
  )

  val openDustBox: Button = Button(
    ItemStackBuilder
      .getDefault(Material.LAVA_BUCKET)
      .title(s"${GREEN}ゴミ箱を開きます。")
      .lore(List(s"${GRAY}クリックで開きます。", s"$RED${BOLD}取扱注意！"))
      .build(),
    ButtonMotion { _ =>
      new DustBoxInventory().open(p)
    }
  )

  val giveFirework: Button = Button(
    ItemStackBuilder
      .getDefault(Material.FIREWORK_ROCKET)
      .title(s"${GREEN}ロケット花火を受け取ります。")
      .lore(List(s"${GRAY}クリックで受け取ります。"))
      .build(),
    ButtonMotion { _ =>
      p.getInventory.addItem(new ItemStack(Material.FIREWORK_ROCKET, 64))
      p.playSound(p.getLocation, Sound.UI_BUTTON_CLICK, 1, 1)
    }
  )

  val getFromAdminGachaTickets: Button = Button(
    ItemStackBuilder
      .getDefault(Material.CHEST_MINECART)
      .title(s"${GREEN}運営からのガチャ券を受け取ります。")
      .lore(List(
        s"${GRAY}クリックで受け取ります。",
        s"${GRAY}受け取れるガチャ券の枚数: ${new GetData().getFromAdminTickets(p)}枚"
      ))
      .build(),
    ButtonMotion { _ =>
      new Distribution().receipt(p)
      ryoServerMenu1.open(p)
    }
  )

  val getGachaTickets: Button = Button(
    ItemStackBuilder
      .getDefault(Material.PAPER)
      .title(s"${GREEN}ガチャ券を受け取ります。")
      .lore(List(
        s"${GRAY}クリックで受け取ります。",
        s"${GRAY}ガチャ券はEXPが100毎に1枚、または",
        s"${GRAY}レベルが10上がる毎に32枚手に入ります。",
        s"${GRAY}受け取れるガチャ券の枚数:" + p.getGachaTickets + "枚",
        s"${GRAY}次のガチャ券まであと" + String.format("%.1f", 100 - p.getQuestExp % 100)
      ))
      .setEffect()
      .build(),
    ButtonMotion { _ =>
      new GetGachaTickets().receipt(p)
      ryoServerMenu1.open(p)
    }
  )


  val openGachaChangeMenu: Button = Button(
    ItemStackBuilder
      .getDefault(Material.HONEY_BOTTLE)
      .title(s"${GREEN}ガチャ特等交換画面を開きます。")
      .lore(List(s"${GRAY}クリックで開きます。"))
      .build(),
    ButtonMotion { _ =>
      new GachaItemChangeGUI().open(p)
    }
  )

  val teleportWorldSpawn: Button = Button(
    ItemStackBuilder
      .getDefault(Material.ENDER_PEARL)
      .title(s"${GREEN}現在いるワールドのスポーン地点に移動します。")
      .lore(List(s"${GRAY}クリックで移動します。"))
      .build(),
    ButtonMotion { _ =>
      p.teleport(p.getWorld.getSpawnLocation)
      p.sendMessage(s"${AQUA}スポーン地点にテレポートしました！")
      p.playSound(p.getLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1)
    }
  )

  val teleportSpawn: Button = Button(
    ItemStackBuilder
      .getDefault(Material.COMPASS)
      .title(s"${GREEN}スポーン地点に移動します。")
      .lore(List(s"${GRAY}クリックで移動します。"))
      .build(),
    ButtonMotion { _ =>
      p.teleport(Bukkit.getWorld("world").getSpawnLocation)
      p.sendMessage(s"${AQUA}worldのスポーン地点にテレポートしました！")
      p.playSound(p.getLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1)
    }
  )

  val openHomeMenu: Button = Button(
    ItemStackBuilder
      .getDefault(Material.WHITE_BED)
      .title(s"${GREEN}ホームメニューを開きます。")
      .lore(List(s"${GRAY}クリックで開きます。"))
      .build(),
    ButtonMotion { _ =>
      new HomeMenu().open(p)
    }
  )

  val teleportLobby: Button = Button(
    ItemStackBuilder
      .getDefault(Material.OAK_DOOR)
      .title(s"${GREEN}ロビーに戻ります。")
      .lore(List(s"${GRAY}クリックでロビーに戻ります。"))
      .build(),
    ButtonMotion { _ =>
      p.playSound(p.getLocation, Sound.BLOCK_WOODEN_DOOR_OPEN, 1, 1)
      val out = ByteStreams.newDataOutput
      out.writeUTF("Connect")
      out.writeUTF("lobby")
      p.sendPluginMessage(ryoServerAssist, "BungeeCord", out.toByteArray)
    }
  )

  val playerInformation: Button = Button(
    Item.getPlayerSkull(p, s"${p.getName}の情報", List(
      s"${WHITE}レベル: Lv.${p.getQuestLevel}",
      s"${WHITE}EXP: ${Format.threeCommaFormat(p.getQuestExp)}",
      s"${WHITE}ランキング: ${p.getRanking}位",
      s"${WHITE}前の順位のプレイヤーとの差:${Format.threeCommaFormat(p.getBeforeExpDiff.getOrElse(0))}",
      s"${WHITE}後ろの順位のプレイヤーとの差:${Format.threeCommaFormat(p.getBehindExpDiff.getOrElse(0))}",
      s"${WHITE}クエストクリア回数: ${p.getQuestClearTimes}回",
      s"${WHITE}ガチャを引いた回数: ${p.getGachaPullNumber}回",
      s"${WHITE}ログイン日数: ${p.getLoginNumber}日",
      s"${WHITE}連続ログイン日数: ${p.getConsecutiveLoginDays}日",
      s"${WHITE}投票回数: ${p.getVoteNumber}回",
      s"${WHITE}連続投票日数: ${p.getReVoteNumber}日"
    ))
  )

  val openEventMenu: Button = Button(
    ItemStackBuilder
      .getDefault(Material.BOOK)
      .title(s"${GREEN}イベント")
      .lore(List(s"${GRAY}クリックで表示します。"))
      .build(),
    ButtonMotion { _ =>
      new EventMenu(ryoServerAssist).open(p)
    }
  )

  val nextPage: Button = Button(
    ItemStackBuilder
      .getDefault(Material.MAGENTA_GLAZED_TERRACOTTA)
      .title(s"${GREEN}次のページに移動します。")
      .lore(List(s"${GRAY}クリックで移動します。"))
      .build(),
    ButtonMotion { _ =>
      new RyoServerMenu2().open(p)
    }
  )
}
