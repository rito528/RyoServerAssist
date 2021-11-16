package com.ryoserver.Title

import com.ryoserver.Menu.MenuLayout.{getX, getY}
import com.ryoserver.Menu.{RyoServerMenu1, Menu}
import com.ryoserver.Player.Name
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.Skill.SkillData
import org.bukkit.ChatColor._
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.{ChatColor, Material}

import java.nio.file.Paths

class TitleMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  var name: String = _
  val slot: Int = 6
  var p: Player = _

  def openInv(player: Player, selectPage: Int): Unit = {
    p = player
    val hasTitles = new PlayerTitleData(ryoServerAssist).getHasTitles(p.getUniqueId.toString)
    val titleConfig = YamlConfiguration.loadConfiguration(Paths.get("plugins/RyoServerAssist/title.yml").toFile)
    var index = 0
    var page = selectPage
    val maxPageNumber = (titleConfig.getConfigurationSection("titles").getKeys(false).size() / 45) + 1
    if (page >= maxPageNumber) page = maxPageNumber
    name = "称号一覧:" + page
    titleConfig.getConfigurationSection("titles").getKeys(false).forEach(title => {
      if (page * 45 >= index && (page - 1) * 45 <= index) {
        val isHasTitle = hasTitles.contains(title)
        if (titleConfig.getBoolean(s"titles.$title.secret") && !isHasTitle) {
          setItem(getX(index), getY(index), Material.BEDROCK, effect = false, s"$GREEN???", List("解放条件:???"))
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
                lore = lore :+ (GRAY + "・" + SkillData.SkillNames(condition))
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
          setItem(getX(index), getY(index), if (isHasTitle) Material.NAME_TAG else Material.BEDROCK, effect = false, title, lore)
        }
      }
      index += 1
    })
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
      new PlayerTitleData(ryoServerAssist).resetSelectTitle(p.getUniqueId.toString)
      new Name(ryoServerAssist).updateName(p)
      p.sendMessage(ChatColor.AQUA + "称号をリセットしました。")
    } else if (index == 53) {
      new TitleMenu(ryoServerAssist).openInv(p, page + 1)
    } else if (0 <= index && 44 >= index && inv.get.getItem(index) != null && inv.get.getItem(index).getType == Material.NAME_TAG) {
      /*
       解放済みの称号
       */
      val titleName = ChatColor.RESET + inv.get.getItem(index).getItemMeta.getDisplayName
      new PlayerTitleData(ryoServerAssist).setSelectTitle(p.getUniqueId.toString, titleName)
      new Name(ryoServerAssist).updateName(p)
      p.sendMessage(ChatColor.AQUA + "称号: 「" + titleName + s"${ChatColor.AQUA}」を設定しました！")
    }
  }

}
