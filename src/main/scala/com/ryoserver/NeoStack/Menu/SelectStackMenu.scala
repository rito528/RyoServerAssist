package com.ryoserver.NeoStack.Menu

import com.ryoserver.Menu.Button.{Button, ButtonMotion}
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{Menu, MenuFrame}
import com.ryoserver.NeoStack.NeoStackItem.NeoStackItemRepository
import com.ryoserver.NeoStack.{NeoStackService, RawNeoStackItemAmountContext}
import com.ryoserver.util.{Item, ItemStackBuilder}
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player

class SelectStackMenu extends Menu {

  override val frame: MenuFrame = MenuFrame(6, "ネオスタック選択収納")
  override val partButton: Boolean = true

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    val compute = computeSelectStackButton(player)
    import compute._
    Map(
      getLayOut(5, 6) -> stack
    )
  }

}

private case class computeSelectStackButton(player: Player) {
  val stack: Button = Button(
    ItemStackBuilder
      .getDefault(Material.CHEST_MINECART)
      .title(s"${GREEN}neoStackに収納します。")
      .lore(List(s"${GRAY}クリックで収納します。"))
      .build(),
    ButtonMotion { _ =>
      player.getOpenInventory.getTopInventory.getContents.foreach(item => {
        if (item != null) {
          val oneItemStack = Item.getOneItemStack(item)
          val uuid = player.getUniqueId
          val service = new NeoStackService
          if (service.isItemExists(oneItemStack)) {
            service.addItemAmount(uuid,oneItemStack,item.getAmount)
            player.getOpenInventory.getTopInventory.removeItem(item)
          }
        }
      })
      player.sendMessage(s"${AQUA}選択されたアイテムをneoStackに収納しました。")
    }
  )
}