package com.ryoserver.Player

import com.ryoserver.Level.Player.{BossBar, LevelLoader}
//import com.ryoserver.Quest.PlayerQuestData
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.Skill.EffectSkill.SkillOperation
import com.ryoserver.SkillSystems.Skill.SpecialSkillPlayerData
import com.ryoserver.SkillSystems.SkillPoint.SkillPointBer
import org.bukkit.entity.Player

class PlayerDataLoader(implicit ryoServerAssist: RyoServerAssist) {

  def load(p: Player): Unit = {
    new CreateData().createPlayerData(p)
    new UpdateData().updateReLogin(p)
    new LevelLoader().loadPlayerLevel(p)
    PlayerData.loadNeoStackPlayerData(p)
    SkillPointBer.create(p)
    new Name().updateName(p)
//    PlayerQuestData.loadPlayerData(p)
  }

  def unload(p: Player): Unit = {
    BossBar.unloadLevelBer(p)
    SkillPointBer.remove(p)
    new SkillOperation(ryoServerAssist).allDisablingSkills(p)
    if (SpecialSkillPlayerData.getActivatedSkill(p).isDefined) SpecialSkillPlayerData.skillInvalidation(p, SpecialSkillPlayerData.getActivatedSkill(p).get)
  }

}
