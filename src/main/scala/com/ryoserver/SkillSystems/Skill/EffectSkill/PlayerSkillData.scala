package com.ryoserver.SkillSystems.Skill.EffectSkill

import org.bukkit.entity.Player

import scala.collection.mutable

object PlayerSkillData {

  val enableSkills: mutable.Map[String, Array[String]] = mutable.Map.empty //プレイヤー名と有効なスキル名を紐付ける

  def getSkills(p: Player): Unit = {
    enableSkills(p.getName)
  }

}
