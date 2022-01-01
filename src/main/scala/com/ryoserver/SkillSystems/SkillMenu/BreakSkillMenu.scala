package com.ryoserver.SkillSystems.SkillMenu

import com.ryoserver.Menu.Menu
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Player.Data
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.Skill.SpecialSkillPlayerData
import com.ryoserver.SkillSystems.Skill.SpecialSkillPlayerData.{isSkillOpened, skillToggle}
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player

class BreakSkillMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  override val slot: Int = 6
  override var name: String = "破壊系スキル選択"
  override var p: Player = _

  def openBreakSkillMenu(player: Player): Unit = {
    p = player
    setItem(2, 1, if (isSkillOpened(p, "ブレイク・デュオ")) Material.WOODEN_PICKAXE else Material.BEDROCK, effect = false, s"${GREEN}ブレイク・デュオ",
      getLore("ブレイク・デュオ","1*2",6))
    setItem(4, 1, if (isSkillOpened(p, "アップダウンブレイク")) Material.STONE_PICKAXE else Material.BEDROCK, effect = false, s"${GREEN}アップダウンブレイク",
      getLore("アップダウンブレイク","1*3",9))
    setItem(6, 1, if (isSkillOpened(p, "パンチングブレイク")) Material.IRON_PICKAXE else Material.BEDROCK, effect = false, s"${GREEN}パンチングブレイク",
      getLore("パンチングブレイク","3*2",18))
    setItem(8, 1, if (isSkillOpened(p, "トンネルブレイク")) Material.GOLDEN_PICKAXE else Material.BEDROCK, effect = false, s"${GREEN}トンネルブレイク",
      getLore("トンネルブレイク","3*3",27))
    setItem(2, 2, if (isSkillOpened(p, "ワイドブレイク")) Material.DIAMOND_PICKAXE else Material.BEDROCK, effect = false, s"${GREEN}ワイドブレイク",
      getLore("ワイドブレイク","5*3",45))
    setItem(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, effect = false, s"${GREEN}スキルカテゴリ選択画面に戻ります。", List(s"${GRAY}クリックで戻ります。"))
    setSkullItem(5, 6, p, s"${GREEN}スキル選択を解除します。", List(s"${GRAY}現在保有中の特殊スキル解放ポイント:" + Data.playerData(p.getUniqueId).specialSkillOpenPoint))
    registerMotion(motion)
    open()
  }

  def getLore(skillName:String,range:String,skillPoint:Int): List[String] = {
    List(s"$GRAY${range}の範囲を破壊します。"
      , s"${GRAY}消費スキルポイント:${skillPoint}"
      , s"$GRAY ${if (isSkillOpened(p, skillName)) "クリックで選択します。" else "クリックで開放します。"}"
      , s"$GRAY[解放条件]"
      , s"$GRAY${if (isSkillOpened(p, skillName)) "・特殊スキル解放ポイントを10ポイント消費"}"
      , s"$GRAY${if (isSkillOpened(p, skillName) && skillName != "ブレイク・デュオ") "・下位スキルをすべて開放"}")
  }

  def motion(p: Player, index: Int): Unit = {
    if (getLayOut(2, 1) == index) {
      skillToggle(p, "ブレイク・デュオ")
      new BreakSkillMenu(ryoServerAssist).openBreakSkillMenu(p)
    } else if (getLayOut(4, 1) == index) {
      skillToggle(p, "アップダウンブレイク")
      new BreakSkillMenu(ryoServerAssist).openBreakSkillMenu(p)
    } else if (getLayOut(6, 1) == index) {
      skillToggle(p, "パンチングブレイク")
      new BreakSkillMenu(ryoServerAssist).openBreakSkillMenu(p)
    } else if (getLayOut(8, 1) == index) {
      skillToggle(p, "トンネルブレイク")
      new BreakSkillMenu(ryoServerAssist).openBreakSkillMenu(p)
    } else if (getLayOut(2, 2) == index) {
      skillToggle(p, "ワイドブレイク")
      new BreakSkillMenu(ryoServerAssist).openBreakSkillMenu(p)
    } else if (getLayOut(1, 6) == index) {
      new SkillCategoryMenu(ryoServerAssist).openSkillCategoryMenu(p)
    } else if (getLayOut(5, 6) == index) {
      if (SpecialSkillPlayerData.getActivatedSkill(p).isDefined) SpecialSkillPlayerData.skillInvalidation(p, SpecialSkillPlayerData.getActivatedSkill(p).get)
      p.sendMessage(s"${AQUA}スキル選択を解除しました。")
    }
  }

}
