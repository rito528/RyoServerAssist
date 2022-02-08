package com.ryoserver.Title

import com.ryoserver.Menu.MenuLayout.{getLayOut, getX, getY}
import com.ryoserver.Menu.{Menu, MenuButton, RyoServerMenu1}
import com.ryoserver.Player.Name
import com.ryoserver.RyoServerAssist
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
    val hasTitles = new PlayerTitleData().getHasTitles(p.getUniqueId)
    val titleConfig = YamlConfiguration.loadConfiguration(Paths.get("plugins/RyoServerAssist/title.yml").toFile)
    name = "称号一覧:" + selectPage
    var invIndex = 0
    titleConfig.getConfigurationSection("titles").getKeys(false).asScala.zipWithIndex.foreach { case (title, index) =>
      if (index < (getLayOut(9, 5) + 1) * selectPage && (getLayOut(9, 5) + 1) * (selectPage - 1) <= index) {
        val isHasTitle = hasTitles.contains(title)
        if (titleConfig.getBoolean(s"titles.$title.secret") && !isHasTitle) {
          setButton(MenuButton(getX(invIndex), getY(invIndex), Material.BEDROCK, s"$GREEN???", List("解放条件:???"))
            .setLeftClickMotion(setTitle(_, index - ((getLayOut(9, 5) + 1) * (selectPage - 1)))))
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
              titleConfig.getStringList(s"titles.$title.condition").forEach(condition => {
                lore = lore :+ s"$GRAY・$condition"
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
          setButton(MenuButton(getX(invIndex), getY(invIndex), if (isHasTitle) Material.NAME_TAG else Material.BEDROCK, title, lore)
            .setLeftClickMotion(setTitle(_, index - ((getLayOut(9, 5) + 1) * (selectPage - 1)))))
        }
        invIndex += 1
      }
    }
    setButton(MenuButton(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, s"${GREEN}メニューに戻る", List(s"${GRAY}クリックでメニューに戻ります。"))
      .setLeftClickMotion(backPage))
    setButton(MenuButton(5, 6, Material.PAPER, s"${GREEN}称号の設定をリセットします。", List(s"${GRAY}クリックでリセットします。"))
      .setLeftClickMotion(reset))
    setButton(MenuButton(9, 6, Material.MAGENTA_GLAZED_TERRACOTTA, s"${GREEN}次のページへ移動", List(s"${GRAY}クリックで次のページに移動します。"))
      .setLeftClickMotion(nextPage))
    build(new TitleMenu(ryoServerAssist).openInv(_, 1))
    open()
  }

  private def backPage(p: Player): Unit = {
    new RyoServerMenu1(ryoServerAssist).menu(p)
  }

  private def nextPage(p: Player): Unit = {
    val page = name.split(":")(1).toInt
    new TitleMenu(ryoServerAssist).openInv(p, page + 1)
  }

  private def setTitle(p: Player, index: Int): Unit = {
    val titleName = inv.get.getItem(index).getItemMeta.getDisplayName
    if (new PlayerTitleData().getHasTitles(p.getUniqueId).contains(titleName)) {
      new PlayerTitleData().setSelectTitle(p.getUniqueId, titleName)
      new Name().updateName(p)
      p.sendMessage(s"${AQUA}称号: 「$RESET$titleName$AQUA」を設定しました！")
    } else {
      p.sendMessage(s"${RED}この称号を持っていないため設定できません！")
    }
  }

  private def reset(p: Player): Unit = {
    new PlayerTitleData().resetSelectTitle(p.getUniqueId)
    new Name().updateName(p)
    p.sendMessage(s"${AQUA}称号をリセットしました。")
  }

}
