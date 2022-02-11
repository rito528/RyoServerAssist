package com.ryoserver.NeoStack.Menu

import com.ryoserver.Menu.Button.{Button, ButtonMotion}
import com.ryoserver.Menu.MenuLayout.{getLayOut, getX, getY}
import com.ryoserver.Menu.{Menu, MenuFrame}
import com.ryoserver.NeoStack.PlayerCategory.getSelectedCategory
import com.ryoserver.NeoStack.{ItemList, LoadNeoStackPage, NeoStackGateway}
import com.ryoserver.RyoServerAssist
import com.ryoserver.util.{Item, ItemStackBuilder}
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

import scala.collection.mutable

class NeoStackEditGUI(page:Int,category:String,ryoServerAssist: RyoServerAssist) extends Menu {

  override val partButton: Boolean = true

  override val frame: MenuFrame = MenuFrame(6,s"neoStackアイテム追加メニュー:$page")

  override def noneOperationButton(player: Player): Map[Int, Button] = {
    super.noneOperationButton(player)
    implicit val session: AutoSession.type = AutoSession
    println(sql"SELECT * FROM StackList WHERE page=$page AND category=$category".map(rs => {
      rs.string("invItem").split(";").zipWithIndex.map { case (itemStackString, index) =>
        val itemStack = Item.getItemStackFromString(itemStackString)
        if (itemStack != null) getLayOut(getX(index),getY(index)) -> Button(itemStack)
        else getLayOut(getX(index),getY(index)) -> Button(new ItemStack(Material.AIR))
      }
    }).toIterable().apply().flatten.toMap.foreach{ case (index,button) =>
      println(index)
      println(button)
    })
    sql"SELECT * FROM StackList WHERE page=$page AND category=$category".map(rs => {
      rs.string("invItem").split(";").zipWithIndex.map { case (itemStackString, index) =>
        val itemStack = Item.getItemStackFromString(itemStackString)
        if (itemStack != null) getLayOut(getX(index),getY(index)) -> Button(itemStack)
        else getLayOut(getX(index),getY(index)) -> Button(new ItemStack(Material.AIR))
      }
    }).toIterable().apply().flatten.toMap
  }

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    val compute = computeNeoStackEditMenuButton(page,category,player,ryoServerAssist)
    import compute._
    Map(
      getLayOut(1,6) -> backPage,
      getLayOut(5,6) -> register,
      getLayOut(9,6) -> nextPage
    )
  }

}

private case class computeNeoStackEditMenuButton(page:Int,category:String,player: Player,ryoServerAssist: RyoServerAssist) {
  private implicit val plugin: RyoServerAssist = ryoServerAssist
  val backPage: Button = Button(
    ItemStackBuilder
      .getDefault(Material.MAGENTA_GLAZED_TERRACOTTA)
      .title(s"${GREEN}前のページに戻ります。")
      .lore(List(s"${GRAY}クリックで戻ります。"))
      .build(),
    ButtonMotion{_ =>
      if (page == 1) new CategorySelectMenu(ryoServerAssist).open(player)
      else new NeoStackEditGUI(page - 1,category, ryoServerAssist)
    }
  )

  val nextPage: Button = Button(
    ItemStackBuilder
      .getDefault(Material.MAGENTA_GLAZED_TERRACOTTA)
      .title(s"${GREEN}次のページに移動します。")
      .lore(List(s"${GRAY}クリックで移動します。"))
      .build(),
    ButtonMotion{_ =>
      new NeoStackEditGUI(page + 1,category, ryoServerAssist)
    }
  )

  val register: Button = Button(
    ItemStackBuilder
      .getDefault(Material.NETHER_STAR)
      .title(s"${GREEN}クリックでリストを保存します。")
      .lore(List(s"${GRAY}カテゴリ:" + getSelectedCategory(player) + "のリストを保存します。"))
      .build(),
    ButtonMotion{_ =>
      val data = new NeoStackGateway()
      var invIndex = 0
      var invItem = ""
      player.getOpenInventory.getTopInventory.getContents.foreach(is => {
        if (invIndex < 45) {
          invItem += Item.getStringFromItemStack(is) + ";"
        }
        invIndex += 1
      })
      data.editItemList(getSelectedCategory(player), page, invItem)
      player.sendMessage(s"${AQUA}カテゴリリスト:${getSelectedCategory(player)}を編集しました。")
      new LoadNeoStackPage().loadStackPage()
      ItemList.stackList = mutable.Map.empty
      ItemList.loadItemList(ryoServerAssist)
    }
  )

  def getNoneButton(itemStack: ItemStack): Button = {
    Button(itemStack)
  }
}
