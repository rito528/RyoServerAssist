package com.ryoserver.Player

case class PlayerData(level:Int,
                      exp:Int,
                      skillPoint:Double,
                      ranking:Int,
                      loginNumber:Int,
                      consecutiveLoginDays:Int,
                      questClearTimes:Int,
                      gachaTickets:Int,
                      gachaPullNumber:Int,
                      SkillOpenPoint:Int,
                      OpenedSkills:Option[String],
                      voteNumber:Int,
                      specialSkillOpenPoint:Int,
                      OpenedSpecialSkills:Option[String],
                      OpenedTitles:Option[String],
                      SelectedTitle:Option[String],
                      autoStack:Boolean,
                      Twitter:Option[String],
                      Discord:Option[String],
                      Word:Option[String]
                     )
