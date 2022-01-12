package com.ryoserver.Config

/*
  このCaseClassはconfigの内容を入れる用の型です。
  configにかかれているタグ名と紐づきます。
 */

case class ConfigDataType(host: String,
                          user: String,
                          pw: String,
                          db: String,
                          log: Boolean,
                          per: Double,
                          bigPer: Double,
                          Special: Double,
                          maxLv: Int,
                          tipsTimer: Int,
                          protectionWorlds: List[String],
                          regenerationNormalWorlds: List[String],
                          regenerationNetherWorlds: List[String],
                          regenerationEndWorlds: List[String],
                          gachaChangeRate: Int,
                          webSite: String,
                          dynmap: String,
                          voteSite: List[String],
                          IPHunterAPIKey: String,
                          exclusion: List[String],
                          authority: List[String],
                          ipInfo: Boolean,
                          enableCommands: List[String],
                          worldDoNotProtection: List[String],
                          autoWorldRegeneration: Boolean
                         )
