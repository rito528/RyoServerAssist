package com.ryoserver.tpa

import com.ryoserver.RyoServerAssist
import org.bukkit.Bukkit
import org.bukkit.ChatColor._
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

import scala.collection.mutable

object Tpa {

  var tpaList: mutable.Map[String, String] = mutable.Map.empty[String, String]
  var cancelingTpa: Set[String] = Set.empty

  def sendTpa(sendPlayer: Player, targetPlayer: Player, ryoServerAssist: RyoServerAssist): Unit = {
    if (tpaList.contains(targetPlayer.getName)) {
      sendPlayer.sendMessage(s"${RED}送信したプレイヤーはすでにtpaを受けているためtpaを送信できませんでした。")
      return
    }
    if (tpaList.values.exists(_ == sendPlayer.getName)) {
      sendPlayer.sendMessage(s"${RED}あなたは既にtpaを送信しているため、tpaを送信できませんでした。")
      return
    }
    if (cancelingTpa.contains(targetPlayer.getName)) {
      sendPlayer.sendMessage(s"${RED}tpaを送信したプレイヤーはtpaを拒否に設定しているため、tpaを送信できませんでした。")
      return
    }
    tpaList += (targetPlayer.getName -> sendPlayer.getName)
    targetPlayer.sendMessage(s"$AQUA${sendPlayer.getName}からtpaが送信されました。")
    targetPlayer.sendMessage(s"${AQUA}依頼を受ける場合は/tpa acceptを、断る場合は/tpa cancelを入力してください。")
    targetPlayer.sendMessage(s"${AQUA}また、この依頼は5分後に自動で削除されます。")
    sendPlayer.sendMessage(s"${AQUA}tpaを送信しました。")
    new BukkitRunnable {
      override def run(): Unit = {
        if (!tpaList.contains(targetPlayer.getName)) return
        tpaList = tpaList.filterNot { case (target, _) => target == targetPlayer.getName }
        sendPlayer.sendMessage(s"${RED}tpa依頼を自動キャンセルしました。")
        targetPlayer.sendMessage(s"${RED}tpa依頼を自動キャンセルしました。")
      }
    }.runTaskLater(ryoServerAssist, 20 * 60 * 5)
  }

  def acceptTpa(acceptPlayer: Player): Unit = {
    if (!tpaList.contains(acceptPlayer.getName)) {
      acceptPlayer.sendMessage(s"${RED}現在tpa依頼を受けていません！")
      return
    }
    acceptPlayer.sendMessage(s"${AQUA}tpa依頼を受けました！")
    val sendPlayer = tpaList.get(acceptPlayer.getName)
    sendPlayer match {
      case Some(player) =>
        Bukkit.getPlayer(player).teleport(acceptPlayer.getLocation())
        Bukkit.getPlayer(player).sendMessage(s"$AQUA${acceptPlayer.getName}にテレポートしました！")
        tpaList = tpaList.filterNot { case (target, _) => target == acceptPlayer.getName }
      case None =>
    }
  }

  def cancelTpa(cancelPlayer: Player): Unit = {
    if (!tpaList.contains(cancelPlayer.getName)) {
      cancelPlayer.sendMessage(s"${RED}現在tpa依頼を受けていないため、キャンセルできませんでした。")
      return
    }
    val sendPlayer = tpaList.get(cancelPlayer.getName)
    sendPlayer match {
      case Some(player) =>
        tpaList = tpaList.filterNot { case (target, _) => target == cancelPlayer.getName }
        cancelPlayer.sendMessage(s"${AQUA}tpa依頼をキャンセルしました。")
        Bukkit.getPlayer(player).sendMessage(s"${RED}tpa依頼がキャンセルされました。")
      case None =>
    }
  }

}
