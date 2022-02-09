package com.ryoserver.SkillSystems.SkillMenu

import com.ryoserver.Menu.{MenuOld, MenuButton, MenuSkull}
import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.Skill.EffectSkill.{EffectSkills, SkillOperation}
import com.ryoserver.SkillSystems.Skill.EffectSkill.EffectSkills._
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player

class EffectSkillMenu(ryoServerAssist: RyoServerAssist) extends MenuOld {

  val slot: Int = 6
  var name: String = "通常スキル選択"
  var p: Player = _

  private implicit val plugin: RyoServerAssist = ryoServerAssist

  def openMenu(player: Player): Unit = {
    p = player
    val lore = getLore(p)(_)
    val activateSkill = new SkillOperation(ryoServerAssist).skillActivation _
    setButton(MenuButton(1, 1, getIcon(p,Material.SHIELD,nankurunaisa),
      s"${GREEN}[基本スキル]${nankurunaisa.skillName}", List(s"${GRAY}耐性1の効果が付与されます。") ::: lore(nankurunaisa))
      .setLeftClickMotion(activateSkill(_,nankurunaisa))
    .setReload())
    setButton(MenuButton(2, 1, getIcon(p,Material.IRON_BOOTS,yoidon),
      s"${GREEN}[基本スキル]${yoidon.skillName}", List(s"${GRAY}移動速度上昇1の効果が付与されます。") ::: lore(yoidon))
      .setLeftClickMotion(activateSkill(_,yoidon))
      .setReload())
    setButton(MenuButton(3, 1, getIcon(p,Material.RABBIT_FOOT,takaminokenbutu),
      s"${GREEN}[基本スキル]${takaminokenbutu.skillName}", List(s"${GRAY}跳躍力上昇1の効果が付与されます。") ::: lore(takaminokenbutu))
      .setLeftClickMotion(activateSkill(_,takaminokenbutu))
      .setReload())
    setButton(MenuButton(4, 1, getIcon(p,Material.IRON_SWORD,tuyonaru),
      s"${GREEN}[基本スキル]${tuyonaru.skillName}", List(s"${GRAY}攻撃力上昇1の効果が付与されます。") ::: lore(tuyonaru))
      .setLeftClickMotion(activateSkill(_,tuyonaru))
      .setReload())
    setButton(MenuButton(5, 1, getIcon(p,Material.IRON_PICKAXE,horida),
      s"${GREEN}[基本スキル]${horida.skillName}", List(s"${GRAY}採掘速度上昇1の効果が付与されます。") ::: lore(horida))
      .setLeftClickMotion(activateSkill(_,horida))
      .setReload())
    setButton(MenuButton(6, 1, getIcon(p,Material.ENCHANTED_GOLDEN_APPLE,zikahu),
      s"${GREEN}[基本スキル]${zikahu.skillName}", List(s"${GRAY}再生能力1の効果が付与されます。") ::: lore(zikahu))
      .setLeftClickMotion(activateSkill(_,zikahu))
      .setReload())
    setButton(MenuButton(1, 2, getIcon(p,Material.ELYTRA,antibekutoru),
      s"${GREEN}${antibekutoru.skillName}",
      List(s"${GRAY}低速落下の効果が付与されます。") ::: lore(antibekutoru))
      .setLeftClickMotion(activateSkill(_,antibekutoru))
      .setEffect()
      .setReload())
    setButton(MenuButton(2, 2, getIcon(p,Material.ENDER_EYE,nekonome),
      s"${GREEN}${nekonome.skillName}",
      List(s"${GRAY}暗視の効果が付与されます。") ::: lore(nekonome))
      .setLeftClickMotion(activateSkill(_,nekonome))
      .setEffect()
      .setReload())
    setButton(MenuButton(3, 2, getIcon(p,Material.LAVA_BUCKET,homutekuto),
      s"${GREEN}${homutekuto.skillName}",
      List(s"${GRAY}耐火の効果が付与されます。") ::: lore(homutekuto))
      .setLeftClickMotion(activateSkill(_,homutekuto))
      .setEffect()
      .setReload())
    setButton(MenuButton(4, 2, getIcon(p,Material.WATER_BUCKET,mizunokokyuu),
      s"${GREEN}${mizunokokyuu.skillName}",
      List(s"${GRAY}水中呼吸の効果が付与されます。") ::: lore(mizunokokyuu))
      .setLeftClickMotion(activateSkill(_,mizunokokyuu))
      .setEffect()
      .setReload())
    setButton(MenuButton(1, 3, getIcon(p,Material.SHIELD,haganenomentaru),
      s"${GREEN}${haganenomentaru.skillName}", List(s"${GRAY}耐性2の効果が付与されます。") ::: lore(haganenomentaru))
      .setLeftClickMotion(activateSkill(_,haganenomentaru))
      .setEffect()
      .setReload())
    setButton(MenuButton(2, 3, getIcon(p, Material.IRON_BOOTS,sinsoku),
      s"${GREEN}${sinsoku.skillName}", List(s"${GRAY}移動速度上昇2の効果が付与されます。") ::: lore(sinsoku))
      .setLeftClickMotion(activateSkill(_,sinsoku))
      .setEffect()
      .setReload())
    setButton(MenuButton(3, 3, getIcon(p,Material.RABBIT_FOOT,pyon),
      s"${GREEN}${pyon.skillName}", List(s"${GRAY}跳躍力上昇2の効果が付与されます。") ::: lore(pyon))
      .setLeftClickMotion(activateSkill(_,pyon))
      .setEffect()
      .setReload())
    setButton(MenuButton(4, 3, getIcon(p,Material.IRON_SWORD,mottotuyonaru),
      s"${GREEN}${mottotuyonaru.skillName}", List(s"${GRAY}攻撃力上昇2の効果が付与されます。") ::: lore(mottotuyonaru))
      .setLeftClickMotion(activateSkill(_,mottotuyonaru))
      .setEffect()
      .setReload())
    setButton(MenuButton(5, 3, getIcon(p,Material.IRON_PICKAXE,saida),
      s"${GREEN}${saida.skillName}", List(s"${GRAY}採掘速度上昇2の効果が付与されます。") ::: lore(saida))
      .setLeftClickMotion(activateSkill(_,saida))
      .setEffect()
      .setReload())
    setButton(MenuButton(6, 3, getIcon(p,Material.ENCHANTED_GOLDEN_APPLE,tiyunokago),
      s"${GREEN}${tiyunokago.skillName}", List(s"${GRAY}再生能力2の効果が付与されます。") ::: lore(tiyunokago))
      .setLeftClickMotion(activateSkill(_,tiyunokago))
      .setEffect()
      .setReload())
    setButton(MenuButton(1, 6, Material.MAGENTA_GLAZED_TERRACOTTA, s"${GREEN}メニューに戻る", List(s"${GRAY}メニューに戻ります。"))
      .setLeftClickMotion(backPage))
    setSkull(MenuSkull(8, 6, p, s"${GREEN}クリックですべてのスキル選択を解除できます。", List(
      s"${GRAY}現在保有中のスキル開放ポイント:" + p.getSkillOpenPoint
    )).setLeftClickMotion(new SkillOperation(ryoServerAssist).allDisablingSkills))
    build(new EffectSkillMenu(ryoServerAssist).openMenu)
    open()
  }

  private def getIcon(p: Player,material: Material,effectSkills: EffectSkills): Material = {
    if (p.getOpenedSkills.contains(effectSkills)) material
    else Material.BEDROCK
  }

  private def getLore(p:Player)(effectSkills: EffectSkills): List[String] = {
    List(if (p.getOpenedSkills.contains(effectSkills)) s"${GRAY}解放済みです。" else s"$GRAY[解放条件]",
      if (p.getOpenedSkills.contains(effectSkills)) "" else s"$GRAY・スキル解放ポイントを10消費",
      if (!p.getOpenedSkills.contains(effectSkills) && effectSkills.isSpecialSkill) s"$GRAY・基本スキルをすべて開放" else  "",
    s"${GRAY}スキルポイントコスト:${effectSkills.cost}").filterNot(_ == "")
  }

  private def backPage(p: Player): Unit = {
    new SkillCategoryMenu().openSkillCategoryMenu(p)
  }

}
