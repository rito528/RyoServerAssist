package com.ryoserver.Commands.Executer.Contexts

import org.bukkit.command.{Command, CommandSender}

case class RawCommandContext(sender: CommandSender,
                             executedCommand: ExecutedCommand,
                             args: List[String])

case class ExecutedCommand(command: Command,alias: String)