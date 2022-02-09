package com.ryoserver.Menu
import com.ryoserver.Menu.Button.{Button, ButtonMotion}
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
    ButtonMotion { e =>
      println(s"test click slot:${e.getSlot}")
      e.setCancelled(true)
    }
  )
}
