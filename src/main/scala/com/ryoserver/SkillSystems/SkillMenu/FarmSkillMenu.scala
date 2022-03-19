package com.ryoserver.SkillSystems.SkillMenu

import com.ryoserver.Menu.Button.{Button, ButtonMotion}
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{Menu, MenuFrame}
import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.Skill.SpecialSkillPlayerData
import com.ryoserver.SkillSystems.Skill.SpecialSkillPlayerData.{getAutoPlaceSeedsStatus, isSkillOpened, skillToggle, toggleDisablingPlaceSeedsPlayer}
import com.ryoserver.util.{Item, ItemStackBuilder}
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player

class FarmSkillMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  override val frame: MenuFrame = MenuFrame(6, "農業系スキル選択")

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    val compute = computeFarmSkillButton(player, ryoServerAssist)
    import compute._
    Map(
      getLayOut(2, 1) -> wingGrow,
      getLayOut(4, 1) -> wideGrow,
      getLayOut(6, 1) -> roundGrow,
      getLayOut(2, 3) -> wingHarvest,
      getLayOut(4, 3) -> wideHarvest,
      getLayOut(6, 3) -> roundHarvest,
      getLayOut(1, 6) -> backPage,
      getLayOut(8, 6) -> clear,
      getLayOut(9, 6) -> autoPlant
    )
  }

}

