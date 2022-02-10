package com.ryoserver.Menu

import com.ryoserver.Menu.Button.Button
import com.ryoserver.Menu.session.MenuSession
import org.bukkit.entity.Player

trait Menu {

  val frame: MenuFrame //Menuの構成を定義する
  val partButton: Boolean = false //MenuでButton以外のクリックをキャンセルしない場合はoverrideしてtrueにしてください

  def settingMenuLayout(player: Player): Map[Int,Button]

  /*
    メニューを開く前に何らかの動作を入れたい場合はopenMotionをoverrideして使ってください。
    返り値がfalseになるとメニューが開かなくなります。
   */
  def openMotion(player: Player): Boolean = {true}

  final def open(player: Player): Unit = {
    if (openMotion(player)) {
      val layout = settingMenuLayout(player)
      val session = new MenuSession(frame) {
        override val currentLayout: Map[Int, Button] = layout
        override val isPartButton: Boolean = partButton
      }
      session.setLayout(layout)
      session.openInventory(player)
    }
  }

}
