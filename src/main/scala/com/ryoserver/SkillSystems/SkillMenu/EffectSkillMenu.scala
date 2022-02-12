package com.ryoserver.SkillSystems.SkillMenu

import com.ryoserver.Menu.Button.{Button, ButtonMotion}
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{Menu, MenuFrame}
import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.Skill.EffectSkill.{EffectSkills, SkillOperation}
import com.ryoserver.util.{Item, ItemStackBuilder}
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player

class EffectSkillMenu(ryoServerAssist: RyoServerAssist) extends Menu {

  override val frame: MenuFrame = MenuFrame(6, "通常スキル選択")

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    val compute = computeEffectSkillButton(player, ryoServerAssist)
    import compute._
    Map(
      getLayOut(1, 1) -> nankurunaisa,
      getLayOut(2, 1) -> yoidon,
      getLayOut(3, 1) -> takaminokenbutu,
      getLayOut(4, 1) -> tuyonaru,
      getLayOut(5, 1) -> horida,
      getLayOut(6, 1) -> zikahu,
      getLayOut(1, 2) -> antibekutoru,
      getLayOut(2, 2) -> nekonome,
      getLayOut(3, 2) -> homutekuto,
      getLayOut(4, 2) -> mizunokokyuu,
      getLayOut(1, 3) -> haganenomentaru,
      getLayOut(2, 3) -> sinsoku,
      getLayOut(3, 3) -> pyon,
      getLayOut(4, 3) -> mottotuyonaru,
      getLayOut(5, 3) -> saida,
      getLayOut(6, 3) -> tiyunokago,
      getLayOut(1, 6) -> backPage,
      getLayOut(9, 6) -> allClear
    )
  }
}

