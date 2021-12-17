package com.ryoserver.SkillSystems.Skill

import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.{EventHandler, Listener}
import org.bukkit.inventory.meta.Damageable
import org.bukkit.{Location, Sound}

import java.security.SecureRandom

class BreakSkillProvider extends Listener {

  @EventHandler
  def onBreak(e: BlockBreakEvent): Unit = {
    break(e.getPlayer,"a",10,e.getBlock.getLocation,BreakRange(1,2,1))
  }

  def break(p:Player,skillName:String,spCost:Int,breakBlockLocation:Location,breakRange: BreakRange): Unit = {
    if (p.getLocation.getY == breakBlockLocation.getY) return
    val direction = p.getFacing.toString
    if (direction == "NORTH" || direction == "SOUTH") {
      val breakPoint = breakBlockLocation.add(-(breakRange.x / 2),-(breakRange.y / 2),0)
      for (y <- 0 until breakRange.y) {
        for (x <- 0 until breakRange.x) {
          val pointClone = breakPoint.clone()
          pointClone.add(x,y,0)
          pointClone.getBlock.breakNaturally(p.getInventory.getItemInMainHand)
          itemAddDamage(p)
        }
      }
    } else if (direction == "EAST" || direction == "WEST") {
      val breakPoint = breakBlockLocation.add(0,-(breakRange.y / 2),-(breakRange.z / 2))
      for (y <- 0 until breakRange.y) {
        for (z <- 0 until breakRange.z) {
          val pointClone = breakPoint.clone()
          pointClone.add(0,y,z)
          pointClone.getBlock.breakNaturally(p.getInventory.getItemInMainHand)
          itemAddDamage(p)
        }
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
        } else if (!p.getInventory.getItemInMainHand.getItemMeta.isUnbreakable) {
          meta.asInstanceOf[Damageable].setDamage(itemDamage + 1)
        }
      }
      p.getInventory.getItemInMainHand.setItemMeta(meta)
    }
  }

}
