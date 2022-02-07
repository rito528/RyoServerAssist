package com.ryoserver.SkillSystems.Skill.EffectSkill

import org.bukkit.entity.Player

import java.util.UUID
import scala.collection.immutable

object EffectSkillData {

  private val enablingSkills: immutable.Map[UUID,EffectSkills] = immutable.Map.empty

  def getEnablingSkill(p:Player): EffectSkills = enablingSkills(p.getUniqueId)

  def setEnablingSkill(p: Player,effectSkills: EffectSkills): Unit = enablingSkills + (p.getUniqueId -> effectSkills)

  def setDisableSkill(p: Player): Unit = enablingSkills - p.getUniqueId

}
