package com.ryoserver.Player

import com.ryoserver.Level.Player.{BossBar, LevelLoader}
import com.ryoserver.NeoStack.{NeoStackGateway, PlayerData}
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.Skill.EffectSkill.skillToggleClass
import com.ryoserver.SkillSystems.SkillPoint.SkillPointBer
import org.bukkit.entity.Player

class PlayerDataLoader(ryoServerAssist: RyoServerAssist) {

  def load(p: Player): Unit = {
    new CreateData(ryoServerAssist).createPlayerData(p)
    new LoadPlayerData(ryoServerAssist).load()
    new UpdateData(ryoServerAssist).update(p)
    new LevelLoader(ryoServerAssist).loadPlayerLevel(p)
    new Name(ryoServerAssist).updateName(p)
    PlayerData.loadNeoStackPlayerData(ryoServerAssist, p)
    SkillPointBer.create(p, ryoServerAssist)
  }

  def unload(p: Player): Unit = {
    BossBar.unloadLevelBer(p)
    SkillPointBer.remove(p)
    new skillToggleClass(p, ryoServerAssist).allEffectClear(p)
  }

}
