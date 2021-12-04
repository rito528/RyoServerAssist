package com.ryoserver.Commands

import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import com.ryoserver.OriginalItem.OriginalItems
import com.ryoserver.SkillSystems.SkillPoint.RecoveryItems
import org.bukkit.entity.Player

class OriginalItemCommand extends CommandBuilder {

  private def tiguruinoyaiba(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.tiguruinoyaiba)
  }

  private def oretaEiyunoKen(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.oretaEiyuNoKen)
  }

  private def elementalPickaxe(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.elementalPickaxe)
  }

  private def blessingPickaxe(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.blessingPickaxe)
  }

  private def ryoNoKen(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.ryoNoKen)
  }

  private def gram(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.gram)
  }

  private def seikenTengeki(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.seikenTengeki)
  }

  private def blackAxe(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.blackAxe)
  }

  private def dignity(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.dignity)
  }

  private def harvestStar(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.harvestStar)
  }

  private def penginNoHane(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.penginNoHane)
  }

  private def homura(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.homura)
  }

  private def hienNoYumi(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.hienNoYumi)
  }

  private def kabuto(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.kabuto)
  }

  private def yoroi(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.yoroi)
  }

  private def asi(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.asi)
  }

  private def boots(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.boots)
  }

  private def min(): Unit = {
    sender.asInstanceOf[Player].getWorld.dropItemNaturally(sender.asInstanceOf[Player].getLocation, RecoveryItems.min)
  }

  private def max(): Unit = {
    sender.asInstanceOf[Player].getWorld.dropItemNaturally(sender.asInstanceOf[Player].getLocation, RecoveryItems.max)
  }

  override val executor: CommandExecutorBuilder = CommandExecutorBuilder(
    Map(
      "血狂の刃" -> tiguruinoyaiba,
      "折れた英雄の剣" -> oretaEiyunoKen,
      "エレメンタルピッケル" -> elementalPickaxe,
      "ブレッシングピッケル" -> blessingPickaxe,
      "りょうの剣" -> ryoNoKen,
      "グラム" -> gram,
      "聖剣・天撃" -> seikenTengeki,
      "ブラックアックス" -> blackAxe,
      "ディグニティー" -> dignity,
      "ハーベストスター" -> harvestStar,
      "ペンギンの羽" -> penginNoHane,
      "無極の弓-炎-" -> homura,
      "飛炎の弓" -> hienNoYumi,
      "テクト-兜-" -> kabuto,
      "テクト-鎧-" -> yoroi,
      "テクト-脚-" -> asi,
      "テクト-靴-" -> boots,
      "スキル回復(小)" -> min,
      "スキル回復(大)" -> max
    )
  ).playerCommand()

}
