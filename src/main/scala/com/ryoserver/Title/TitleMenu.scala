package com.ryoserver.Title

import com.ryoserver.Menu.MenuLayout.{getLayOut, getX, getY}
import com.ryoserver.Menu.{Menu, RyoServerMenu1}
import com.ryoserver.Player.Name
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.Skill.EffectSkill.SkillData
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player

import java.nio.file.Paths
import scala.jdk.CollectionConverters.CollectionHasAsScala

class TitleMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  val slot: Int = 6
  var name: String = _
  var p: Player = _

  def openInv(player: Player, selectPage: Int): Unit = {
    p = player
    val hasTitles = new PlayerTitleData(ryoServerAssist).getHasTitles(p.getUniqueId)
    val titleConfig = YamlConfiguration.loadConfiguration(Paths.get("plugins/RyoServerAssist/title.yml").toFile)
    name = "称号一覧:" + selectPage
    var invIndex = 0
    titleConfig.getConfigurationSection("titles").getKeys(false).asScala.zipWithIndex.foreach{case (title,index) =>
      if (index < (getLayOut(9, 5) + 1) * selectPage && (getLayOut(9, 5) + 1) * (selectPage - 1) <= index) {
        val isHasTitle = hasTitles.contains(title)
        if (titleConfig.getBoolean(s"titles.$title.secret") && !isHasTitle) {
          setItem(getX(invIndex), getY(invIndex), Material.BEDROCK, effect = false, s"$GREEN???", List("解放条件:???"))
        } else {
          var lore: List[String] = List.empty
          val configCondition = titleConfig.getInt(s"titles.$title.condition")
          titleConfig.getString(s"titles.$title.type").toLowerCase() match {
            case "lv" =>
              lore = List(s"${GRAY}解放条件:Lv.${configCondition}になろう。")
            case "continuouslogin" =>
              lore = List(s"${GRAY}解放条件:${configCondition}日間連続でログインしよう。")
            case "logindays" =>
              lore = List(s"${GRAY}解放条件:${configCondition}日間ログインしよう。")
            case "questclearnumber" =>
              lore = List(s"${GRAY}解放条件:${configCondition}回クエストをクリアしよう。")
            case "gachanumber" =>
              lore = List(s"${GRAY}解放条件:${configCondition}回ガチャを引こう。")
            case "skillopen" =>
              lore = List(s"${GRAY}解放条件:以下のスキルを開放しよう。")
              titleConfig.getIntegerList(s"titles.$title.condition").forEach(condition => {
                lore = lore :+ s"$GRAY・${SkillData.SkillNames(condition)}"
              })
            case "loginyear" =>
              lore = List(s"${GRAY}解放条件:${configCondition}年にログインしよう。")
            case "loginperiod" =>
              lore = List(s"${GRAY}解放条件:${titleConfig.getString(s"titles.$title.start")}〜${titleConfig.getString(s"titles.$title.end")}までにログインしよう。")
            case "loginday" =>
              lore = List(s"${GRAY}解放条件:${titleConfig.getString(s"titles.$title.condition")}にログインしよう。")
            case "titlegetnumber" =>
              lore = List(s"${GRAY}解放条件:${configCondition}回称号を解禁しよう。")
            case "continuousloginandquestclearnumber" =>
              lore = List(s"${GRAY}解放条件:${titleConfig.getString(s"titles.$title.condition").split(",")(0)}日ログインと、" +
                s"${titleConfig.getString(s"titles.$title.condition").split(",")(1)}回クエストをクリアしよう。")
          }
          setItem(getX(invIndex), getY(invIndex), if (isHasTitle) Material.NAME_TAG else Material.BEDROCK, effect = false, title, lore)
        }
        invIndex += 1
      }
    }
    setItem(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, effect = false, s"${GREEN}メニューに戻る", List(s"${GRAY}クリックでメニューに戻ります。"))
    setItem(5, 6, Material.PAPER, effect = false, s"${GREEN}称号の設定をリセットします。", List(s"${GRAY}クリックでリセットします。"))
    setItem(9, 6, Material.MAGENTA_GLAZED_TERRACOTTA, effect = false, s"${GREEN}次のページへ移動", List(s"${GRAY}クリックで次のページに移動します。"))
    registerMotion(motion)
    open()
  }

  def motion(p: Player, index: Int): Unit = {
    val page = name.split(":")(1).toInt
    if (index == 45) {
      new RyoServerMenu1(ryoServerAssist).menu(p)
    } else if (index == 49) {
      new PlayerTitleData(ryoServerAssist).resetSelectTitle(p.getUniqueId)
      new Name(ryoServerAssist).updateName(p)
      p.sendMessage(s"${AQUA}称号をリセットしました。")
    } else if (index == 53) {
      new TitleMenu(ryoServerAssist).openInv(p, page + 1)
    } else if (0 <= index && 44 >= index && inv.get.getItem(index) != null && inv.get.getItem(index).getType == Material.NAME_TAG) {
      /*
       解放済みの称号
       */
      val titleName = inv.get.getItem(index).getItemMeta.getDisplayName
      new PlayerTitleData(ryoServerAssist).setSelectTitle(p.getUniqueId, titleName)
      new Name(ryoServerAssist).updateName(p)
      p.sendMessage(s"${AQUA}称号: 「$RESET$titleName$AQUA」を設定しました！")
    }
  }

}
