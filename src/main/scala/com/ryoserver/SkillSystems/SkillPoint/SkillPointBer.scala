package com.ryoserver.SkillSystems.SkillPoint

import com.ryoserver.Level.Player.GetPlayerData
import com.ryoserver.Player.Data
import com.ryoserver.RyoServerAssist
import org.bukkit.Bukkit
import org.bukkit.boss.{BarColor, BarStyle}
import org.bukkit.entity.Player

import scala.collection.mutable

object SkillPointBer {

  private var bers: mutable.Map[Player, org.bukkit.boss.BossBar] = mutable.Map.empty

  def create(p: Player): Unit = {
    val maxSkillPoint = new SkillPointCal().getMaxSkillPoint(new GetPlayerData().getPlayerLevel(p))
    val playerSkillPoint = new SkillPointData().getSkillPoint(p)
    val bossBer = Bukkit.createBossBar("スキルポイント: " + playerSkillPoint, BarColor.WHITE, BarStyle.SOLID)
    bossBer.setProgress(new SkillPointData().getSkillPoint(p) / maxSkillPoint.toDouble)
    bossBer.setVisible(true)
    bossBer.addPlayer(p)
    bers += (p -> bossBer)
  }

  def update(p: Player): Unit = {
    val bossBer = bers.get(p)
    val maxSkillPoint = new SkillPointCal().getMaxSkillPoint(Data.playerData(p.getUniqueId).level)
    bossBer.get.setTitle("スキルポイント: " + new SkillPointData().getSkillPoint(p))
    bossBer.get.setProgress(new SkillPointData().getSkillPoint(p) / maxSkillPoint.toDouble)
  }

  def remove(p: Player): Unit = {
    val ber = bers(p)
    ber.setVisible(false)
    bers = bers.filterNot { case (player, _) => p == player }
  }

}
