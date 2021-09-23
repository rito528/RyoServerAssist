package com.ryoserver.SkillSystems.SkillPoint

import com.ryoserver.Level.Player.getPlayerData
import com.ryoserver.RyoServerAssist
import org.bukkit.Bukkit
import org.bukkit.boss.{BarColor, BarStyle}
import org.bukkit.entity.Player

import scala.collection.mutable

object SkillPointBer {

  private var bers: mutable.Map[Player, org.bukkit.boss.BossBar] = mutable.Map.empty

  def create(p:Player,ryoServerAssist: RyoServerAssist): Unit = {
    val maxSkillPoint = new SkillPointCal().getMaxSkillPoint(new getPlayerData(ryoServerAssist).getPlayerLevel(p))
    val playerSkillPoint = new SkillPointData(ryoServerAssist).getSkillPoint(p)
    val bossBer = Bukkit.createBossBar("スキルポイント:" + playerSkillPoint , BarColor.WHITE, BarStyle.SOLID)
    bossBer.setProgress(new SkillPointData(ryoServerAssist).getSkillPoint(p).toDouble / maxSkillPoint.toDouble)
    bossBer.setVisible(true)
    bossBer.addPlayer(p)
    bers += (p -> bossBer)
  }

  def update(p:Player,ryoServerAssist: RyoServerAssist): Unit = {
    val bossBer = bers.get(p)
    val maxSkillPoint = new SkillPointCal().getMaxSkillPoint(new getPlayerData(ryoServerAssist).getPlayerLevel(p))
    bossBer.get.setTitle("スキルポイント:" + new SkillPointData(ryoServerAssist).getSkillPoint(p))
    bossBer.get.setProgress(new SkillPointData(ryoServerAssist).getSkillPoint(p).toDouble / maxSkillPoint.toDouble)
  }

  def remove(p:Player): Unit = {
    val ber = bers(p)
    ber.setVisible(false)
    bers = bers.filterNot{case (player,_) => p == player}
  }

}
