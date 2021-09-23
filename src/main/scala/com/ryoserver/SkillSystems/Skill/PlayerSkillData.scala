package com.ryoserver.SkillSystems.Skill

import org.bukkit.scheduler.BukkitRunnable

import scala.collection.mutable

object PlayerSkillData {

  var skillMap:mutable.Map[String,mutable.Map[String,BukkitRunnable]] = mutable.Map.empty

}
