package com.ryoserver.SkillSystems.Skill

import org.bukkit.{Location, Sound}
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable

import java.security.SecureRandom

class DestructionSkill extends Listener {

  @EventHandler
  def onBreak(e: BlockBreakEvent): Unit = {
    val p = e.getPlayer
    val direction = p.getFacing.toString
    val loc = e.getBlock.getLocation
    if (direction == "NORTH" || direction == "SOUTH") {
      if (loc.getY < p.getLocation.getY) {
        nsBreak(p, loc, 0, -1, 0,0,1)
      } else if (loc.getY == p.getLocation.getY) {
        nsBreak(p, loc, 0, 0, 0,0,1)
      } else {
        nsBreak(p, loc, 0, -1, 0,0,1)
      }
    } else if (direction == "EAST" || direction == "WEST") {
      if (loc.getY < p.getLocation.getY) {
        ewBreak(p, loc, 0, -1, 0,1,0)
      } else if (loc.getY == p.getLocation.getY) {
        ewBreak(p, loc, 0, 1, 0,1,0)
      } else {
        ewBreak(p, loc, 0, -1, 0,1,0)
      }
    }
  }

  /*
    南北で破壊する場合のメソッド
   */
  def nsBreak(p:Player,location: Location,x:Int,y:Int,z:Int,lxm:Int,lym:Int): Unit = {
    location.add(x,y,z)
    for (lx <- 0 to lxm) {
      for (ly <- 0 to lym) {
        val cloneLocation = location.clone()
        cloneLocation.add(lx, ly, 0)
        cloneLocation.getBlock.breakNaturally(p.getInventory.getItemInMainHand)
        itemAddDamage(p)
      }
    }
  }

  /*
   東西で破壊する場合のメソッド
  */
  def ewBreak(p:Player,location: Location,x:Int,y:Int,z:Int,lym:Int,lzm:Int): Unit = {
    location.add(x,y,z)
    for (lz <- 0 to lzm) {
      for (ly <- 0 to lym) {
        val cloneLocation = location.clone()
        cloneLocation.add(0, ly, lz)
        cloneLocation.getBlock.breakNaturally(p.getInventory.getItemInMainHand)
        itemAddDamage(p)
      }
    }
  }

  /*
    アイテム耐久値の計算
   */
  private def itemAddDamage(p: Player): Unit = {
    if (p.getInventory.getItemInMainHand != null && p.getInventory.getItemInMainHand.getType.getMaxDurability != 0) {
      val meta = p.getInventory.getItemInMainHand.getItemMeta
      val random = SecureRandom.getInstance("SHA1PRNG")
      val durabilityLevel = meta.getEnchantLevel(Enchantment.DURABILITY)
      val probability = 100 / (durabilityLevel + 1)
      if (random.nextInt(100) <= probability) {
        val itemDamage = meta.asInstanceOf[Damageable].getDamage
        if (itemDamage + 1 > p.getInventory.getItemInMainHand.getType.getMaxDurability) {
          p.getInventory.setItemInMainHand(null)
          p.playSound(p.getLocation, Sound.ENTITY_ITEM_BREAK, 1, 1)
        } else {
          meta.asInstanceOf[Damageable].setDamage(itemDamage + 1)
        }
      }
      p.getInventory.getItemInMainHand.setItemMeta(meta)
    }
  }

}
