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
    "血狂の刃" -> tiguruinoyaiba,
    "折れた英雄の剣" -> oretaEiyunoKen,
    "エレメンタルピッケル" -> elementalPickaxe,
    "ブレッシングピッケル" -> blessingPickaxe,
    "ふらっくぴっける" -> flacPickaxe,
    "りょうの剣" -> ryoNoKen,
    "グラム" -> gram,
    "聖剣・天撃" -> seikenTengeki,
    "ブラックアックス" -> blackAxe,
    "ディグニティー" -> dignity,
    "ハーベストスター" -> harvestStar,
    "ハーベストスター・改" -> harvestStarKai,
    "ペンギンの羽" -> penginNoHane,
    "無極の弓-炎-" -> homura,
    "飛炎の弓" -> hienNoYumi,
    "すごい釣り竿・改" -> sugoiTurizaoKai,
    "テクト-兜-" -> kabuto,
    "テクト-鎧-" -> yoroi,
    "テクト-脚-" -> asi,
    "テクト-靴-" -> boots,
    "テクト-兜-・改" -> kabutoKai,
    "テクト-鎧-・改" -> yoroiKai,
    "テクト-脚-・改" -> asiKai,
    "テクト-靴-・改" -> bootsKai,
    "グングニル" -> gungunil,
    "バトルアックス" -> battleAxe,
    "ライフル" -> rifle,
    "木こりの斧" -> kikorinoono,
    "氷結の靴" -> hyouketunokutu,
    "名もなき剣" -> namonakiken,
    "不滅の翼" -> humetunotubasa,
    "勇者の盾" -> yuusyanotate,
    "職人のツルハシ" -> syokuninnnoturuhasi,
    "金運のツルハシ" -> kinnunnnoturuhasi,
    "スキル回復(小)" -> min,
    "スキル回復(中)" -> mid,
    "スキル回復(大)" -> max
  )

  private def all(): Unit = {
    commandList.foreach { case (_, func) => func.apply() }
  }

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

  private def flacPickaxe(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.flacPickaxe)
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

  private def harvestStarKai(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.harvestStarKai)
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

  private def sugoiTurizaoKai(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.sugoiTurizaokai)
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

  private def kabutoKai(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.kabutoKai)
  }

  private def yoroiKai(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.yoroiKai)
  }

  private def asiKai(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.asiKai)
  }

  private def bootsKai(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.bootsKai)
  }

  private def gungunil(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.gungunil)
  }

  private def battleAxe(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.battleAxe)
  }

  private def rifle(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.rifle)
  }

  private def kikorinoono(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.kikorinoOno)
  }

  private def humetunotubasa(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.humetunotubasa)
  }

  private def hyouketunokutu(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.hyouketunokutu)
  }

  private def namonakiken(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.namonakiken)
  }

  private def yuusyanotate(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.yuusyanotate)
  }

  private def syokuninnnoturuhasi(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.syokuninnnoturuhasi)
  }

  private def kinnunnnoturuhasi(): Unit = {
    val p = sender.asInstanceOf[Player]
    p.getWorld.dropItem(p.getLocation, OriginalItems.kinnunnnoturuhasi)
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

}
