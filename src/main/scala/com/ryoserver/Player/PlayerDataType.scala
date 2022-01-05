package com.ryoserver.Player

case class PlayerDataType(level: Int,
                          exp: Double,
                          lastDistributionReceived: Int,
                          skillPoint: Double,
                          loginNumber: Int,
                          consecutiveLoginDays: Int,
                          questClearTimes: Int,
                          gachaTickets: Int,
                          gachaPullNumber: Int,
                          SkillOpenPoint: Int,
                          OpenedSkills: Option[String],
                          voteNumber: Int,
                          specialSkillOpenPoint: Int,
                          OpenedSpecialSkills: Option[String],
                          OpenedTitles: Option[String],
                          SelectedTitle: Option[String],
                          autoStack: Boolean,
                          Twitter: Option[String],
                          Discord: Option[String],
                          Word: Option[String]
                     )
