package com.ryoserver.Quest

/*
  このcaseClassはクエストのデータ用の型です。
  第1引数にはMaterialNameもしくはEntityTypeNameを入れてください。
 */

case class QuestType(questName: String,
                     questType: String,
                     minLevel: Int,
                     maxLevel: Int,
                     exp: Double,
                     requireList: Map[String, Int],
                    )
