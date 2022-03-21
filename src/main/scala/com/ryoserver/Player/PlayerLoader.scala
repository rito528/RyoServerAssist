package com.ryoserver.Player

import com.ryoserver.Level.Player.{BossBar, LevelLoader}
import com.ryoserver.Maintenance.MaintenanceData.getMaintenance
import com.ryoserver.NeoStack.NeoStackItem.NeoStackItemRepository
import com.ryoserver.Player.PlayerManager.getPlayerData
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.Skill.EffectSkill.SkillOperation
import com.ryoserver.SkillSystems.Skill.SpecialSkillPlayerData
import com.ryoserver.SkillSystems.SkillPoint.SkillPointBer
import com.ryoserver.Title.GiveTitle
import org.bukkit.entity.Player

class PlayerLoader(implicit ryoServerAssist: RyoServerAssist) {

  def load(p: Player): Unit = {
    if (getMaintenance && !p.hasPermission("ryoserverassist.maintenance")) {
      p.kickPlayer("現在メンテナンス中です。\n\n詳細は公式Twitter、Discordを御覧ください。")
    }
    new PlayerService().loadPlayerData(p.getUniqueId)
    new UpdateData().updateReLogin(p)
    new LevelLoader().loadPlayerLevel(p)
    new NeoStackItemRepository().restore(p.getUniqueId)
    SkillPointBer.create(p)
    new Name().updateName(p)
    val title = new GiveTitle()
    title.continuousLogin(p)
    title.loginDays(p)
    title.loginYear(p)
    title.loginPeriod(p)
    title.loginDay(p)
    title.continuousLoginAndQuestClearNumber(p)
  }

  def unload(implicit p: Player): Unit = {
    BossBar.unloadLevelBer(p)
    SkillPointBer.remove(p)
    new SkillOperation(ryoServerAssist).allDisablingSkills(p)
    if (SpecialSkillPlayerData.getActivatedSkill(p).isDefined) SpecialSkillPlayerData.skillInvalidation(p, SpecialSkillPlayerData.getActivatedSkill(p).get)
    p.getRyoServerData.setLastLogoutNow().apply
  }

}
