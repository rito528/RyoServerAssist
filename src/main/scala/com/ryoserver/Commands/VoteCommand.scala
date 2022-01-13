package com.ryoserver.Commands

import com.ryoserver.Commands.Builder.{CommandBuilder, CommandExecutorBuilder}
import com.ryoserver.Vote.Vote

class VoteCommand extends CommandBuilder {

  override val executor: CommandExecutorBuilder = CommandExecutorBuilder(
    Map(
      "testVote" -> testVote
    )
  ).playerCommand()

  def testVote(): Unit = {
    new Vote().vote("投票テスト",sender.getName)
  }

}
