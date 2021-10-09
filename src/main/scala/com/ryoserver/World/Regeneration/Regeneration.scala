package com.ryoserver.World.Regeneration

import com.onarandombox.MultiverseCore.MultiverseCore
import com.onarandombox.MultiversePortals.MultiversePortals
import com.ryoserver.RyoServerAssist
import org.bukkit.Bukkit.getConsoleSender
import org.bukkit.World.Environment
import org.bukkit._

import java.security.SecureRandom
import java.util.{Calendar, TimeZone}

class Regeneration(ryoServerAssist: RyoServerAssist) {

  private def isFriday: Boolean = {
    val calendar = Calendar.getInstance()
    calendar.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"))
    if (calendar.get(Calendar.DAY_OF_WEEK) == 7) return true
    false
  }

  private def regenerationCommands(listName:String,worldType:Environment): Unit = {
    val core = Bukkit.getServer.getPluginManager.getPlugin("Multiverse-Core").asInstanceOf[MultiverseCore]
    val portals = Bukkit.getServer.getPluginManager.getPlugin("Multiverse-Portals").asInstanceOf[MultiversePortals]
    val worldManager = core.getMVWorldManager
    ryoServerAssist.getConfig.getStringList(listName).forEach(world => {
      List(
        s"dynmap purgemap $world cave",
        s"dynmap purgemap $world Surface",
        s"dynmap purgemap $world Flat",
        s"dmap mapdelete $world:cave",
        s"dmap mapdelete $world:Surface",
        s"dmap mapdelete $world:Flat"
      ).foreach(cmd => Bukkit.dispatchCommand(getConsoleSender,cmd))
      worldManager.deleteWorld(world)
      worldManager.addWorld(world,worldType,null,WorldType.NORMAL,true,null)
      worldManager.getMVWorld(world).getCBWorld.setGameRule(GameRule.KEEP_INVENTORY.asInstanceOf[GameRule[Any]],true)
      worldManager.getMVWorld(world).setDifficulty(Difficulty.HARD)
      val spawnLocation = worldManager.getMVWorld(world).getCBWorld.getSpawnLocation
      val random = SecureRandom.getInstance("SHA1PRNG")
      worldManager.getMVWorld(world).getCBWorld.setSpawnLocation(worldManager.getMVWorld(world).getCBWorld.getHighestBlockAt(random.nextInt(1000),random.nextInt(1000)).getLocation)
      spawnLocation.add(0,-1,0).getBlock.setType(Material.BEDROCK)
      worldManager.getMVWorld(world).getCBWorld.setSpawnLocation(spawnLocation)
      Bukkit.dispatchCommand(getConsoleSender,s"wb $world set 5000 5000 spawn")
      Bukkit.dispatchCommand(getConsoleSender,"wb shape square")
      portals.getPortalManager.getPortal(s"worldTo$world").setExactDestination(Bukkit.getWorld(world).getSpawnLocation)
      Bukkit.dispatchCommand(getConsoleSender,s"dynmap fullrender $world")
    })
  }

  def regeneration(): Unit = {
    if (!isFriday) return
    ryoServerAssist.getLogger.info("ワールドの再生成を行います。")
    regenerationCommands("regenerationNormalWorlds",Environment.NORMAL)
    regenerationCommands("regenerationNetherWorlds",Environment.NETHER)
    regenerationCommands("regenerationEndWorlds",Environment.THE_END)
  }

}
