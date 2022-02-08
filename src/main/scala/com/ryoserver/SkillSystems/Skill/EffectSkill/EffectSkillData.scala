package com.ryoserver.SkillSystems.Skill.EffectSkill

import org.bukkit.entity.Player

import java.util.UUID
import scala.collection.mutable

object EffectSkillData {

  private val enablingSkills: mutable.Map[UUID,Set[EffectSkills]] = mutable.Map.empty

  def getEnablingSkill(p:Player): Set[EffectSkills] = enablingSkills(p.getUniqueId)

  def setEnablingSkill(p: Player,effectSkills: EffectSkills): Unit = {
    val uuid = p.getUniqueId
    enablingSkills += (uuid -> (if (enablingSkills.contains(uuid)) enablingSkills(uuid) ++ Set(effectSkills) else Set(effectSkills)))
  }

  def setDisableSkill(p: Player,effectSkills: EffectSkills): Unit = {
    val uuid = p.getUniqueId
    enablingSkills += (uuid -> enablingSkills(uuid).filterNot(_ == effectSkills))
  }

}
