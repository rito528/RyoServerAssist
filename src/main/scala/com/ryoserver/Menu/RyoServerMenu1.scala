package com.ryoserver.Menu

import com.ryoserver.Distribution.Distribution
import com.ryoserver.DustBox.DustBoxInventory
import com.ryoserver.Gacha.{GachaItemChangeGUI, GetGachaTickets}
import com.ryoserver.Home.Home
import com.ryoserver.Level.Player.GetPlayerData
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.NeoStack.Menu.CategorySelectMenu
import com.ryoserver.Player.{Data, GetData, GetRyoServerPlayerData}
import com.ryoserver.Quest.Event.EventMenu
import com.ryoserver.Quest.QuestMenu
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.SkillMenu.SkillCategoryMenu
import com.ryoserver.Storage.Storage
import com.ryoserver.Title.TitleMenu
import com.ryoserver.World.SimpleRegion.RegionMenu
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player

class RyoServerMenu1(ryoServerAssist: RyoServerAssist) extends Menu {

  val slot = 6
  var name = "りょう鯖メニュー"
  var p: Player = _

  def menu(player: Player): Unit = {
    p = player
    setItem(1, 1, Material.CRAFTING_TABLE, effect = false, s"${GREEN}作業台を開きます。", List(s"${GRAY}クリックで開きます。"))
    setItem(3, 1, Material.WOODEN_AXE, effect = false, s"${GREEN}保護メニューを開きます。", List(s"${GRAY}クリックで開きます。"))
    setItem(5, 1, Material.BOOK, effect = false, s"${GREEN}クエストを選択します。", List(s"${GRAY}クリックで開きます。"))
    setItem(7, 1, Material.BEACON, effect = false, s"${GREEN}スキルを選択します。", List(s"${GRAY}クリックで開きます。"))
    setItem(9, 1, Material.NAME_TAG, effect = false, s"${GREEN}称号一覧を開きます。", List(s"${GRAY}クリックで開きます。"))
    setItem(1, 3, Material.CHEST, effect = false, s"${GREEN}ストレージを開きます。", List(s"${GRAY}クリックで開きます。"))
    setItem(3, 3, Material.ENDER_CHEST, effect = false, s"${GREEN}エンダーチェストを開きます。", List(s"${GRAY}クリックで開きます。"))
    setItem(5, 3, Material.SHULKER_BOX, effect = false, s"${GREEN}ネオスタックを開きます。", List(s"${GRAY}クリックで開きます"))
    setItem(7, 3, Material.LAVA_BUCKET, effect = false, s"${GREEN}ゴミ箱を開きます。", List(s"${GRAY}クリックで開きます。", s"$RED${BOLD}取扱注意！"))
    setItem(9, 3, Material.FIREWORK_ROCKET, effect = false, s"${GREEN}ロケット花火を受け取ります。", List(s"${GRAY}クリックで受け取ります。"))
    setItem(1, 5, Material.CHEST_MINECART, effect = false, s"${GREEN}運営からのガチャ券を受け取ります。", List(
      s"${GRAY}クリックで受け取ります。",
      s"${GRAY}受け取れるガチャ券の枚数: ${new GetData().getFromAdminTickets(p)}枚"
    ))
    setItem(3, 5, Material.PAPER, effect = true, s"${GREEN}ガチャ券を受け取ります。", List(
      s"${GRAY}クリックで受け取ります。",
      s"${GRAY}ガチャ券はEXPが100毎に1枚、または",
      s"${GRAY}レベルが10上がる毎に32枚手に入ります。",
      s"${GRAY}受け取れるガチャ券の枚数:" + Data.playerData(p.getUniqueId).gachaTickets + "枚",
      s"${GRAY}次のガチャ券まであと" + String.format("%.1f", (100 - Data.playerData(p.getUniqueId).exp % 100))))
    setItem(5, 5, Material.HONEY_BOTTLE, effect = true, s"${GREEN}ガチャ特等アイテム交換画面を開きます。", List(s"${GRAY}クリックで開きます。"))
    setItem(1, 6, Material.ENDER_PEARL, effect = false, s"${GREEN}現在いるワールドのスポーン地点に移動します。", List(s"${GRAY}クリックで移動します。"))
    setItem(2, 6, Material.COMPASS, effect = false, s"${GREEN}スポーン地点に移動します。", List(s"${GRAY}クリックで移動します。"))
    setItem(3, 6, Material.WHITE_BED, effect = false, s"${GREEN}ホームメニューを開きます。", List(s"${GRAY}クリックで開きます。"))
    setItem(5, 6, Material.OAK_DOOR, effect = false, s"${GREEN}ロビーに戻ります。", List(s"${GRAY}クリックでロビーに戻ります。"))
    val playerData = Data.playerData(p.getUniqueId)
    setSkullItem(7, 5, p, p.getName + "の情報", List(
      s"${WHITE}レベル: Lv.${playerData.level}",
      s"${WHITE}EXP: ${playerData.exp}",
      s"${WHITE}ランキング: ${new GetRyoServerPlayerData(p).getRanking}位",
      s"${WHITE}クエストクリア回数: ${playerData.questClearTimes}回",
      s"${WHITE}ガチャを引いた回数: ${playerData.gachaPullNumber}回",
      s"${WHITE}ログイン日数: ${playerData.loginNumber}日",
      s"${WHITE}連続ログイン日数: ${playerData.consecutiveLoginDays}日",
      s"${WHITE}投票回数: ${playerData.voteNumber}回"
    ))
    setItem(9, 5, Material.BOOK, effect = true, s"${GREEN}イベント", List(s"${GRAY}クリックで表示します。"))
    setItem(9, 6, Material.MAGENTA_GLAZED_TERRACOTTA, effect = false, s"${GREEN}次のページに移動します。", List(s"${GRAY}クリックで移動します。"))
    registerMotion(registerMenu)
    open()
  }

