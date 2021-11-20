package com.ryoserver.Commands.Builder

case class CommandExecutorBuilder(args: Map[String, () => Unit]) {

  var nonArgumentExecutor: () => Unit = _
  var playerCommandData = false

  def setNonArgumentExecutor(executor: () => Unit): CommandExecutorBuilder = {
    nonArgumentExecutor = executor
    this
  }

  def playerCommand(): CommandExecutorBuilder = {
    playerCommandData = true
    this
  }

}
