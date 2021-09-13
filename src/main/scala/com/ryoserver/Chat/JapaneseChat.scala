package com.ryoserver.Chat

import com.ryoserver.RyoServerAssist
import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet}
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.bukkit.ChatColor
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.{EventHandler, EventPriority, Listener}

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class JapaneseChat(ryoServerAssist: RyoServerAssist) extends Listener {
  ryoServerAssist.getServer.getPluginManager.registerEvents(this,ryoServerAssist)

  @EventHandler(priority = EventPriority.HIGHEST)
  def onChat(e: AsyncPlayerChatEvent): Unit = {
    val msg = e.getMessage
    val msgArray = msg.split(Array(' ','　'))
    var changedMsg = ""
    var counter = 0
    msgArray.foreach(s => {
      if (counter != 0 && counter != msgArray.length) changedMsg += " "
      s.toCharArray.foreach(c => {
        if (!(String.valueOf(c).getBytes().length < 2)) {
          e.setMessage(s"$msg")
          return
        }
      })
      counter += 1
      getServerMsg(getURL("google",getServerMsg(getURL("local",s)))).replaceFirst("\\[","").replace("\\],\\],","")
        .replaceAll("\",\\[","\",").replaceAll("],","").replace("]]]","]").split("]\\[").foreach(s2 => {
        changedMsg += s2.replace("[","").replaceAll("\"","").replace("]","").split(",")(1)
      })

    })
    e.setMessage(s"$msg ${ChatColor.YELLOW}($changedMsg)")
  }

  val getURL: (String, String) => String = (api:String, msg: String) => {
    api match {
      case "google" => "http://www.google.com/transliterate?langpair=ja-Hira%7Cja&text=" + URLEncoder.encode(msg,StandardCharsets.UTF_8)
      case "local" => s"http://${ryoServerAssist.getConfig.getString("JapaneseChatServerIP")}:${ryoServerAssist.getConfig.getString("JapaneseChatServerPort")}/$msg"
    }
  }

  def getServerMsg(url :String): String = {
    val charset = StandardCharsets.UTF_8
    val httpclient = HttpClients.createDefault()
    val getRequest = new HttpGet(url)

    var response: CloseableHttpResponse = null
    try {
      response = httpclient.execute(getRequest)
      val responseBody = EntityUtils.toString(response.getEntity, charset)
      httpclient.close()
      responseBody
    } catch {
      case _: Exception => ""
    }
  }

}
