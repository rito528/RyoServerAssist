package com.ryoserver.SkillSystems.Skill

import org.bukkit.scheduler.BukkitRunnable

import scala.collection.mutable

object SkillData {

  var skillMap:mutable.Map[String,mutable.Map[String,BukkitRunnable]] = mutable.Map.empty

}
