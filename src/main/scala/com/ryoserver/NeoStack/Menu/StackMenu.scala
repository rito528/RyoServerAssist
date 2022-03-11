package com.ryoserver.NeoStack.Menu

import com.ryoserver.Menu.Button.{Button, ButtonMotion}
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{Menu, MenuFrame}
import com.ryoserver.NeoStack.NeoStackPage.NeoStackPageRepository
import com.ryoserver.NeoStack.{Category, NeoStackService}
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.{Item, ItemStackBuilder}
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack

import scala.jdk.CollectionConverters._

class StackMenu(page: Int, category: Category, ryoServerAssist: RyoServerAssist) extends Menu {

  override val frame: MenuFrame = MenuFrame(6, s"neoStack:$page")

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    val compute = computeStackMenuButton(player, page, category, ryoServerAssist)
    import compute._
    new NeoStackPageRepository().getCategoryPageBy(compute.category,compute.page).zipWithIndex.map{case (itemStack,index) =>
      if (itemStack != null) {
        index -> getStackButton(itemStack)
      } else {
        index -> Button(ItemStackBuilder.getDefault(Material.AIR).build())
      }
    }.toMap ++ (if (compute.player.hasPermission("ryoserverassist.neoStack")) Map(
      getLayOut(5, 6) -> openAddMenu
    ) else Map.empty) ++ Map(
      getLayOut(1, 6) -> backPage,
      getLayOut(9, 6) -> nextPage
    )
  }

}

private case class computeStackMenuButton(player: Player, page: Int, category: Category, ryoServerAssist: RyoServerAssist) {
  val backPage: Button = Button(
    ItemStackBuilder
      .getDefault(Material.MAGENTA_GLAZED_TERRACOTTA)
      .title(s"${GREEN}前のページに戻ります。")
      .lore(List(s"${GRAY}クリックで戻ります。"))
      .build(),
    ButtonMotion { _ =>
      val backPage = page - 1
      if (backPage == 0) new CategorySelectMenu(ryoServerAssist).open(player)
      else new StackMenu(backPage, category, ryoServerAssist).open(player)
    }
  )

  val nextPage: Button = Button(
    ItemStackBuilder
      .getDefault(Material.MAGENTA_GLAZED_TERRACOTTA)
      .title(s"${GREEN}次のページに移動します。")
      .lore(List(s"${GRAY}クリックで次のページに移動します。"))
      .build(),
    ButtonMotion { _ =>
      new StackMenu(page + 1, category, ryoServerAssist).open(player)
    }
  )

  val openAddMenu: Button = Button(
    ItemStackBuilder
      .getDefault(Material.CHEST)
      .title(s"${GREEN}アイテムを追加します。")
      .lore(List(s"${GRAY}クリックで追加メニューを開きます。"))
      .build(),
    ButtonMotion { _ =>
      new NeoStackEditGUI(page, category, ryoServerAssist).open(player)
    }
  )

  def getStackButton(itemStack: ItemStack): Button = {
    val is = Item.getOneItemStack(itemStack)
    val playerHasAmount = new NeoStackService().getItemAmount(player.getUniqueId,is).getOrElse(0)
    val setItem = is.clone()
    val meta = setItem.getItemMeta
    meta.setLore(List(
      s"$BLUE${BOLD}保有数:$UNDERLINE${playerHasAmount}個",
      s"${GRAY}右クリックで1つ、左クリックで1st取り出します。"
    ).asJava)
    setItem.setItemMeta(meta)
    Button(
      setItem,
      ButtonMotion { e =>
        val neoStackService = new NeoStackService
        e.getClick match {
          case ClickType.LEFT =>
            neoStackService.addItemToPlayer(player,is,is.getType.getMaxStackSize)
          case ClickType.RIGHT =>
            neoStackService.addItemToPlayer(player, is, 1)
          case _ =>
        }
        new StackMenu(page, category, ryoServerAssist).open(player)
      }
    )
  }
}