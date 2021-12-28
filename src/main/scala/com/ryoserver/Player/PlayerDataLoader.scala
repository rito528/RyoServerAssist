package com.ryoserver.Player

import com.ryoserver.Level.Player.{BossBar, LevelLoader}
import com.ryoserver.NeoStack.PlayerData
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.Skill.EffectSkill.skillToggleClass
import com.ryoserver.SkillSystems.Skill.SpecialSkillPlayerData
import com.ryoserver.SkillSystems.SkillPoint.SkillPointBer
import org.bukkit.entity.Player

class PlayerDataLoader(ryoServerAssist: RyoServerAssist) {

  def load(p: Player): Unit = {
    new CreateData(ryoServerAssist).createPlayerData(p)
    new UpdateData(ryoServerAssist).update(p)
    new LevelLoader().loadPlayerLevel(p)
    PlayerData.loadNeoStackPlayerData(ryoServerAssist, p)
    SkillPointBer.create(p)
    new Name(ryoServerAssist).updateName(p)
  }

  def unload(p: Player): Unit = {
    BossBar.unloadLevelBer(p)
    SkillPointBer.remove(p)
    new skillToggleClass(p, ryoServerAssist).allEffectClear(p)
    if (SpecialSkillPlayerData.getActivatedSkill(p).isDefined) SpecialSkillPlayerData.skillInvalidation(p, SpecialSkillPlayerData.getActivatedSkill(p).get)
  }

}
