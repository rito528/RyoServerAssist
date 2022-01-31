package com.ryoserver.Commands

import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import com.ryoserver.OriginalItem.OriginalItems
import com.ryoserver.SkillSystems.SkillPoint.RecoveryItems
import org.bukkit.entity.Player

class OriginalItemCommand extends CommandBuilder {

  override val executor: CommandExecutorBuilder = CommandExecutorBuilder(
    commandList ++
      Map("all" -> all)
  ).playerCommand()
  private val commandList: Map[String, () => Unit] = Map(
    "勇者の盾" -> yuusyanotate,
    "スキル回復(小)" -> min,
    "スキル回復(中)" -> mid,
    "スキル回復(大)" -> max,
    "特等交換券" -> tokutoukoukanken
  )

  private def all(): Unit = {
    commandList.foreach { case (_, func) => func.apply() }
  }

  private def yuusyanotate(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.yuusyanotate)
  }

  private def min(): Unit = {
    sender.asInstanceOf[Player].getWorld.dropItemNaturally(sender.asInstanceOf[Player].getLocation, RecoveryItems.min)
  }

  private def mid(): Unit = {
    sender.asInstanceOf[Player].getWorld.dropItemNaturally(sender.asInstanceOf[Player].getLocation, RecoveryItems.mid)
  }

  private def max(): Unit = {
    sender.asInstanceOf[Player].getWorld.dropItemNaturally(sender.asInstanceOf[Player].getLocation, RecoveryItems.max)
  }

  private def tokutoukoukanken(): Unit = {
    sender.asInstanceOf[Player].getWorld.dropItemNaturally(sender.asInstanceOf[Player].getLocation, OriginalItems.tokutoukoukanken)
  }

}
