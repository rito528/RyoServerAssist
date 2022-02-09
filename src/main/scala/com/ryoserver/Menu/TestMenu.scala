package com.ryoserver.Menu
import com.ryoserver.Menu.Button.{Button, LeftClickMotion}
import com.ryoserver.Menu.Contexts.MenuFrame
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.util.ItemStackBuilder
import org.bukkit.Material
import org.bukkit.entity.Player

object TestMenu extends Menu {
  override val frame: MenuFrame = MenuFrame(6,"テストメニュー")

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    Map(
      getLayOut(1,1) -> testButton
    )
  }

  val testButton: Button = Button(
    ItemStackBuilder.getDefault(Material.GRASS_BLOCK).title("テスト").lore(List("テストロール")).build(),
    LeftClickMotion { e =>
      println(s"player:${e.getWhoClicked.getName}")
      println("テストボタンがクリックされました")
    }
  )
}
