package com.ryoserver.Quest.Event

import org.jetbrains.annotations.NotNull

case class EventType(
                      @NotNull name: String,
                      @NotNull eventType: String,
                      @NotNull start: String,
                      @NotNull end: String,
                      item: String,
                      @NotNull exp: Double,
                      reward: Int,
                      distribution: Int,
                    )
