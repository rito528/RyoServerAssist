package com.ryoserver.Player

import com.ryoserver.Level.Player.{BossBar, LevelLoader}
import com.ryoserver.NeoStack.PlayerData
import com.ryoserver.RyoServerAssist
import com.ryoserver.SkillSystems.Skill.skillToggleClass
import com.ryoserver.SkillSystems.SkillPoint.SkillPointBer
import org.bukkit.entity.Player

class PlayerDataLoader(ryoServerAssist: RyoServerAssist) {

  def load(p: Player): Unit = {
    new CreateData(ryoServerAssist).createPlayerData(p)
    new UpdateData(ryoServerAssist).update(p)
    new LevelLoader(ryoServerAssist).loadPlayerLevel(p)
    SkillPointBer.create(p, ryoServerAssist)
    new Name(ryoServerAssist).updateName(p)
    PlayerData.loadNeoStackPlayerData(ryoServerAssist, p)
  }

  def unload(p: Player): Unit = {
    BossBar.unloadLevelBer(p)
    SkillPointBer.remove(p)
    new skillToggleClass(p, ryoServerAssist).allEffectClear(p)
  }

}
