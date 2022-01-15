package com.ryoserver.World.Regeneration

import com.onarandombox.MultiverseCore.MultiverseCore
import com.onarandombox.MultiversePortals.MultiversePortals
import com.ryoserver.Config.ConfigData.getConfig
import com.ryoserver.RyoServerAssist
import org.bukkit.Bukkit.getConsoleSender
import org.bukkit.World.Environment
import org.bukkit._

import java.security.SecureRandom
import java.util.{Calendar, TimeZone}

class Regeneration(ryoServerAssist: RyoServerAssist) {

  def regeneration(isForce: Boolean = false): Unit = {
    if ((!isFriday && !isForce) || (!isForce && !getConfig.autoWorldRegeneration)) return
    ryoServerAssist.getLogger.info("ワールドの再生成を行います。")
    regenerationCommands(getConfig.regenerationNormalWorlds, Environment.NORMAL)
    regenerationCommands(getConfig.regenerationNetherWorlds, Environment.NETHER)
    regenerationCommands(getConfig.regenerationEndWorlds, Environment.THE_END)
  }

  private def isFriday: Boolean = {
    val calendar = Calendar.getInstance()
    calendar.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"))
    if (calendar.get(Calendar.DAY_OF_WEEK) == 6) return true
    false
  }

  private def regenerationCommands(list: List[String], worldType: Environment): Unit = {
    val core = Bukkit.getServer.getPluginManager.getPlugin("Multiverse-Core").asInstanceOf[MultiverseCore]
    val portals = Bukkit.getServer.getPluginManager.getPlugin("Multiverse-Portals").asInstanceOf[MultiversePortals]
    val worldManager = core.getMVWorldManager
    list.foreach(world => {
      List(
        "dynmap pause all",
        s"dynmap purgemap $world flat",
        "dynmap pause none"
      ).foreach(cmd => Bukkit.dispatchCommand(getConsoleSender, cmd))
      worldManager.deleteWorld(world)
      worldManager.addWorld(world, worldType, null, WorldType.NORMAL, true, null)
      worldManager.getMVWorld(world).getCBWorld.setGameRule(GameRule.KEEP_INVENTORY.asInstanceOf[GameRule[Any]], true)
      worldManager.getMVWorld(world).getCBWorld.setGameRule(GameRule.DO_INSOMNIA.asInstanceOf[GameRule[Any]], false)
      worldManager.getMVWorld(world).setDifficulty(Difficulty.HARD)
      val random = SecureRandom.getInstance("SHA1PRNG")
      var x = random.nextInt(500)
      var z = random.nextInt(500)
      var y = 64
      if (worldType == Environment.THE_END) {
        worldManager.getMVWorld(world).setRespawnToWorld("world")
        x = -106
        z = -60
        y = 55
      }
      val landLocation = new Location(worldManager.getMVWorld(world).getCBWorld, x, y, z)
      for (y <- 0 until 60) {
        for (x <- 0 until 40) {
          for (z <- 0 until 40) {
            val minusLoc = landLocation.clone().add(-20, 0, -20)
            minusLoc.add(x, y, z).getBlock.setType(Material.AIR)
          }
        }
      }
      for (x <- 0 until 40) {
        for (z <- 0 until 40) {
          val minusLoc = landLocation.clone().add(-20, 0, -20)
          minusLoc.add(x, 0, z).getBlock.setType(Material.BEDROCK)
        }
      }
      worldManager.getMVWorld(world).setSpawnLocation(new Location(worldManager.getMVWorld(world).getCBWorld, x, y, z))
      Bukkit.dispatchCommand(getConsoleSender, s"wb $world set 5000 5000 spawn")
      Bukkit.dispatchCommand(getConsoleSender, "wb shape square")
      portals.getPortalManager.getPortal(s"worldTo$world").setExactDestination(Bukkit.getWorld(world).getSpawnLocation)
      Bukkit.dispatchCommand(getConsoleSender, s"dynmap fullrender $world:Flat")
    })
  }

}
