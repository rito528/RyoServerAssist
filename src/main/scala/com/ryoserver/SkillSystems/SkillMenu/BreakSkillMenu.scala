package com.ryoserver.SkillSystems.SkillMenu

import com.ryoserver.Menu.Button.{Button, ButtonMotion}
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{Menu, MenuButton, MenuFrame, MenuSkull}
import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.Skill.SpecialSkillPlayerData
import com.ryoserver.SkillSystems.Skill.SpecialSkillPlayerData.{isSkillOpened, skillToggle}
import com.ryoserver.util.{Item, ItemStackBuilder}
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player

class BreakSkillMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  override val frame: MenuFrame = MenuFrame(6,"破壊系スキル選択")

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    val compute = computeBreakSkillButton(player, ryoServerAssist)
    import compute._
    Map(
      getLayOut(2,1) -> breakDuo,
      getLayOut(4,1) -> upDownBreak,
      getLayOut(6,1) -> pantingBreak,
      getLayOut(8,1) -> tunnelBreak,
      getLayOut(2,2) -> wideBreak,
      getLayOut(1,6) -> backPage,
      getLayOut(9,6) -> clear
    )
  }

}

private case class computeBreakSkillButton(player: Player,ryoServerAssist: RyoServerAssist) {
  private implicit val plugin: RyoServerAssist = ryoServerAssist
  val breakDuo: Button = Button(
    ItemStackBuilder
      .getDefault(getIcon("ブレイク・デュオ",Material.WOODEN_PICKAXE))
      .title(s"${GREEN}ブレイク・デュオ")
      .lore(getLore("ブレイク・デュオ","1*2",6))
      .build(),
    ButtonMotion{_ =>
      skillToggle(player, "ブレイク・デュオ")
    }
  )

  val upDownBreak: Button = Button(
    ItemStackBuilder
      .getDefault(getIcon("アップダウンブレイク",Material.STONE_PICKAXE))
      .title(s"${GREEN}アップダウンブレイク")
      .lore(getLore("アップダウンブレイク","1*3",9))
      .build(),
    ButtonMotion{_ =>
      skillToggle(player, "アップダウンブレイク")
    }
  )

  val pantingBreak: Button = Button(
    ItemStackBuilder
      .getDefault(getIcon("パンチングブレイク",Material.IRON_PICKAXE))
      .title(s"${GREEN}パンチングブレイク")
      .lore(getLore("パンチングブレイク","3*2",18))
      .build(),
    ButtonMotion{_ =>
      skillToggle(player, "パンチングブレイク")
    }
  )

  val tunnelBreak: Button = Button(
    ItemStackBuilder
      .getDefault(getIcon("トンネルブレイク",Material.GOLDEN_PICKAXE))
      .title(s"${GREEN}トンネルブレイク")
      .lore(getLore("トンネルブレイク","3*3",27))
      .build(),
    ButtonMotion{_ =>
      skillToggle(player, "トンネルブレイク")
    }
  )

  val wideBreak: Button = Button(
    ItemStackBuilder
      .getDefault(getIcon("ワイドブレイク",Material.GOLDEN_PICKAXE))
      .title(s"${GREEN}ワイドブレイク")
      .lore(getLore("ワイドブレイク","5*3",45))
      .build(),
    ButtonMotion{_ =>
      skillToggle(player, "ワイドブレイク")
    }
  )

  val clear: Button = Button(
    Item.getPlayerSkull(player,s"${GREEN}スキル選択を解除します",
      List(s"${GRAY}現在保有中の特殊スキル解放ポイント:" + player.getSpecialSkillOpenPoint)),
    ButtonMotion{_ =>
      if (SpecialSkillPlayerData.getActivatedSkill(player).isDefined) {
        SpecialSkillPlayerData.skillInvalidation(player, SpecialSkillPlayerData.getActivatedSkill(player).get)
      }
      player.sendMessage(s"${AQUA}スキル選択を解除しました。")
    }
  )

  val backPage: Button = Button(
    ItemStackBuilder
      .getDefault(Material.MAGENTA_GLAZED_TERRACOTTA)
      .title(s"${GREEN}スキルカテゴリ選択画面に戻ります。")
      .lore(List("クリックで戻ります。"))
      .build(),
    ButtonMotion{_ =>
      new SkillCategoryMenu().open(player)
    }
  )

  private def getIcon(skillName: String, openedIcon: Material): Material = {
    if (isSkillOpened(player, skillName)) {
      openedIcon
    } else {
      Material.BEDROCK
    }
  }

  private def getLore(skillName: String, range: String, skillPoint: Int): List[String] = {
    List(s"$GRAY${range}の範囲を破壊します。"
      , s"${GRAY}消費スキルポイント:$skillPoint"
      , s"$GRAY ${if (isSkillOpened(player, skillName)) "クリックで選択します。" else "クリックで開放します。"}"
      , s"${if (!isSkillOpened(player, skillName)) s"$GRAY[解放条件]" else ""}"
      , s"${if (!isSkillOpened(player, skillName)) s"$GRAY・特殊スキル解放ポイントを10ポイント消費" else ""}"
      , s"${if (!isSkillOpened(player, skillName) && skillName != "ブレイク・デュオ") s"$GRAY・下位スキルをすべて開放" else ""}")
      .filterNot(_ == "")
  }
}
