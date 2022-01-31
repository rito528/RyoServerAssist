package com.ryoserver.util

import com.ryoserver.RyoServerAssist

object Logger {

  private var logger:java.util.logging.Logger = _

  def getLogger: java.util.logging.Logger = {
    logger
  }

  def setLogger(ryoServerAssist: RyoServerAssist): Unit = {
    logger = ryoServerAssist.getLogger
  }

}
