package com.ryoserver.Title

import com.ryoserver.Menu.Button.{Button, ButtonMotion}
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{Menu, MenuFrame}
import com.ryoserver.Player.Name
import com.ryoserver.RyoServerAssist
import com.ryoserver.RyoServerMenu.RyoServerMenu1
import com.ryoserver.util.ItemStackBuilder
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player

import java.nio.file.Paths
import scala.jdk.CollectionConverters.CollectionHasAsScala

class TitleMenu(page:Int, ryoServerAssist: RyoServerAssist) extends Menu {

  override val frame: MenuFrame = MenuFrame(6,s"称号一覧$page")

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    val hasTitles = new PlayerTitleData().getHasTitles(player.getUniqueId)
    val titleConfig = YamlConfiguration.loadConfiguration(Paths.get("plugins/RyoServerAssist/title.yml").toFile)
    val compute = computeTitleMenuButton(player,page, ryoServerAssist)
    import compute._
    Map(
      getLayOut(1,6) -> backPage,
      getLayOut(5,6) -> reset,
      getLayOut(9,6) -> nextPage
    ) ++
    titleConfig.getConfigurationSection("titles").getKeys(false).asScala.zipWithIndex.filter{case (_,index) =>
      index < (getLayOut(9, 5) + 1) * this.page && (getLayOut(9, 5) + 1) * (this.page - 1) <= index}
      .map { case (title, index) =>
        val isHasTitle = hasTitles.contains(title)
        if (titleConfig.getBoolean(s"titles.$title.secret") && !isHasTitle) {
          index - ((getLayOut(9, 5) + 1) * (this.page - 1)) -> titleButton(title,List(s"${GRAY}解放条件:???"))
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
          index - ((getLayOut(9, 5) + 1) * (this.page - 1)) -> titleButton(title, lore)
        }
      }
    }

}

private case class computeTitleMenuButton(player:Player,page:Int,ryoServerAssist: RyoServerAssist) {
  val backPage: Button = Button(
    ItemStackBuilder
      .getDefault(Material.MAGENTA_GLAZED_TERRACOTTA)
      .title(s"$GREEN${if (page == 1) "メニューに戻る" else "前のページに戻る"}")
      .lore(List(s"${GRAY}クリックで戻ります。"))
      .build(),
    ButtonMotion{_ =>
      if (page == 1) new RyoServerMenu1(ryoServerAssist).open(player)
      else new TitleMenu(page - 1,ryoServerAssist).open(player)
    }
  )

  val nextPage: Button = Button(
    ItemStackBuilder
      .getDefault(Material.MAGENTA_GLAZED_TERRACOTTA)
      .title(s"${GREEN}次のページに移動します。")
      .lore(List(s"${GRAY}クリックで次のページに移動します。"))
      .build(),
    ButtonMotion{_ =>
      new TitleMenu(page + 1,ryoServerAssist).open(player)
    }
  )

  val reset: Button = Button(
    ItemStackBuilder
      .getDefault(Material.PAPER)
      .title(s"${GREEN}称号の設定をリセットします。")
      .lore(List(s"${GRAY}クリックでリセットします。"))
      .build(),
    ButtonMotion{_ =>
      new PlayerTitleData().resetSelectTitle(player.getUniqueId)
      new Name().updateName(player)
      player.sendMessage(s"${AQUA}称号をリセットしました。")
    }
  )

  def titleButton(title: String,lore:List[String]): Button = {
    val hasTitles = new PlayerTitleData().getHasTitles(player.getUniqueId)
    Button(
      ItemStackBuilder
        .getDefault(if (hasTitles.contains(title)) Material.NAME_TAG else Material.BEDROCK)
        .title(s"$RESET$title")
        .lore(lore)
        .build(),
      ButtonMotion{_ =>
        if (new PlayerTitleData().getHasTitles(player.getUniqueId).contains(title)) {
          new PlayerTitleData().setSelectTitle(player.getUniqueId, title)
          new Name().updateName(player)
          player.sendMessage(s"${AQUA}称号: 「$RESET$title$AQUA」を設定しました！")
        } else {
          player.sendMessage(s"${RED}この称号を持っていないため設定できません！")
        }
      }
    )
  }
}
