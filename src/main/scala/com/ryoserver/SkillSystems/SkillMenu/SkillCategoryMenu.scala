package com.ryoserver.SkillSystems.SkillMenu

import com.ryoserver.Menu.Button.{Button, ButtonMotion}
import com.ryoserver.Menu.MenuLayout.getLayOut
import com.ryoserver.Menu.{Menu, MenuFrame}
import com.ryoserver.RyoServerAssist
import com.ryoserver.RyoServerMenu.RyoServerMenu1
import com.ryoserver.util.ItemStackBuilder
import org.bukkit.ChatColor._
import org.bukkit.Material
import org.bukkit.entity.Player

class SkillCategoryMenu(implicit ryoServerAssist: RyoServerAssist) extends Menu {

  override val frame: MenuFrame = MenuFrame(3,"スキルカテゴリ選択")

  override def settingMenuLayout(player: Player): Map[Int, Button] = {
    val compute = computeSelectSkillCategoryMenu(player,ryoServerAssist)
    import compute._
    Map(
      getLayOut(3,2) -> openEffectSkillMenu,
      getLayOut(5,2) -> openBreakSkillMenu,
      getLayOut(7,2) -> openFarmSkillMenu,
      getLayOut(1,3) -> backMenu
    )
  }

}

private case class computeSelectSkillCategoryMenu(player: Player,ryoServerAssist: RyoServerAssist) {
  val openEffectSkillMenu: Button = Button(
    ItemStackBuilder
      .getDefault(Material.POTION)
      .title(s"${GREEN}エフェクトスキル")
      .lore(List(s"${GRAY}クリックで開きます。"))
      .build(),
    ButtonMotion{_ =>
      new EffectSkillMenu(ryoServerAssist).open(player)
    }
  )

  val openBreakSkillMenu: Button = Button(
    ItemStackBuilder
      .getDefault(Material.GOLDEN_PICKAXE)
      .title(s"$GREEN[特殊] 破壊系スキル")
      .lore(List(s"${GRAY}クリックで開きます。"))
      .build(),
    ButtonMotion{_ =>
      new BreakSkillMenu(ryoServerAssist).open(player)
    }
  )

  val openFarmSkillMenu: Button = Button(
    ItemStackBuilder
      .getDefault(Material.GOLDEN_HOE)
      .title(s"$GREEN[特殊] 農業系スキル")
      .lore(List(s"${GRAY}クリックで開きます。"))
      .build(),
    ButtonMotion{_ =>
      new FarmSkillMenu(ryoServerAssist).open(player)
    }
  )

  val backMenu: Button = Button(
    ItemStackBuilder
      .getDefault(Material.MAGENTA_GLAZED_TERRACOTTA)
      .title(s"${GREEN}メニューに戻ります。")
      .lore(List(s"${GRAY}クリックで戻ります。"))
      .build(),
    ButtonMotion{_ =>
      new RyoServerMenu1(ryoServerAssist).open(player)
    }
  )
}
