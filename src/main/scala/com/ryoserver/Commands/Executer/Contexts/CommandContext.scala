package com.ryoserver.Commands.Executer.Contexts

trait CommandContext {

  val args: List[String]
  val playerCommand: Boolean = false

  def execute(rawCommandContext: RawCommandContext): Unit

}