private case class computeFarmSkillButton(player: Player, ryoServerAssist: RyoServerAssist) {
  lazy val toggle: String => Unit = skillToggle(player, _)
  private implicit val plugin: RyoServerAssist = ryoServerAssist

  val wingGrow: Button = Button(
    ItemStackBuilder
      .getDefault(getIcon("ウインググロー", Material.WOODEN_HOE))
      .title(s"${GREEN}ウインググロー")
      .lore(getGrowSkillLore("ウインググロー", "1*3", 15))
      .build(),
    ButtonMotion { _ =>
      toggle("ウインググロー")
      new FarmSkillMenu(ryoServerAssist).open(player)
    }
  )

  val wideGrow: Button = Button(
    ItemStackBuilder
      .getDefault(getIcon("ワイドグロー", Material.STONE_HOE))
      .title(s"${GREEN}ワイドグロー")
      .lore(getGrowSkillLore("ワイドグロー", "1*5", 30))
      .build(),
    ButtonMotion { _ =>
      toggle("ワイドグロー")
      new FarmSkillMenu(ryoServerAssist).open(player)
    }
  )

  val roundGrow: Button = Button(
    ItemStackBuilder
      .getDefault(getIcon("ラウンドグロー", Material.IRON_HOE))
      .title(s"${GREEN}ラウンドグロー")
      .lore(getGrowSkillLore("ラウンドグロー", "3*3", 55))
      .build(),
    ButtonMotion { _ =>
      toggle("ラウンドグロー")
      new FarmSkillMenu(ryoServerAssist).open(player)
    }
  )

  val wingHarvest: Button = Button(
    ItemStackBuilder
      .getDefault(getIcon("ウイングハーベスト", Material.WOODEN_HOE))
      .title(s"${GREEN}ウイングハーベスト")
      .lore(getHarvestSkillLore("ウイングハーベスト", "1*3", 9))
      .build(),
    ButtonMotion { _ =>
      toggle("ウイングハーベスト")
      new FarmSkillMenu(ryoServerAssist).open(player)
    }
  )

  val wideHarvest: Button = Button(
    ItemStackBuilder
      .getDefault(getIcon("ワイドハーベスト", Material.STONE_HOE))
      .title(s"${GREEN}ワイドハーベスト")
      .lore(getHarvestSkillLore("ワイドハーベスト", "1*5", 15))
      .build(),
    ButtonMotion { _ =>
      toggle("ワイドハーベスト")
      new FarmSkillMenu(ryoServerAssist).open(player)
    }
  )

  val roundHarvest: Button = Button(
    ItemStackBuilder
      .getDefault(getIcon("ラウンドハーベスト", Material.IRON_HOE))
      .title(s"${GREEN}ラウンドハーベスト")
      .lore(getHarvestSkillLore("ラウンドハーベスト", "3*3", 21))
      .build(),
    ButtonMotion { _ =>
      toggle("ラウンドハーベスト")
      new FarmSkillMenu(ryoServerAssist).open(player)
    }
  )

  val backPage: Button = Button(
    ItemStackBuilder
      .getDefault(Material.MAGENTA_GLAZED_TERRACOTTA)
      .title(s"${GREEN}スキルカテゴリ選択画面に戻ります")
      .lore(List(s"${GRAY}クリックで戻ります。"))
      .build(),
    ButtonMotion { _ =>
      new SkillCategoryMenu(ryoServerAssist).open(player)
      new FarmSkillMenu(ryoServerAssist).open(player)
    }
  )

  val clear: Button = Button(
    Item.getPlayerSkull(player, s"${GREEN}スキル選択を解除します。",
      List(s"${GRAY}現在保有中の特殊スキル解放ポイント:" + player.getRyoServerData.specialSkillOpenPoint)),
    ButtonMotion { _ =>
      if (SpecialSkillPlayerData.getActivatedSkill(player).isDefined) {
        SpecialSkillPlayerData.skillInvalidation(player, SpecialSkillPlayerData.getActivatedSkill(player).get)
      }
      player.sendMessage(s"${AQUA}スキル選択を解除しました。")
    }
  )

  val autoPlant: Button = Button(
    ItemStackBuilder
      .getDefault(Material.WHEAT_SEEDS)
      .title(s"${GREEN}自動種植え機能を有効にします。")
      .lore(List(
        s"${GRAY}現在の状態: ${getAutoSeedsPlaceStatus(player)}",
        s"${GRAY}収穫スキルを利用した際に有効になります。"
      ))
      .build(),
    ButtonMotion { _ =>
      toggleSeedsAutoPlace(player)
      new FarmSkillMenu(ryoServerAssist).open(player)
    }
  )

  private def getIcon(skillName: String, openedIcon: Material): Material = {
    if (isSkillOpened(player, skillName)) {
      openedIcon
    } else {
      Material.BEDROCK
    }
  }

  private def getGrowSkillLore(skillName: String, range: String, skillPoint: Int): List[String] = {
    List(s"$GRAY${range}の範囲の作物を成長させます。"
      , s"${GRAY}消費スキルポイント:$skillPoint"
      , s"$GRAY ${if (isSkillOpened(player, skillName)) "クリックで選択します。" else "クリックで開放します。"}"
      , s"${if (!isSkillOpened(player, skillName)) s"$GRAY[解放条件]" else ""}"
      , s"${if (!isSkillOpened(player, skillName)) s"$GRAY・特殊スキル解放ポイントを10ポイント消費" else ""}"
      , s"${if (!isSkillOpened(player, skillName) && skillName != "ウインググロー") s"$GRAY・下位スキルをすべて開放" else ""}")
      .filterNot(_ == "")
  }

  private def getHarvestSkillLore(skillName: String, range: String, skillPoint: Int): List[String] = {
    List(s"$GRAY${range}の範囲の作物を収穫します。"
      , s"${GRAY}消費スキルポイント:$skillPoint"
      , s"${GRAY}種植え機能を利用した場合は収穫したブロック分だけ"
      , s"${GRAY}スキルポイントを追加で消費します。"
      , s"$GRAY ${if (isSkillOpened(player, skillName)) "クリックで選択します。" else "クリックで開放します。"}"
      , s"${if (!isSkillOpened(player, skillName)) s"$GRAY[解放条件]" else ""}"
      , s"${if (!isSkillOpened(player, skillName)) s"$GRAY・特殊スキル解放ポイントを10ポイント消費" else ""}"
      , s"${if (!isSkillOpened(player, skillName) && skillName != "ウイングハーベスト") s"$GRAY・下位スキルをすべて開放" else ""}")
      .filterNot(_ == "")
  }

  private def getAutoSeedsPlaceStatus(p: Player): String = {
    if (getAutoPlaceSeedsStatus(p.getUniqueId)) {
      "有効"
    } else {
      "無効"
    }
  }

  private def toggleSeedsAutoPlace(p: Player): Unit = {
    toggleDisablingPlaceSeedsPlayer(p.getUniqueId)
  }

}
