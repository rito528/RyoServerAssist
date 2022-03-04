package com.ryoserver.Quest.Event

case class EventType(
                      name: String,
                      eventType: String,
                      start: String,
                      end: String,
                      item: String,
                      exp: Double,
                      reward: Int,
                      distribution: Int,
                    )
