package com.ryoserver.Gacha.SubSystems

import com.ryoserver.Gacha.{GachaGateway, Rarity}
import com.ryoserver.Menu.Button.{Button, ButtonMotion}
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{Menu, MenuFrame}
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.ItemStackBuilder
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player

class GachaAddItemInventory(ryoServerAssist: RyoServerAssist) extends Menu {

  override val frame: MenuFrame = MenuFrame(6, "ガチャアイテム追加メニュー")
  override val partButton: Boolean = true

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    val compute = computeGachaAddItemInventory(player, ryoServerAssist)
    import compute._
    Map(
      getLayOut(2, 6) -> addMiss,
      getLayOut(4, 6) -> addPer,
      getLayOut(6, 6) -> addBigPer,
      getLayOut(8, 6) -> addSpecial
    )
  }

}

private case class computeGachaAddItemInventory(player: Player, ryoServerAssist: RyoServerAssist) {
  private lazy val inv = player.getOpenInventory.getTopInventory

  val addMiss: Button = Button(
    ItemStackBuilder
      .getDefault(Material.EXPERIENCE_BOTTLE)
      .title(s"${GREEN}はずれにアイテムを追加します。")
      .build(),
    ButtonMotion { _ =>
      add(Rarity.miss)
      player.sendMessage(s"${AQUA}はずれにアイテムを追加しました。")
    }
  )

  val addPer: Button = Button(
    ItemStackBuilder
      .getDefault(Material.PHANTOM_MEMBRANE)
      .title(s"${GREEN}あたりにアイテムを追加します。")
      .build(),
    ButtonMotion { _ =>
      add(Rarity.per)
      player.sendMessage(s"${AQUA}あたりにアイテムを追加しました。")
    }
  )

  val addBigPer: Button = Button(
    ItemStackBuilder
      .getDefault(Material.DIAMOND)
      .title(s"${GREEN}大当たりにアイテムを追加します。")
      .build(),
    ButtonMotion { _ =>
      add(Rarity.bigPer)
      player.sendMessage(s"${AQUA}大当たりにアイテムを追加しました。")
    }
  )

  val addSpecial: Button = Button(
    ItemStackBuilder
      .getDefault(Material.NETHERITE_INGOT)
      .title(s"${GREEN}特等にアイテムを追加します。")
      .build(),
    ButtonMotion { _ =>
      add(Rarity.special)
      player.sendMessage(s"${AQUA}特等にアイテムを追加しました。")
    }
  )

  private def add(rarity: Rarity): Unit = {
    List(46, 48, 50, 52).foreach(inv.clear)
    inv.getContents.foreach(is => {
      if (is != null) new GachaGateway().addGachaItem(ryoServerAssist, is, rarity)
    })
  }
}