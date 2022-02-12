package com.ryoserver.World.Regeneration

import com.onarandombox.MultiversePortals.MultiversePortals
import com.ryoserver.RyoServerAssist
import org.bukkit.Bukkit.getConsoleSender
import org.bukkit.World.Environment
import org.bukkit._
import org.bukkit.scheduler.BukkitRunnable

import java.io.File
import java.security.SecureRandom

class RegenerationTask(ryoServerAssist: RyoServerAssist, list: List[String], worldType: Environment) {

  def runRegeneration(): Unit = {
    val portals = Bukkit.getServer.getPluginManager.getPlugin("Multiverse-Portals").asInstanceOf[MultiversePortals]
    list.foreach(world => {
      List(
        "dynmap pause all",
        s"dynmap purgemap $world flat",
        "dynmap pause none"
      ).foreach(cmd => Bukkit.dispatchCommand(getConsoleSender, cmd))
      Bukkit.unloadWorld(world, false)
      deleteDirectory(new File(world))
      new BukkitRunnable {
        override def run(): Unit = {
          val createdWorld = WorldCreator.name(world)
            .environment(worldType)
            .seed(new SecureRandom().nextLong())
            .createWorld()
          createdWorld.setGameRule(GameRule.KEEP_INVENTORY.asInstanceOf[GameRule[Any]], true)
          createdWorld.setGameRule(GameRule.DO_INSOMNIA.asInstanceOf[GameRule[Any]], false)
          createdWorld.setDifficulty(Difficulty.HARD)
          new BukkitRunnable {
            override def run(): Unit = {
              val random = SecureRandom.getInstance("SHA1PRNG")
              var x = random.nextInt(500)
              var z = random.nextInt(500)
              var y = 64
              if (worldType == Environment.THE_END) {
                x = -106
                z = -60
                y = 55
              }
              new BukkitRunnable {
                override def run(): Unit = {
                  val landLocation = new Location(createdWorld, x, y, z)
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
                  createdWorld.setSpawnLocation(new Location(createdWorld, x, y, z))
                  new BukkitRunnable {
                    override def run(): Unit = {
                      Bukkit.dispatchCommand(getConsoleSender, s"wb $world set 5000 5000 spawn")
                      Bukkit.dispatchCommand(getConsoleSender, "wb shape square")
                      portals.getPortalManager.getPortal(s"worldTo$world").setExactDestination(Bukkit.getWorld(world).getSpawnLocation)
                      Bukkit.dispatchCommand(getConsoleSender, s"dynmap fullrender $world:Flat")
                    }
                  }.runTaskLater(ryoServerAssist, 60)
                }
              }.runTaskLater(ryoServerAssist, 60)
            }
          }.runTaskLater(ryoServerAssist, 60)
        }
      }.runTaskLater(ryoServerAssist, 60)
    })
  }

  def deleteDirectory(file: File): Unit = {
    if (!file.exists()) return
    if (file.isDirectory) {
      file.listFiles().foreach(file => {
        deleteDirectory(file)
      })
    }
    file.delete()
  }

}
