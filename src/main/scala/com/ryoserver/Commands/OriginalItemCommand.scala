package com.ryoserver.Commands

import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import com.ryoserver.OriginalItem.OriginalItems
import org.bukkit.entity.Player

class OriginalItemCommand extends CommandBuilder {

  def tiguruinoyaiba(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.tiguruinoyaiba)
  }

  def oretaEiyunoKen(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.oretaEiyuNoKen)
  }

  def elementalPickaxe(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.elementalPickaxe)
  }

  def blessingPickaxe(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.blessingPickaxe)
  }

  def ryoNoKen(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.ryoNoKen)
  }

  def gram(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.gram)
  }

  def seikenTengeki(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.seikenTengeki)
  }

  def blackAxe(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.blackAxe)
  }

  def dignity(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.dignity)
  }

  def harvestStar(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.harvestStar)
  }

  def penginNoHane(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.penginNoHane)
  }

  def homura(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.homura)
  }

  def hienNoYumi(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.hienNoYumi)
  }

  def kabuto(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.kabuto)
  }

  def yoroi(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.yoroi)
  }

  def asi(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.asi)
  }

  def boots(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.boots)
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
      "テクト-靴-" -> boots
    )
  ).playerCommand()

}
