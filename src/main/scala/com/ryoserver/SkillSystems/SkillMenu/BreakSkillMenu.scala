package com.ryoserver.SkillSystems.SkillMenu

import com.ryoserver.Menu.{MenuOld, MenuButton, MenuSkull}
import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.Skill.SpecialSkillPlayerData
import com.ryoserver.SkillSystems.Skill.SpecialSkillPlayerData.{isSkillOpened, skillToggle}
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player

class BreakSkillMenu(ryoServerAssist: RyoServerAssist) extends MenuOld {

  override val slot: Int = 6
  override var name: String = "破壊系スキル選択"
  override var p: Player = _

  def openBreakSkillMenu(player: Player): Unit = {
    p = player
    setButton(MenuButton(2, 1, getIcon("ブレイク・デュオ", Material.WOODEN_PICKAXE), s"${GREEN}ブレイク・デュオ",
      getLore("ブレイク・デュオ", "1*2", 6))
      .setLeftClickMotion(toggle(_, "ブレイク・デュオ"))
      .setReload())
    setButton(MenuButton(4, 1, getIcon("アップダウンブレイク", Material.STONE_PICKAXE), s"${GREEN}アップダウンブレイク",
      getLore("アップダウンブレイク", "1*3", 9))
      .setLeftClickMotion(toggle(_, "アップダウンブレイク"))
      .setReload())
    setButton(MenuButton(6, 1, getIcon("パンチングブレイク", Material.IRON_PICKAXE), s"${GREEN}パンチングブレイク",
      getLore("パンチングブレイク", "3*2", 18))
      .setLeftClickMotion(toggle(_, "パンチングブレイク"))
      .setReload())
    setButton(MenuButton(8, 1, getIcon("トンネルブレイク", Material.GOLDEN_PICKAXE), s"${GREEN}トンネルブレイク",
      getLore("トンネルブレイク", "3*3", 27))
      .setLeftClickMotion(toggle(_, "トンネルブレイク"))
      .setReload())
    setButton(MenuButton(2, 2, getIcon("ワイドブレイク", Material.DIAMOND_PICKAXE), s"${GREEN}ワイドブレイク",
      getLore("ワイドブレイク", "5*3", 45))
      .setLeftClickMotion(toggle(_, "ワイドブレイク"))
      .setReload())
    setButton(MenuButton(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, s"${GREEN}スキルカテゴリ選択画面に戻ります。", List(s"${GRAY}クリックで戻ります。"))
      .setLeftClickMotion(backPage))
    setSkull(MenuSkull(5, 6, p, s"${GREEN}スキル選択を解除します。",
      List(s"${GRAY}現在保有中の特殊スキル解放ポイント:" + p.getSpecialSkillOpenPoint))
      .setLeftClickMotion(clear))
    build(new BreakSkillMenu(ryoServerAssist).openBreakSkillMenu)
    open()
  }

  private def getIcon(skillName: String, openedIcon: Material): Material = {
    if (isSkillOpened(p, skillName)) {
      openedIcon
    } else {
      Material.BEDROCK
    }
  }

  private def toggle(p: Player, skillName: String): Unit = {
    skillToggle(p, skillName)
  }

  private def backPage(p: Player): Unit = {
    new SkillCategoryMenu(ryoServerAssist).openSkillCategoryMenu(p)
  }

  private def clear(p: Player): Unit = {
    if (SpecialSkillPlayerData.getActivatedSkill(p).isDefined) {
      SpecialSkillPlayerData.skillInvalidation(p, SpecialSkillPlayerData.getActivatedSkill(p).get)
    }
    p.sendMessage(s"${AQUA}スキル選択を解除しました。")
  }

  private def getLore(skillName: String, range: String, skillPoint: Int): List[String] = {
    List(s"$GRAY${range}の範囲を破壊します。"
      , s"${GRAY}消費スキルポイント:$skillPoint"
      , s"$GRAY ${if (isSkillOpened(p, skillName)) "クリックで選択します。" else "クリックで開放します。"}"
      , s"${if (!isSkillOpened(p, skillName)) s"$GRAY[解放条件]" else ""}"
      , s"${if (!isSkillOpened(p, skillName)) s"$GRAY・特殊スキル解放ポイントを10ポイント消費" else ""}"
      , s"${if (!isSkillOpened(p, skillName) && skillName != "ブレイク・デュオ") s"$GRAY・下位スキルをすべて開放" else ""}")
      .filterNot(_ == "")
  }

}
