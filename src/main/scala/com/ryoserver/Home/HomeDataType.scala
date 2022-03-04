package com.ryoserver.Home

import java.util.UUID

case class HomeDataType(UUID: UUID,
                        point: Int,
                        world: String,
                        x: Double,
                        y: Double,
                        z: Double,
                        isLocked: Boolean)
