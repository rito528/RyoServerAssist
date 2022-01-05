package com.ryoserver.SkillSystems.SkillPoint

import com.ryoserver.Player.Data
import com.ryoserver.Player.PlayerManager.getPlayerData
import org.bukkit.Bukkit
import org.bukkit.boss.{BarColor, BarStyle}
import org.bukkit.entity.Player

import scala.collection.mutable

object SkillPointBer {

  private var bers: mutable.Map[Player, org.bukkit.boss.BossBar] = mutable.Map.empty

  def create(p: Player): Unit = {
    val maxSkillPoint = new SkillPointCal().getMaxSkillPoint(p.getQuestLevel)
    val playerSkillPoint = p.getSkillPoint
    val bossBer = Bukkit.createBossBar("スキルポイント: " + playerSkillPoint, BarColor.WHITE, BarStyle.SOLID)
    bossBer.setProgress(playerSkillPoint / maxSkillPoint.toDouble)
    bossBer.setVisible(true)
    bossBer.addPlayer(p)
    bers += (p -> bossBer)
  }

  def update(p: Player): Unit = {
    val bossBer = bers.get(p)
    val maxSkillPoint = new SkillPointCal().getMaxSkillPoint(p.getQuestLevel)
    bossBer.get.setTitle("スキルポイント: " + p.getSkillPoint)
    bossBer.get.setProgress(p.getSkillPoint / maxSkillPoint.toDouble)
  }

  def remove(p: Player): Unit = {
    val ber = bers(p)
    ber.setVisible(false)
    bers = bers.filterNot { case (player, _) => p == player }
  }

}