  def registerMenu(player: Player, index: Int): Unit = {
    val motion = new MenuMotion(ryoServerAssist)
    val motions = Map[Int, Player => Unit](
      getLayOut(1, 1) -> motion.openWorkBench,
      getLayOut(3, 1) -> new RegionMenu(ryoServerAssist).menu _,
      getLayOut(5, 1) -> new QuestMenu(ryoServerAssist).selectInventory _,
      getLayOut(7, 1) -> new SkillCategoryMenu(ryoServerAssist).openSkillCategoryMenu _,
      getLayOut(9, 1) -> {
        new TitleMenu(ryoServerAssist).openInv(_, 1)
      },
      getLayOut(1, 3) -> new Storage(ryoServerAssist).load _,
      getLayOut(3, 3) -> motion.openEnderChest,
      getLayOut(5, 3) -> new CategorySelectMenu(ryoServerAssist).openCategorySelectMenu _,
      getLayOut(7, 3) -> new DustBoxInventory().openDustBox _,
      getLayOut(9, 3) -> motion.giveFirework,
      getLayOut(1, 5) -> new Distribution(ryoServerAssist).receipt _,
      getLayOut(3, 5) -> new GetGachaTickets().receipt _,
      getLayOut(5, 5) -> new GachaItemChangeGUI(ryoServerAssist).openChangeGUI _,
      getLayOut(9, 5) -> new EventMenu(ryoServerAssist).openEventMenu _,
      getLayOut(1, 6) -> {
        motion.worldTeleport(_, world = false)
      },
      getLayOut(2, 6) -> {
        motion.worldTeleport(_, world = true)
      },
      getLayOut(3, 6) -> new Home(ryoServerAssist).homeInventory _,
      getLayOut(5, 6) -> motion.teleportToHub,
      getLayOut(9, 6) -> new RyoServerMenu2(ryoServerAssist).openPage2 _
    )
    if (motions.contains(index)) {
      motions(index)(player)
      if (index == getLayOut(1, 5) || index == getLayOut(3, 5)) {
        new RyoServerMenu1(ryoServerAssist).menu(player)
      }
    }
  }

}
