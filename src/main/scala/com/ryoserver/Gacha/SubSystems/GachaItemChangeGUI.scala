package com.ryoserver.Gacha.SubSystems

import com.ryoserver.Config.ConfigData.getConfig
import com.ryoserver.Gacha.GachaLoader
import com.ryoserver.Menu.Button.{Button, ButtonMotion}
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{Menu, MenuFrame}
import com.ryoserver.RyoServerAssist
import com.ryoserver.RyoServerMenu.RyoServerMenu1
import com.ryoserver.SkillSystems.SkillPoint.RecoveryItems
import com.ryoserver.util.ItemStackBuilder
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player

class GachaItemChangeGUI(implicit ryoServerAssist: RyoServerAssist) extends Menu {

  override val frame: MenuFrame = MenuFrame(6, "ガチャ特等取引")
  override val partButton: Boolean = true

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    val compute = computeGachaItemChangeMenuButton(player, ryoServerAssist)
    import compute._
    Map(
      getLayOut(1, 6) -> backPage,
      getLayOut(5, 6) -> change
    )
  }

}

private case class computeGachaItemChangeMenuButton(player: Player, ryoServerAssist: RyoServerAssist) {
  private lazy val rate: Int = getConfig.gachaChangeRate
  private implicit val plugin: RyoServerAssist = ryoServerAssist

  val backPage: Button = Button(
    ItemStackBuilder
      .getDefault(Material.MAGENTA_GLAZED_TERRACOTTA)
      .title(s"${GREEN}メニューに戻る")
      .lore(List(s"${GRAY}クリックでメニューに戻ります。"))
      .build(),
    ButtonMotion { _ =>
      new RyoServerMenu1(ryoServerAssist).open(player)
    }
  )

  val change: Button = Button(
    ItemStackBuilder
      .getDefault(Material.NETHER_STAR)
      .title(s"$RESET${GREEN}クリックでインベントリ内の特等アイテムを交換します。")
      .lore(List(
        s"${GRAY}クリックでガチャアイテムを交換します。", s"${GRAY}特等アイテム1個 -> スキル回復(大)${rate}個"
      ))
      .build(),
    ButtonMotion { _ =>
      changeItem(player)
    }
  )

  private def changeItem(p: Player): Unit = {
    var changeAmount = 0
    val inv = p.getOpenInventory.getTopInventory
    inv.getContents.foreach(itemStack => {
      if (itemStack != null && GachaLoader.specialItemList.toList.contains(itemStack)) {
        changeAmount += rate
      }
    })
    if (changeAmount != 0) {
      val item = RecoveryItems.max.clone()
      item.setAmount(changeAmount)
      p.getWorld.dropItem(p.getLocation, item)
      p.sendMessage(s"${AQUA}特等アイテムを${changeAmount}個のスキル回復(大)と交換しました。")
      inv.getContents.zipWithIndex.foreach { case (is, index) =>
        if (GachaLoader.specialItemList.toList.contains(is)) inv.clear(index)
      }
      new GachaItemChangeGUI().open(p)
    } else {
      p.sendMessage(s"${RED}特等アイテムが見つかりませんでした。")
    }
  }

}