private case class computeEffectSkillButton(player: Player, plugin: RyoServerAssist) {
  private implicit val ryoServerAssist: RyoServerAssist = plugin
  private lazy val activateSkill = new SkillOperation(ryoServerAssist).skillActivation(player, _)

  val nankurunaisa: Button = Button(
    ItemStackBuilder
      .getDefault(getIcon(player, Material.SHIELD, EffectSkills.nankurunaisa))
      .title(s"$GREEN[基本スキル]なんくるないさ")
      .lore(List(s"${GRAY}耐性1の効果が付与されます。") ++ getLore(player, EffectSkills.nankurunaisa))
      .build(),
    ButtonMotion { _ =>
      activateSkill(EffectSkills.nankurunaisa)
      new EffectSkillMenu(ryoServerAssist).open(player)
    }
  )

  val yoidon: Button = Button(
    ItemStackBuilder
      .getDefault(getIcon(player, Material.IRON_BOOTS, EffectSkills.yoidon))
      .title(s"$GREEN[基本スキル]よーいドン")
      .lore(List(s"${GRAY}移動速度上昇1の効果が付与されます。") ++ getLore(player, EffectSkills.yoidon))
      .build(),
    ButtonMotion { _ =>
      activateSkill(EffectSkills.yoidon)
      new EffectSkillMenu(ryoServerAssist).open(player)
    }
  )

  val takaminokenbutu: Button = Button(
    ItemStackBuilder
      .getDefault(getIcon(player, Material.RABBIT_FOOT, EffectSkills.takaminokenbutu))
      .title(s"$GREEN[基本スキル]高みの見物")
      .lore(List(s"${GRAY}跳躍力上昇1の効果が付与されます。") ++ getLore(player, EffectSkills.takaminokenbutu))
      .build(),
    ButtonMotion { _ =>
      activateSkill(EffectSkills.takaminokenbutu)
      new EffectSkillMenu(ryoServerAssist).open(player)
    }
  )

  val tuyonaru: Button = Button(
    ItemStackBuilder
      .getDefault(getIcon(player, Material.IRON_SWORD, EffectSkills.tuyonaru))
      .title(s"$GREEN[基本スキル]ツヨナール")
      .lore(List(s"${GRAY}攻撃力上昇1の効果が付与されます。") ++ getLore(player, EffectSkills.tuyonaru))
      .build(),
    ButtonMotion { _ =>
      activateSkill(EffectSkills.tuyonaru)
      new EffectSkillMenu(ryoServerAssist).open(player)
    }
  )

  val horida: Button = Button(
    ItemStackBuilder
      .getDefault(getIcon(player, Material.IRON_PICKAXE, EffectSkills.horida))
      .title(s"$GREEN[基本スキル]ホリダー")
      .lore(List(s"${GRAY}採掘速度上昇1の効果が付与されます。") ++ getLore(player, EffectSkills.horida))
      .build(),
    ButtonMotion { _ =>
      activateSkill(EffectSkills.horida)
      new EffectSkillMenu(ryoServerAssist).open(player)
    }
  )

  val zikahu: Button = Button(
    ItemStackBuilder
      .getDefault(getIcon(player, Material.GOLDEN_APPLE, EffectSkills.zikahu))
      .title(s"$GREEN[基本スキル]ジ・カフ")
      .lore(List(s"${GRAY}再生能力1の効果が付与されます。") ++ getLore(player, EffectSkills.zikahu))
      .build(),
    ButtonMotion { _ =>
      activateSkill(EffectSkills.zikahu)
      new EffectSkillMenu(ryoServerAssist).open(player)
    }
  )

  val antibekutoru: Button = Button(
    ItemStackBuilder
      .getDefault(getIcon(player, Material.ELYTRA, EffectSkills.antibekutoru))
      .title(s"${GREEN}アンチベクトル")
      .lore(List(s"${GRAY}低速落下の効果が付与されます。") ++ getLore(player, EffectSkills.antibekutoru))
      .setEffect()
      .build(),
    ButtonMotion { _ =>
      activateSkill(EffectSkills.antibekutoru)
      new EffectSkillMenu(ryoServerAssist).open(player)
    }
  )

  val nekonome: Button = Button(
    ItemStackBuilder
      .getDefault(getIcon(player, Material.ENDER_EYE, EffectSkills.nekonome))
      .title(s"${GREEN}猫の目")
      .lore(List(s"${GRAY}暗視の効果が付与されます。") ++ getLore(player, EffectSkills.nekonome))
      .setEffect()
      .build(),
    ButtonMotion { _ =>
      activateSkill(EffectSkills.nekonome)
      new EffectSkillMenu(ryoServerAssist).open(player)
    }
  )

  val homutekuto: Button = Button(
    ItemStackBuilder
      .getDefault(getIcon(player, Material.LAVA_BUCKET, EffectSkills.homutekuto))
      .title(s"${GREEN}猫の目")
      .lore(List(s"${GRAY}耐火の効果が付与されます。") ++ getLore(player, EffectSkills.homutekuto))
      .setEffect()
      .build(),
    ButtonMotion { _ =>
      activateSkill(EffectSkills.homutekuto)
      new EffectSkillMenu(ryoServerAssist).open(player)
    }
  )

  val mizunokokyuu: Button = Button(
    ItemStackBuilder
      .getDefault(getIcon(player, Material.WATER_BUCKET, EffectSkills.mizunokokyuu))
      .title(s"${GREEN}水の呼吸")
      .lore(List(s"${GRAY}水中呼吸の効果が付与されます。") ++ getLore(player, EffectSkills.mizunokokyuu))
      .setEffect()
      .build(),
    ButtonMotion { _ =>
      activateSkill(EffectSkills.mizunokokyuu)
      new EffectSkillMenu(ryoServerAssist).open(player)
    }
  )

  val haganenomentaru: Button = Button(
    ItemStackBuilder
      .getDefault(getIcon(player, Material.SHIELD, EffectSkills.haganenomentaru))
      .title(s"${GREEN}鋼のメンタル")
      .lore(List(s"${GRAY}耐性2の効果が付与されます。") ++ getLore(player, EffectSkills.haganenomentaru))
      .setEffect()
      .build(),
    ButtonMotion { _ =>
      activateSkill(EffectSkills.haganenomentaru)
      new EffectSkillMenu(ryoServerAssist).open(player)
    }
  )

  val sinsoku: Button = Button(
    ItemStackBuilder
      .getDefault(getIcon(player, Material.IRON_BOOTS, EffectSkills.sinsoku))
      .title(s"${GREEN}神速")
      .lore(List(s"${GRAY}耐性2の効果が付与されます。") ++ getLore(player, EffectSkills.sinsoku))
      .setEffect()
      .build(),
    ButtonMotion { _ =>
      activateSkill(EffectSkills.sinsoku)
      new EffectSkillMenu(ryoServerAssist).open(player)
    }
  )

  val pyon: Button = Button(
    ItemStackBuilder
      .getDefault(getIcon(player, Material.RABBIT_FOOT, EffectSkills.pyon))
      .title(s"${GREEN}ぴょん")
      .lore(List(s"${GRAY}耐性2の効果が付与されます。") ++ getLore(player, EffectSkills.pyon))
      .setEffect()
      .build(),
    ButtonMotion { _ =>
      activateSkill(EffectSkills.pyon)
      new EffectSkillMenu(ryoServerAssist).open(player)
    }
  )

  val mottotuyonaru: Button = Button(
    ItemStackBuilder
      .getDefault(getIcon(player, Material.IRON_SWORD, EffectSkills.mottotuyonaru))
      .title(s"${GREEN}モットツヨナール")
      .lore(List(s"${GRAY}攻撃力上昇2の効果が付与されます。") ++ getLore(player, EffectSkills.mottotuyonaru))
      .setEffect()
      .build(),
    ButtonMotion { _ =>
      activateSkill(EffectSkills.mottotuyonaru)
      new EffectSkillMenu(ryoServerAssist).open(player)
    }
  )

  val saida: Button = Button(
    ItemStackBuilder
      .getDefault(getIcon(player, Material.IRON_PICKAXE, EffectSkills.saida))
      .title(s"${GREEN}サイダー")
      .lore(List(s"${GRAY}採掘速度上昇2の効果が付与されます。") ++ getLore(player, EffectSkills.saida))
      .setEffect()
      .build(),
    ButtonMotion { _ =>
      activateSkill(EffectSkills.saida)
      new EffectSkillMenu(ryoServerAssist).open(player)
    }
  )

  val tiyunokago: Button = Button(
    ItemStackBuilder
      .getDefault(getIcon(player, Material.GOLDEN_APPLE, EffectSkills.tiyunokago))
      .title(s"${GREEN}治癒の加護")
      .lore(List(s"${GRAY}再生能力2の効果が付与されます。") ++ getLore(player, EffectSkills.tiyunokago))
      .setEffect()
      .build(),
    ButtonMotion { _ =>
      activateSkill(EffectSkills.tiyunokago)
      new EffectSkillMenu(ryoServerAssist).open(player)
    }
  )

  val allClear: Button = Button(
    Item.getPlayerSkull(player, s"${GREEN}クリックですべてのスキル選択を解除できます。", List(
      s"${GRAY}現在保有中のスキル開放ポイント:" + player.getSkillOpenPoint
    )),
    ButtonMotion { _ =>
      new SkillOperation(ryoServerAssist).allDisablingSkills(player)
      new EffectSkillMenu(ryoServerAssist).open(player)
    }
  )

  val backPage: Button = Button(
    ItemStackBuilder
      .getDefault(Material.MAGENTA_GLAZED_TERRACOTTA)
      .title(s"${GREEN}メニューに戻る")
      .lore(List(s"${GRAY}クリックで戻ります。"))
      .build(),
    ButtonMotion { _ =>
      new SkillCategoryMenu().open(player)
    }
  )

  private def getIcon(p: Player, material: Material, effectSkills: EffectSkills): Material = {
    if (p.getOpenedSkills.contains(effectSkills)) material
    else Material.BEDROCK
  }

  private def getLore(p: Player, effectSkills: EffectSkills): List[String] = {
    List(if (p.getOpenedSkills.contains(effectSkills)) s"${GRAY}解放済みです。" else s"$GRAY[解放条件]",
      if (p.getOpenedSkills.contains(effectSkills)) "" else s"$GRAY・スキル解放ポイントを10消費",
      if (!p.getOpenedSkills.contains(effectSkills) && effectSkills.isSpecialSkill) s"$GRAY・基本スキルをすべて開放" else "",
      s"${GRAY}スキルポイントコスト:${effectSkills.cost}").filterNot(_ == "")
  }

}
