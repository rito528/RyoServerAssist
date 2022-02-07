package com.ryoserver.SkillSystems.Skill.EffectSkill

import enumeratum.{Enum, EnumEntry}
import org.bukkit.potion.PotionEffectType

sealed abstract class EffectSkills(val skillName: String,
                                   val cost: Int,
                                   val isSpecialSkill: Boolean,
                                   val effectType: PotionEffectType,
                                   val effectLevel: Int
                                  ) extends EnumEntry

object EffectSkills extends Enum[EffectSkills] {

  override def values: IndexedSeq[EffectSkills] = findValues

  case object nankurunaisa extends EffectSkills(
    "なんくるないさ",
    300,
    isSpecialSkill = false,
    PotionEffectType.DAMAGE_RESISTANCE,
    0
  )

  case object yoidon extends EffectSkills(
    "よーいドン",
    300,
    isSpecialSkill = false,
    PotionEffectType.SPEED,
    0
  )

  case object takaminokenbutu extends EffectSkills(
    "高みの見物",
    300,
    isSpecialSkill = false,
    PotionEffectType.JUMP,
    0
  )

  case object tuyonaru extends EffectSkills(
    "ツヨナール",
    300,
    isSpecialSkill = false,
    PotionEffectType.INCREASE_DAMAGE,
    0
  )

  case object horida extends EffectSkills(
    "ホリダー",
    300,
    isSpecialSkill = false,
    PotionEffectType.FAST_DIGGING,
    0
  )

  case object zikahu extends EffectSkills(
    "ジ・カフ",
    300,
    isSpecialSkill = false,
    PotionEffectType.REGENERATION,
    0
  )

  case object antibekutoru extends EffectSkills(
    "アンチベクトル",
    600,
    isSpecialSkill = true,
    PotionEffectType.SLOW_FALLING,
    0
  )

  case object nekonome extends EffectSkills(
    "猫の目",
    600,
    isSpecialSkill = true,
    PotionEffectType.NIGHT_VISION,
    0
  )

  case object homutekuto extends EffectSkills(
    "ホムテクト",
    600,
    isSpecialSkill = true,
    PotionEffectType.FIRE_RESISTANCE,
    0
  )

  case object mizunokokyuu extends EffectSkills(
    "水の呼吸",
    600,
    isSpecialSkill = true,
    PotionEffectType.WATER_BREATHING,
    0
  )

  case object haganenomentaru extends EffectSkills(
    "鋼のメンタル",
    600,
    isSpecialSkill = true,
    PotionEffectType.DAMAGE_RESISTANCE,
    1
  )

  case object sinsoku extends EffectSkills(
    "神速",
    600,
    isSpecialSkill = true,
    PotionEffectType.SPEED,
    1
  )

  case object pyon extends EffectSkills(
    "ぴょ～ん",
    600,
    isSpecialSkill = true,
    PotionEffectType.JUMP,
    1
  )

  case object mottoyutonaru extends EffectSkills(
    "モットツヨナール",
    600,
    isSpecialSkill = true,
    PotionEffectType.INCREASE_DAMAGE,
    1
  )

  case object saida extends EffectSkills(
    "サイダー",
    600,
    isSpecialSkill = true,
    PotionEffectType.FAST_DIGGING,
    1
  )

  case object tiyunokago extends EffectSkills(
    "治癒の加護",
    600,
    isSpecialSkill = true,
    PotionEffectType.REGENERATION,
    1
  )
}
