package com.ryoserver.Commands.Executer.Contexts

trait CommandContext {

    def execute(rawCommandContext: RawCommandContext): Unit

    val args: List[String]

    val playerCommand: Boolean = false

}
