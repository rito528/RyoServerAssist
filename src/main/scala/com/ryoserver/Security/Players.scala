package com.ryoserver.Security

import com.ryoserver.Config.ConfigData.getConfig
import org.bukkit.ChatColor._
import org.bukkit.Sound
import org.bukkit.entity.Player

import scala.collection.mutable

object Players {

  val getPlayerIP: Player => String = (p: Player) =>
    p.getAddress.toString.replace("/", "").replace(s":${p.getAddress.getPort}", "")

  val getPlayerStatus: Player => String = (target: Player) => {
    var msg = s"$YELLOW---------------プレイヤー情報---------------\n" +
      s"${GREEN}プレイヤー名:${target.getName}\n" +
      s"UUID:${target.getUniqueId}\n" +
      s"ワールド名:${target.getWorld.getName}\n" +
      s"権限:${target.isOp}\n" +
      s"ゲームモード:${target.getGameMode}\n" +
      s"飛行:${target.getAllowFlight}\n"
    if (getConfig.ipInfo) {
      val checkVPNAndProxy = new GetPlayerInfo
      val ip = getPlayerIP(target)
      val info = checkVPNAndProxy.getIPInfo(ip)
      msg += s"IPアドレス:$ip\n" +
        s"国名:${info("country")}\n" +
        s"地域:${info("city")}\n" +
        s"回線事業者:${info("isp")}\n"
    }
    msg += s"$YELLOW-----------------------------------------"
    msg
  }

  var freezeList: Array[String] = Array.empty
  var hideList: Array[Player] = Array.empty

  def freezePlayer(p: Player): Unit = {
    Players.freezeList :+= p.getName
    p.sendMessage(s"${RED}あなたの動作が権限者により制限されました。")
    p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1)
  }

  def unFreezePlayer(p: Player): Unit = {
    Players.freezeList = Players.freezeList.filter(listPlayer => listPlayer != p.getName)
    p.sendMessage(s"${AQUA}あなたの動作の制限が解除されました。")
    p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1)
  }


}
