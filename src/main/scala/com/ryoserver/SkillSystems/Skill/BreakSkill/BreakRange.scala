package com.ryoserver.SkillSystems.Skill.BreakSkill

/*
  破壊範囲の縦、横、奥行きを指定する。
  奥行きを指定しない場合は0を入力してください。
 */
final case class BreakRange(height: Int, width: Int, depth: Int)
