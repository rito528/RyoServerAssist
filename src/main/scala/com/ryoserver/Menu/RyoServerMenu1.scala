package com.ryoserver.Menu

import com.ryoserver.Distribution.Distribution
import com.ryoserver.DustBox.DustBoxInventory
import com.ryoserver.Gacha.{GachaItemChangeGUI, GetGachaTickets}
import com.ryoserver.Home.Home
import com.ryoserver.NeoStack.Menu.CategorySelectMenu
import com.ryoserver.Player.GetData
import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.Quest.Event.EventMenu
import com.ryoserver.Quest.QuestMenu
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.SkillMenu.SkillCategoryMenu
import com.ryoserver.Storage.Storage
import com.ryoserver.Title.TitleMenu
import com.ryoserver.World.SimpleRegion.RegionMenu
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.{Material, Sound}

class RyoServerMenu1(ryoServerAssist: RyoServerAssist) extends Menu {

  val slot = 6
  var name = "りょう鯖メニュー"
  var p: Player = _

  def menu(player: Player): Unit = {
    p = player
    val motion = new MenuMotion(ryoServerAssist)
    setButton(MenuButton(1,1,Material.CRAFTING_TABLE,s"${GREEN}作業台を開きます。",List(s"${GRAY}クリックで開きます。"))
    .setLeftClickMotion(motion.openWorkBench))
    setButton(MenuButton(3, 1, Material.WOODEN_AXE, s"${GREEN}保護メニューを開きます。", List(s"${GRAY}クリックで開きます。"))
    .setLeftClickMotion(new RegionMenu(ryoServerAssist).menu _))
    setButton(MenuButton(5, 1, Material.BOOK, s"${GREEN}クエストを選択します。", List(s"${GRAY}クリックで開きます。"))
    .setLeftClickMotion(new QuestMenu(ryoServerAssist).selectInventory _))
    setButton(MenuButton(6, 1, Material.BOOK, s"${GREEN}デイリークエストを選択します。",List(s"${GRAY}クリックで開きます。"))
    .setLeftClickMotion(new QuestMenu(ryoServerAssist).selectDailyQuestMenu _))
    setButton(MenuButton(7, 1, Material.BEACON, s"${GREEN}スキルを選択します。", List(s"${GRAY}クリックで開きます。"))
    .setLeftClickMotion(new SkillCategoryMenu(ryoServerAssist).openSkillCategoryMenu _))
    setButton(MenuButton(9, 1, Material.NAME_TAG, s"${GREEN}称号一覧を開きます。", List(s"${GRAY}クリックで開きます。"))
    .setLeftClickMotion(new TitleMenu(ryoServerAssist).openInv(_, 1)))
    setButton(MenuButton(1, 3, Material.CHEST, s"${GREEN}ストレージを開きます。", List(s"${GRAY}クリックで開きます。"))
    .setLeftClickMotion(new Storage().load _))
    setButton(MenuButton(3, 3, Material.ENDER_CHEST, s"${GREEN}エンダーチェストを開きます。", List(s"${GRAY}クリックで開きます。"))
    .setLeftClickMotion(motion.openEnderChest))
    setButton(MenuButton(5, 3, Material.SHULKER_BOX, s"${GREEN}ネオスタックを開きます。", List(s"${GRAY}クリックで開きます"))
    .setLeftClickMotion(new CategorySelectMenu(ryoServerAssist).openCategorySelectMenu _))
    setButton(MenuButton(7, 3, Material.LAVA_BUCKET, s"${GREEN}ゴミ箱を開きます。", List(s"${GRAY}クリックで開きます。", s"$RED${BOLD}取扱注意！"))
    .setLeftClickMotion(new DustBoxInventory().openDustBox _))
    setButton(MenuButton(9, 3, Material.FIREWORK_ROCKET, s"${GREEN}ロケット花火を受け取ります。", List(s"${GRAY}クリックで受け取ります。"))
    .setLeftClickMotion(motion.giveFirework))
    setButton(MenuButton(1, 5, Material.CHEST_MINECART, s"${GREEN}運営からのガチャ券を受け取ります。", List(
      s"${GRAY}クリックで受け取ります。",
      s"${GRAY}受け取れるガチャ券の枚数: ${new GetData().getFromAdminTickets(p)}枚"
    )).setLeftClickMotion(new Distribution().receipt _)
    .setReload())
    setButton(MenuButton(3, 5, Material.PAPER, s"${GREEN}ガチャ券を受け取ります。", List(
      s"${GRAY}クリックで受け取ります。",
      s"${GRAY}ガチャ券はEXPが100毎に1枚、または",
      s"${GRAY}レベルが10上がる毎に32枚手に入ります。",
      s"${GRAY}受け取れるガチャ券の枚数:" + p.getGachaTickets + "枚",
      s"${GRAY}次のガチャ券まであと" + String.format("%.1f", 100 - p.getQuestExp % 100)))
    .setLeftClickMotion(new GetGachaTickets().receipt _)
    .setEffect()
    .setReload())
    setButton(MenuButton(5, 5, Material.HONEY_BOTTLE, s"${GREEN}ガチャ特等アイテム交換画面を開きます。", List(s"${GRAY}クリックで開きます。"))
    .setLeftClickMotion(new GachaItemChangeGUI(ryoServerAssist).openChangeGUI _)
    .setEffect())
    setButton(MenuButton(1, 6, Material.ENDER_PEARL, s"${GREEN}現在いるワールドのスポーン地点に移動します。", List(s"${GRAY}クリックで移動します。"))
    .setLeftClickMotion(motion.worldTeleport(_, world = false)))
    setButton(MenuButton(2, 6, Material.COMPASS, s"${GREEN}スポーン地点に移動します。", List(s"${GRAY}クリックで移動します。"))
    .setLeftClickMotion(motion.worldTeleport(_, world = true)))
    setButton(MenuButton(3, 6, Material.WHITE_BED, s"${GREEN}ホームメニューを開きます。", List(s"${GRAY}クリックで開きます。"))
    .setLeftClickMotion(new Home(ryoServerAssist).homeInventory _))
    setButton(MenuButton(5, 6, Material.OAK_DOOR, s"${GREEN}ロビーに戻ります。", List(s"${GRAY}クリックでロビーに戻ります。"))
    .setLeftClickMotion(motion.teleportToHub))
    setSkull(MenuSkull(7, 5, p, p.getName + "の情報", List(
      s"${WHITE}レベル: Lv.${p.getQuestLevel}",
      s"${WHITE}EXP: ${f"${p.getQuestExp.toInt}%,3d" + f"${p.getQuestExp - p.getQuestExp.toInt}%.1f".replace("0.",".")}",
      s"${WHITE}ランキング: ${p.getRanking}位",
      s"${WHITE}クエストクリア回数: ${p.getQuestClearTimes}回",
      s"${WHITE}ガチャを引いた回数: ${p.getGachaPullNumber}回",
      s"${WHITE}ログイン日数: ${p.getLoginNumber}日",
      s"${WHITE}連続ログイン日数: ${p.getConsecutiveLoginDays}日",
      s"${WHITE}投票回数: ${p.getVoteNumber}回",
      s"${WHITE}連続投票日数: ${p.getReVoteNumber}日"
    )))
    setButton(MenuButton(9, 5, Material.BOOK, s"${GREEN}イベント", List(s"${GRAY}クリックで表示します。"))
    .setLeftClickMotion(new EventMenu(ryoServerAssist).openEventMenu _)
    .setEffect())
    setButton(MenuButton(9, 6, Material.MAGENTA_GLAZED_TERRACOTTA, s"${GREEN}次のページに移動します。", List(s"${GRAY}クリックで移動します。"))
    .setLeftClickMotion(new RyoServerMenu2(ryoServerAssist).openPage2))
    p.playSound(p.getLocation,Sound.BLOCK_IRON_TRAPDOOR_OPEN,1,1)
    build(new RyoServerMenu1(ryoServerAssist).menu)
    open()
  }

}
