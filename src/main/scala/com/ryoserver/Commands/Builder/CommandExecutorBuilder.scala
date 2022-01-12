package com.ryoserver.Commands.Builder

/*
  CommandBuilderをmixinした際に求められるcase class
  args(0)に入る引数をcaseClassの引数としてMapに格納して呼び出す。
 */

case class CommandExecutorBuilder(args: Map[String, () => Unit]) {

  var nonArgumentExecutor: () => Unit = _
  var playerCommandData = false

  /*
    このメソッドをインスタンスとして指定し、引数に関数を与えるとコマンド引数がなかった場合の挙動を登録します。
   */
  def setNonArgumentExecutor(executor: () => Unit): CommandExecutorBuilder = {
    nonArgumentExecutor = executor
    this
  }

  /*
    このメソッドをインスタンスとして利用した場合は、プレイヤーからのみ実行できるコマンドになります。
   */
  def playerCommand(): CommandExecutorBuilder = {
    playerCommandData = true
    this
  }

}
