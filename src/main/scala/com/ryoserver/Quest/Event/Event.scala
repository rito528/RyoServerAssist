package com.ryoserver.Quest.Event

import com.ryoserver.Quest.EventType
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack

import java.util.Date

case class Event(name: String,eventType: Option[EventType] = None,start: Date, end: Date,
                 deliveryItem: Option[ItemStack] = None, suppressionEntity: Option[EntityType] = None,
                 rewardExp: Option[Double] = None,rewardPoint: Option[Int] = None,
                 rewardGachaAmount: Option[Int] = None,ratio: Option[Double] = None) {

  def getDeliveryEvent(deliveryItem: ItemStack,rewardExp: Int,rewardPoint: Int,rewardGachaAmount: Int): Event = {
    this.copy(deliveryItem = Option(deliveryItem), eventType = Option(EventType.delivery) ,rewardExp = Option(rewardExp),
      rewardPoint = Option(rewardPoint), rewardGachaAmount = Option(rewardGachaAmount))
  }

  def getSuppressionEvent(suppressionEntity: EntityType,rewardExp: Int,rewardPoint: Int,rewardGachaAmount: Int): Event = {
    this.copy(suppressionEntity = Option(suppressionEntity),eventType = Option(EventType.suppression),rewardExp = Option(rewardExp),
      rewardPoint = Option(rewardPoint), rewardGachaAmount = Option(rewardGachaAmount))
  }

}

object Event {

  def getDefault(name: String,eventType: EventType,start: Date,end: Date): Event = {
    Event(
      name = name,
      start = start,
      end = end
    )
  }

}
