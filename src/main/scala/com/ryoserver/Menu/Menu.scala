package com.ryoserver.Menu

import com.ryoserver.Menu.Button.Button
import com.ryoserver.Menu.session.MenuSession
import org.bukkit.entity.Player

trait Menu {

  val frame: MenuFrame //Menuの構成を定義する
  val partButton: Boolean = false //MenuでButton以外のクリックをキャンセルしない場合はoverrideしてtrueにしてください
  val isReturnItem: Boolean = true //Menuのアイテムを最後返却しない場合はoverrideしてfalseにしてください

  def settingMenuLayout(player: Player): Map[Int, Button]

  final def open(player: Player): Unit = {
    if (openMotion(player)) {
      val layout = settingMenuLayout(player)
      val session = new MenuSession(frame) {
        override val currentLayout: Map[Int, Button] = layout
        override val isPartButton: Boolean = partButton
        override val noneOperationButtons: Map[Int, Button] = noneOperationButton(player)
        override val returnItem: Boolean = isReturnItem
      }
      session.setNoneOperationButton()
      session.setLayout(layout)
      session.openInventory(player)
    }
  }

  /*
    メニューを開く前に何らかの動作を入れたい場合はopenMotionをoverrideして使ってください。
    返り値がfalseになるとメニューが開かなくなります。
   */
  def openMotion(player: Player): Boolean = {
    true
  }

  /*
    動作を行わないボタンを追加した場合にoverrideして使ってください。
    ここから追加したボタンは通常のアイテムと同じように扱われます。
   */
  def noneOperationButton(player: Player): Map[Int, Button] = Map.empty

}
