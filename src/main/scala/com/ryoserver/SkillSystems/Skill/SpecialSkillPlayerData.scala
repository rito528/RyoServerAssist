package com.ryoserver.SkillSystems.Skill

import com.ryoserver.Player.Data
import com.ryoserver.Player.PlayerManager.{getPlayerData, setPlayerData}
import org.bukkit.ChatColor._
import org.bukkit.entity.Player

import scala.collection.mutable

object SpecialSkillPlayerData {

  private var selectedBreakSkill: mutable.Map[Player, String] = mutable.Map()

  def skillToggle(p: Player, skillName: String): Unit = {
    if (!isSkillOpened(p, skillName) && checkSkillOpen(p, skillName) && p.getSpecialSkillOpenPoint >= 10) {
      skillOpen(p, skillName)
      p.sendMessage(s"$AQUA${skillName}を開放しました。")
      return
    } else if (!isSkillOpened(p, skillName)) {
      p.sendMessage(s"$RED${skillName}を開放できません！")
      return
    }
    if (isActivatedSkill(p, skillName)) {
      skillInvalidation(p, skillName)
    } else if (getActivatedSkill(p).isEmpty) {
      skillActivation(p, skillName)
    } else {
      skillInvalidation(p, getActivatedSkill(p).get)
      skillActivation(p, skillName)
    }
  }

  def checkSkillOpen(p: Player, skillName: String): Boolean = {
    if (SpecialSkillDependency.growSkill.contains(skillName)) {
      SpecialSkillDependency.growSkill.foreach(growSkill => {
        if (growSkill == skillName && !isSkillOpened(p, growSkill)) {
          return true
        } else if (growSkill != skillName && isSkillOpened(p, growSkill)) {
        } else {
          return false
        }
      })
    }
    if (SpecialSkillDependency.harvestSkill.contains(skillName)) {
      SpecialSkillDependency.harvestSkill.foreach(harvestSkill => {
        if (harvestSkill == skillName && !isSkillOpened(p, harvestSkill)) {
          return true
        } else if (harvestSkill != skillName && isSkillOpened(p, harvestSkill)) {
        } else {
          return false
        }
      })
    }
    if (SpecialSkillDependency.breakSkill.contains(skillName)) {
      SpecialSkillDependency.breakSkill.foreach(breakSkill => {
        if (breakSkill == skillName && !isSkillOpened(p, breakSkill)) {
          return true
        } else if (breakSkill != skillName && isSkillOpened(p, breakSkill)) {
        } else {
          return false
        }
      })
    }
    false
  }

  def isSkillOpened(p: Player, skillName: String): Boolean = {
    val openedSkills = p.getOpenedSpecialSkills.orNull
    if (openedSkills != null) openedSkills.contains(skillName) else false
  }

  def skillOpen(p: Player, skillName: String): Unit = {
    val data = Data.playerData(p.getUniqueId)
    var openedSkills = ""
    data.OpenedSpecialSkills match {
      case Some(s) =>
        openedSkills += s + "," + skillName
      case None =>
        openedSkills += skillName
    }
    p.addSpecialSkillOpenPoint(-10)
    p.openSpecialSkills(openedSkills)
  }

  def skillInvalidation(p: Player, skillName: String): Unit = {
    selectedBreakSkill = selectedBreakSkill
      .filterNot { case (player, _) => player == p }
    p.sendMessage(s"${AQUA}スキル: ${skillName}を無効化しました。")
  }

  def skillActivation(p: Player, skillName: String): Unit = {
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
