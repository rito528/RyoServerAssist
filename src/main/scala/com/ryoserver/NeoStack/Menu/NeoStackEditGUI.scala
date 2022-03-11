package com.ryoserver.NeoStack.Menu

import com.ryoserver.Menu.Button.{Button, ButtonMotion}
import com.ryoserver.Menu.MenuLayout.{getLayOut, getX, getY}
import com.ryoserver.Menu.{Menu, MenuFrame}
import com.ryoserver.NeoStack.NeoStackPage.NeoStackPageRepository
import .getSelectedCategory
import com.ryoserver.NeoStack.Category
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.{Item, ItemStackBuilder}
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

import scala.collection.mutable

class NeoStackEditGUI(page: Int, category: Category, ryoServerAssist: RyoServerAssist) extends Menu {

  override val partButton: Boolean = true
  override val isReturnItem: Boolean = false

  override val frame: MenuFrame = MenuFrame(6, s"neoStackアイテム追加メニュー:$page")

  override def noneOperationButton(player: Player): Map[Int, Button] = {
    super.noneOperationButton(player)
    new NeoStackPageRepository().getCategoryPageBy(category, page).zipWithIndex.map{case (itemStack,index) =>
      if (itemStack != null) getLayOut(getX(index), getY(index)) -> Button(itemStack)
      else getLayOut(getX(index), getY(index)) -> Button(new ItemStack(Material.AIR))
    }.toMap
  }

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    val compute = computeNeoStackEditMenuButton(page, category, player, ryoServerAssist)
    import compute._
    Map(
      getLayOut(1, 6) -> backPage,
      getLayOut(5, 6) -> register,
      getLayOut(9, 6) -> nextPage
    )
  }

}

private case class computeNeoStackEditMenuButton(page: Int, category: Category, player: Player, ryoServerAssist: RyoServerAssist) {
  private implicit val plugin: RyoServerAssist = ryoServerAssist
  val backPage: Button = Button(
    ItemStackBuilder
      .getDefault(Material.MAGENTA_GLAZED_TERRACOTTA)
      .title(s"${GREEN}前のページに戻ります。")
      .lore(List(s"${GRAY}クリックで戻ります。"))
      .build(),
    ButtonMotion { _ =>
      if (page == 1) new CategorySelectMenu(ryoServerAssist).open(player)
      else new NeoStackEditGUI(page - 1, category, ryoServerAssist)
    }
  )

  val nextPage: Button = Button(
    ItemStackBuilder
      .getDefault(Material.MAGENTA_GLAZED_TERRACOTTA)
      .title(s"${GREEN}次のページに移動します。")
      .lore(List(s"${GRAY}クリックで移動します。"))
      .build(),
    ButtonMotion { _ =>
      new NeoStackEditGUI(page + 1, category, ryoServerAssist)
    }
  )

  val register: Button = Button(
    ItemStackBuilder
      .getDefault(Material.NETHER_STAR)
      .title(s"${GREEN}クリックでリストを保存します。")
      .lore(List(s"${GRAY}カテゴリ:" + category.name + "のリストを保存します。"))
      .build(),
    ButtonMotion { _ =>
      val neoStackPageRepository = new NeoStackPageRepository
      val invItems = player.getOpenInventory.getTopInventory.getContents.toList.map(Item.getOneItemStack)
      neoStackPageRepository.changeItem(category,page,invItems)
      neoStackPageRepository.store(category,page)
      player.sendMessage(s"${AQUA}カテゴリリスト:${category.name}を編集しました。")
    }
  )

  def getNoneButton(itemStack: ItemStack): Button = {
    Button(itemStack)
  }
}
