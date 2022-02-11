package com.ryoserver.NeoStack.Menu

import com.ryoserver.Menu.Button.{Button, ButtonMotion}
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{Menu, MenuFrame}
import com.ryoserver.NeoStack.NeoStackPageData.stackPageData
import com.ryoserver.NeoStack.{NeoStackGateway, PlayerData}
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.{Item, ItemStackBuilder}
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack

class StackMenu(page:Int,category:String,ryoServerAssist: RyoServerAssist) extends Menu {

  override val frame: MenuFrame = MenuFrame(6,s"neoStack:$page")

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    val compute = computeStackMenuButton(player, page, category, ryoServerAssist)
    import compute._
    Map(
      getLayOut(1, 6) -> backPage,
      getLayOut(9, 6) -> nextPage
    ) ++ (if (compute.player.hasPermission("ryoserverassist.neoStack")) Map(
      getLayOut(5, 6) -> openAddMenu
    ) else Map.empty) ++ {
      if (stackPageData.contains(compute.category) && stackPageData(compute.category).contains(compute.page)) {
        stackPageData(compute.category)(compute.page)
      } else {
        ""
      }
    }.split(";").zipWithIndex.map { case (item, index) =>
      val itemStack = Item.getItemStackFromString(item)
      if (itemStack != null) {
        index - ((getLayOut(9, 5) + 1) * (this.page - 1)) -> getStackButton(itemStack)
      } else {
        index - ((getLayOut(9, 5) + 1) * (this.page - 1)) -> Button(ItemStackBuilder.getDefault(Material.AIR).build())
      }
    }
  }

}

private case class computeStackMenuButton(player: Player,page: Int,category: String,ryoServerAssist: RyoServerAssist) {
  val backPage: Button = Button(
    ItemStackBuilder
      .getDefault(Material.MAGENTA_GLAZED_TERRACOTTA)
      .title(s"${GREEN}前のページに戻ります。")
      .lore(List(s"${GRAY}クリックで戻ります。"))
      .build(),
    ButtonMotion{_ =>
      val categoryMenu = new CategorySelectMenu(ryoServerAssist)
      val backPage = page - 1
      if (backPage == 0) categoryMenu.open(player)
      else new StackMenu(backPage,category,ryoServerAssist).open(player)
    }
  )

  val nextPage: Button = Button(
    ItemStackBuilder
      .getDefault(Material.MAGENTA_GLAZED_TERRACOTTA)
      .title(s"${GREEN}次のページに移動します。")
      .lore(List(s"${GRAY}クリックで次のページに移動します。"))
      .build(),
    ButtonMotion{_ =>
      new StackMenu(page + 1,category,ryoServerAssist).open(player)
    }
  )

  val openAddMenu: Button = Button(
    ItemStackBuilder
      .getDefault(Material.CHEST)
      .title(s"${GREEN}アイテムを追加します。")
      .lore(List(s"${GRAY}クリックで追加メニューを開きます。"))
      .build(),
    ButtonMotion{_ =>
      new NeoStackEditGUI(page,category,ryoServerAssist).open(player)
    }
  )

  def getStackButton(itemStack: ItemStack): Button = {
    val is = Item.getOneItemStack(itemStack)
    val playerData = PlayerData.playerData
      .filter(_.uuid == player.getUniqueId)
      .filter(_.savingItemStack == is)
    Button(
      ItemStackBuilder
        .getDefault(is.getType)
        .lore(List(
          s"$BLUE${BOLD}保有数:$UNDERLINE${if (playerData.isEmpty) 0 else playerData.head.amount}個",
          s"${GRAY}右クリックで1つ、左クリックで1st取り出します。"
        ))
        .build(),
      ButtonMotion{e =>
        val neoStackGateway = new NeoStackGateway()
        e.getClick match {
          case ClickType.LEFT =>
            neoStackGateway.addItemToPlayer(player, playerData.head.savingItemStack, is.getType.getMaxStackSize)
          case ClickType.RIGHT =>
            neoStackGateway.addItemToPlayer(player, playerData.head.savingItemStack, 1)
          case _ =>
        }
        new StackMenu(page, category, ryoServerAssist).open(player)
      }
    )
  }
}