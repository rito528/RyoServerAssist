package com.ryoserver.SkillSystems.SkillPoint

import com.ryoserver.Player.PlayerManager.getPlayerData
import org.bukkit.Bukkit
import org.bukkit.boss.{BarColor, BarStyle}
import org.bukkit.entity.Player

import scala.collection.mutable

object SkillPointBer {

  private var bers: mutable.Map[Player, org.bukkit.boss.BossBar] = mutable.Map.empty

  def create(p: Player): Unit = {
    val maxSkillPoint = new SkillPointCal().getMaxSkillPoint(p.getRyoServerData.level)
    val playerSkillPoint = p.getRyoServerData.skillPoint
    val bossBer = Bukkit.createBossBar("スキルポイント: " + playerSkillPoint, BarColor.WHITE, BarStyle.SOLID)
    bossBer.setProgress(playerSkillPoint / maxSkillPoint.toDouble)
    bossBer.setVisible(true)
    bossBer.addPlayer(p)
    bers += (p -> bossBer)
  }

  def update(p: Player): Unit = {
    val bossBer = bers.get(p)
    val maxSkillPoint = new SkillPointCal().getMaxSkillPoint(p.getRyoServerData.level)
    bossBer.get.setTitle("スキルポイント: " + p.getRyoServerData.skillPoint)
    bossBer.get.setProgress(p.getRyoServerData.skillPoint / maxSkillPoint.toDouble)
  }

  def remove(p: Player): Unit = {
    val ber = bers(p)
    ber.setVisible(false)
    bers = bers.filterNot { case (player, _) => p == player }
  }

}
