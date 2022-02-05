package com.ryoserver.Commands

import com.ryoserver.Commands.Executer.Contexts.{CommandContext, RawCommandContext}
import com.ryoserver.Commands.Executer.ContextualTabExecutor
import com.ryoserver.util.Item
import com.ryoserver.util.ScalikeJDBC.getData
import org.bukkit.Bukkit
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

object PlayerCommand {

  val executor: TabExecutor = ContextualTabExecutor.tabExecuter(new CommandContext {
    override def execute(rawCommandContext: RawCommandContext): Unit = {
      val args = rawCommandContext.args
      val sender = rawCommandContext.sender
      if (args.length != 1) return
      args.head.toLowerCase match {
        case "firstjoinitems" =>
          val inv = Bukkit.createInventory(null, 9, "初参加アイテム設定画面")
          implicit val session: AutoSession.type = AutoSession
          val firstJoinItemsTable = sql"SELECT ItemStack FROM firstJoinItems;"
          if (firstJoinItemsTable.getHeadData.nonEmpty) {
            firstJoinItemsTable.foreach(rs => {
              rs.string("ItemStack").split(";").zipWithIndex.foreach { case (itemStackString, index) =>
                val itemStack = Item.getItemStackFromString(itemStackString)
                if (itemStack != null) inv.setItem(index, itemStack)
              }
            })
          }
          sender.asInstanceOf[Player].openInventory(inv)
      }
    }

    override val args: List[String] = List("firstJoinItems")

    override val playerCommand: Boolean = true
  })

}
