package com.ryoserver.SkillSystems.Skill

import org.bukkit.ChatColor.AQUA
import org.bukkit.entity.Player

import scala.collection.mutable

object SpecialSkillPlayerData {

  private var selectedBreakSkill: mutable.Map[Player, String] = mutable.Map()

  def breakSkillToggle(p: Player, skillName: String): Unit = {
    if (isActivatedSkill(p, skillName)) {
      breakSkillInvalidation(p, skillName)
    } else if (getActivatedSkill(p).isEmpty) {
      breakSkillActivation(p, skillName)
    } else {
      breakSkillInvalidation(p, getActivatedSkill(p).get)
      breakSkillActivation(p, skillName)
    }
  }

  def breakSkillInvalidation(p: Player, skillName: String): Unit = {
    selectedBreakSkill = selectedBreakSkill
      .filterNot { case (player, _) => player == p }
    p.sendMessage(s"${AQUA}スキル: ${skillName}を無効化しました。")
  }

  def breakSkillActivation(p: Player, skillName: String): Unit = {
    selectedBreakSkill += (p -> skillName)
    p.sendMessage(s"${AQUA}スキル: ${skillName}を有効化しました。")
  }

  def isActivatedSkill(p: Player, skillName: String): Boolean = {
    selectedBreakSkill.contains(p) && selectedBreakSkill(p) == skillName
  }

  def getActivatedSkill(p: Player): Option[String] = {
    if (selectedBreakSkill.contains(p)) Option(selectedBreakSkill(p))
    else None
  }

}
