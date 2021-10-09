package com.ryoserver.Title

import com.ryoserver.Inventory.Item.getItem
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.Skill.SkillData
import org.bukkit.{Bukkit, Material}
import org.bukkit.ChatColor._
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player

import scala.jdk.CollectionConverters._
import java.nio.file.Paths

class TitleInventory(ryoServerAssist: RyoServerAssist) {

  def openInv(p:Player): Unit = {
    val inv = Bukkit.createInventory(null,54,"称号一覧")
    val hasTitles = new PlayerTitleData(ryoServerAssist).getHasTitles(p.getUniqueId.toString)
    val titleConfig = YamlConfiguration.loadConfiguration(Paths.get("plugins/RyoServerAssist/title.yml").toFile)
    var index = 0
    titleConfig.getConfigurationSection("titles").getKeys(false).forEach(title => {
      val isHasTitle = hasTitles.contains(title)
      if (titleConfig.getBoolean(s"titles.$title.secret") && !isHasTitle) {
        inv.setItem(index,getItem(Material.BEDROCK,s"${GREEN}???",List("解放条件:???").asJava))
      } else {
        var lore:List[String] = List.empty
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
        }
        inv.setItem(index,getItem(if (isHasTitle) Material.NAME_TAG else Material.BEDROCK,GREEN + title,lore.asJava))
      }
      index += 1
    })
    inv.setItem(45,getItem(Material.MAGENTA_GLAZED_TERRACOTTA,s"${GREEN}メニューに戻る",List(s"${GRAY}クリックでメニューに戻ります。").asJava))
    p.openInventory(inv)
  }

}
