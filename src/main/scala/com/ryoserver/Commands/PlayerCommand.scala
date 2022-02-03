package com.ryoserver.Commands

import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import com.ryoserver.Commands.Executer.Contexts.{CommandContext, RawCommandContext}
import com.ryoserver.Commands.Executer.ContextualTabExecutor
import com.ryoserver.util.SQL
import org.bukkit.Bukkit
import org.bukkit.command.TabExecutor
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player

object PlayerCommand {

  val executor: TabExecutor = ContextualTabExecutor.tabExecuter(new CommandContext {
    override def execute(rawCommandContext: RawCommandContext): Unit = {
      val args = rawCommandContext.args
      val sender = rawCommandContext.sender
      if (args.length != 1) return
      args.head.toLowerCase match {
        case "firstjoinitems" =>
          val sql = new SQL()
          val items = sql.executeQuery("SELECT ItemStack FROM firstJoinItems;")
          val inv = Bukkit.createInventory(null, 9, "初参加アイテム設定画面")
          var counter = 0
          if (items.next()) {
            val invData = items.getString("ItemStack").split(";")
            val config = new YamlConfiguration
            invData.foreach(material => {
              config.loadFromString(material)
              inv.setItem(counter, config.getItemStack("i", null))
              counter += 1
            })
          }
          sender.asInstanceOf[Player].openInventory(inv)
          sql.close()
      }
    }

    override val args: List[String] = List("firstJoinItems")

    override val playerCommand: Boolean = true
  })

}
