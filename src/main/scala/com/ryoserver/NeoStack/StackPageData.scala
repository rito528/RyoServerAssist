package com.ryoserver.NeoStack

import scala.collection.mutable

object StackPageData {

  var stackPageData: mutable.Map[String, mutable.Map[Int, String]] = mutable.Map.empty //カテゴリ,ページ数,itemList

}
