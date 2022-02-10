package com.ryoserver.World.SimpleRegion

import com.ryoserver.Menu.Button.{Button, ButtonMotion}
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{Menu, MenuFrame}
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.{ItemStackBuilder, WorldGuardWrapper}
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class RegionMenu(implicit ryoServerAssist: RyoServerAssist) extends Menu {

  override val frame: MenuFrame = MenuFrame(1,"保護メニュー")

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    val compute = computeButton(player,ryoServerAssist)
    import compute._
    Map(
      getLayOut(3,1) -> giveWoodenAxe,
      getLayOut(5,1) -> createRegion,
      getLayOut(7,1) -> openEditMenu
    )
  }

}

private case class computeButton(p: Player,ryoServerAssist: RyoServerAssist) {
  val giveWoodenAxe: Button = Button(
    ItemStackBuilder
      .getDefault(Material.WOODEN_AXE)
      .title(s"${GREEN}木の斧を取得します。")
      .lore(List(
        s"${GRAY}保護方法",
        s"${GRAY}木の斧で始点を左クリックします。",
        s"${GRAY}終点を対角で右クリックします。",
        s"${GRAY}その後、ダイヤの斧をクリックします。"))
      .build(),
    ButtonMotion{_ =>
      p.getInventory.addItem(new ItemStack(Material.WOODEN_AXE, 1))
      p.sendMessage(s"${AQUA}保護用の木の斧を配布しました。")
    }
  )

  val createRegion: Button = Button(
    ItemStackBuilder
      .getDefault(Material.DIAMOND_AXE)
      .title(s"${GREEN}保護をします。")
      .lore(List(
        s"${GRAY}クリックで保護を開始します。",
        s"${GRAY}結果がチャットに表示されます。"
      ))
      .build(),
    ButtonMotion{_ =>
      new WorldGuardWrapper().createRegion(p)
    }
  )

  val openEditMenu: Button = Button(
    ItemStackBuilder
      .getDefault(Material.GOLDEN_AXE)
      .title(s"${GREEN}保護編集メニューを開きます。")
      .lore(List(
        s"${GRAY}クリックで保護編集メニューを開きます。",
        s"${GRAY}自分が管理者の保護範囲内にいる必要があります。"
      ))
      .build(),
    ButtonMotion{_ =>
      new RegionSettingMenu(ryoServerAssist).openMenu(p)
    }
  )
}
