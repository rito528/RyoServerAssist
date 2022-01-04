package com.ryoserver.Quest

import com.ryoserver.Level.Player.GetPlayerData
import com.ryoserver.Menu.Menu
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.NeoStack.NeoStackGateway
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.Entity.getEntity
import com.ryoserver.util.Translate
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class QuestProcessMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  val slot: Int = 6
  var p: Player = _
  var name: String = _

  partButton = true

  def inventory(player: Player): Unit = {
    p = player
    val questGateway = new QuestGateway()
    questGateway.getSelectedQuest(p) match {
      case Some(selectedQuestData) =>
        if (selectedQuestData.questType == "delivery") {
          name = "納品"
          val requireList = questGateway.getQuestProgress(p).map { case (require, amount) =>
            s"$WHITE${Translate.materialNameToJapanese(Material.matchMaterial(require))}:${amount}個"
          }
          setItem(1, 6, Material.BOOK, effect = false, s"$RESET[納品クエスト]${selectedQuestData.questName}", List(
            s"$WHITE【納品リスト】"
          ) ++ requireList ++ List(
            "",
            s"${WHITE}このクエストを完了した際に得られる経験値量:${selectedQuestData.exp}"
          ))
          setItem(2, 6, Material.NETHER_STAR, effect = false, s"${YELLOW}納品する", List(s"${GRAY}クリックで納品します。"))
          val data = new GetPlayerData()
          if (data.getPlayerLevel(p) >= 20) {
            val neoStackGateway = new NeoStackGateway(ryoServerAssist)
            setItem(3, 6, Material.SHULKER_BOX, effect = false, s"${YELLOW}neoStackから納品します。",
              List(s"${GRAY}クリックでneoStackから納品します。") ++ questGateway.getQuestProgress(p).map { case (require, amount) =>
                s"$WHITE${Translate.materialNameToJapanese(Material.matchMaterial(require))}:${
                  val neoStackAmount = neoStackGateway.getNeoStackAmount(p,new ItemStack(Material.matchMaterial(require)))
                  if (neoStackAmount >= amount) s"$AQUA$BOLD${UNDERLINE}OK"
                  else s"$RED$BOLD${UNDERLINE}${-(neoStackAmount - amount)}個不足しています"
                }"
              }
            )
            buttons :+= getLayOut(3, 6)
          }
        } else if (selectedQuestData.questType == "suppression") {
          name = "討伐"
          val requireList = questGateway.getQuestProgress(p).map { case (require, amount) =>
            s"$WHITE${Translate.entityNameToJapanese(getEntity(require))}:${amount}個"
          }
          setItem(1, 6, Material.BOOK, effect = false, s"$RESET[討伐クエスト]${selectedQuestData.questName}", List(
            s"$WHITE【納品リスト】"
          ) ++ requireList ++ List(
            "",
            s"${WHITE}このクエストを完了した際に得られる経験値量:${selectedQuestData.exp}"
          ))
        }
      case None =>
    }
    setItem(9, 6, Material.RED_WOOL, effect = false, s"$RED${BOLD}クエストを中止する",
      List(s"$RED${BOLD}クリックでクエストを中止します。",
        s"$RED$BOLD${UNDERLINE}納品したアイテムは戻りません！"))
    buttons :+= getLayOut(1, 6)
    buttons :+= getLayOut(2, 6)
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
        if (index == getLayOut(2, 6) && player.getOpenInventory.getTopInventory.getItem(getLayOut(2, 6)) != null) {
          new QuestProcessInventoryMotions(ryoServerAssist).delivery(player)
        } else if (index == getLayOut(3, 6) && player.getOpenInventory.getTopInventory.getItem(getLayOut(3, 6)) != null) {
          new QuestProcessInventoryMotions(ryoServerAssist).deliveryFromNeoStack(player)
        }
      }
    }.runTask(ryoServerAssist)
  }

}
