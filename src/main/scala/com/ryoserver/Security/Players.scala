package com.ryoserver.Security

import org.bukkit.{ChatColor, Sound}
import org.bukkit.entity.Player

import scala.collection.mutable

object Players {

  var vpnCounter = mutable.Map[String, Int]()
  var freezeList: Array[String] = Array.empty
  var hideList: Array[Player] = Array.empty

  def counterCheckAndAdd(p: Player): Boolean = {
    if (vpnCounter.contains(p.getName))
      vpnCounter(p.getName) = vpnCounter(p.getName) + 1
    else
      vpnCounter(p.getName) = 1

    if (vpnCounter(p.getName) >= 5) {
      vpnCounter.remove(p.getName)
      false
    } else true
  }

  def freezePlayer(p: Player): Unit = {
    Players.freezeList :+= p.getName
    p.sendMessage(ChatColor.RED + "あなたの動作が権限者により制限されました。")
    p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1)
  }

  def unFreezePlayer(p: Player): Unit = {
    Players.freezeList = Players.freezeList.filter(listPlayer => listPlayer != p.getName)
    p.sendMessage(ChatColor.AQUA + "あなたの動作の制限が解除されました。")
    p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1)
  }

  val getPlayerIP: Player => String = (p: Player) =>
    p.getAddress.toString.replace("/", "").replace(s":${p.getAddress.getPort}", "")

  val getPlayerStatus: Player => String = (target: Player) => {
    var msg = ChatColor.YELLOW + "---------------プレイヤー情報---------------\n" +
      ChatColor.GREEN + "プレイヤー名:" + target.getName() + "\n" +
      "UUID:" + target.getUniqueId + "\n" +
      "ワールド名:" + target.getWorld.getName + "\n" +
      "権限:" + target.isOp + "\n" +
      "ゲームモード:" + target.getGameMode + "\n" +
      "飛行:" + target.getAllowFlight + "\n"
    if (Config.config.getBoolean("ipInfo")) {
      val checkVPNAndProxy = new CheckVPNAndProxy
      val ip = getPlayerIP(target)
      val info = checkVPNAndProxy.getIPInfo(ip)
      msg += "IPアドレス:" + ip + "\n" +
        "国名:" + info("country") + "\n" +
        "地域:" + info("city") + "\n" +
        "回線事業者:" + info("isp") + "\n"
    }
    msg += ChatColor.YELLOW + "-----------------------------------------"
    msg
  }


}
