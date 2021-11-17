package com.ryoserver.Quest

import com.ryoserver.Level.Player.GetPlayerData
import com.ryoserver.Menu.Menu
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.Entity.getEntity
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

import java.util
import scala.jdk.CollectionConverters._

class QuestProcessMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  val slot: Int = 6
  var p: Player = _
  var name: String = _

  partButton = true

  def inventory(player: Player): Unit = {
    p = player
    val lottery = new LotteryQuest()
    val questData = new QuestData(ryoServerAssist)
    lottery.questName = questData.getSelectedQuest(p)
    lottery.loadQuestData()
    val questType = if (lottery.questType.equalsIgnoreCase("delivery")) "納品クエスト"
    else if (lottery.questType.equalsIgnoreCase("suppression")) "討伐クエスト"
    val questDetails: java.util.List[String] = new util.ArrayList[String]()
    questDetails.add(WHITE + "【残りリスト】")
    if (questType == "納品クエスト") {
      name = "納品"
      questData.getSelectedQuestRemaining(p).split(";").foreach(i => {
        val material = Material.matchMaterial(i.split(":")(0))
        val itemStack = new ItemStack(material)
        var itemName = ""
        if (material.isBlock) itemName = "block." + itemStack.getType.getKey.toString.replace(":", ".")
        else if (material.isItem) itemName = "item." + itemStack.getType.getKey.toString.replace(":", ".")
        questDetails.add(WHITE + "・" + LoadQuests.langFile.get(itemName).textValue() + ":" + i.split(":")(1) + "個")
      })
    } else if (questType == "討伐クエスト") {
      name = "討伐"
      questData.getSelectedQuestRemaining(p).split(";").foreach(e => {
        val entity = getEntity(e.split(":")(0))
        questDetails.add(WHITE + "・" + LoadQuests.langFile.get("entity." + entity.getKey.toString.replace(":", ".")).textValue()
          + ":" + e.split(":")(1) + "体")
      })
    }
    questDetails.add(WHITE + "【説明】")
    questDetails.add(WHITE + "このクエストを完了した際に得られる経験値量:" + lottery.exp)
    setItem(1, 6, Material.BOOK, effect = false, s"[$questType]" + lottery.questName, questDetails.asScala.toList)
    if (questType == "納品クエスト") {
      setItem(2, 6, Material.NETHER_STAR, effect = false, s"${YELLOW}納品する", List(s"${GRAY}クリックで納品します。"))
      val data = new GetPlayerData(ryoServerAssist)
      if (data.getPlayerLevel(p) >= 20) setItem(3, 6, Material.SHULKER_BOX, effect = false, s"${YELLOW}neoStackから納品します。", List(s"${GRAY}クリックでneoStackから納品します。"))
    }
    setItem(9, 6, Material.RED_WOOL, effect = false, s"$RED${BOLD}クエストを中止する",
      List(s"$RED${BOLD}クリックでクエストを中止します。",
        s"$RED$BOLD${UNDERLINE}納品したアイテムは戻りません！"))
    buttons :+= getLayOut(1, 6)
    buttons :+= getLayOut(2, 6)
    buttons :+= getLayOut(3, 6)
    buttons :+= getLayOut(9, 6)
    registerMotion(motion)
    open()
  }

  def motion(player: Player, index: Int): Unit = {
    new BukkitRunnable {
      override def run(): Unit = {
        val motions = Map[Int, Player => Unit](
          getLayOut(9, 6) -> {
            new QuestProcessInventoryMotions(ryoServerAssist).questDestroy
          }
        )
        if (motions.contains(index) && motions(index) != null) motions(index)(player)
        if (index == getLayOut(2, 6) && player.getOpenInventory.getTopInventory.getItem(getLayOut(2, 6)) != null) new QuestProcessInventoryMotions(ryoServerAssist).delivery(player, player.getOpenInventory.getTopInventory)
        else if (index == getLayOut(3,6) && player.getOpenInventory.getTopInventory.getItem(getLayOut(3,6)) != null) new QuestProcessInventoryMotions(ryoServerAssist).deliveryFromNeoStack(player,player.getOpenInventory.getTopInventory)
      }
    }.runTask(ryoServerAssist)
  }

}
