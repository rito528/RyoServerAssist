package com.ryoserver.Distribution

object DistributionData {

  //SQLのprimary keyは1から始まるのに対して、Scalaのインデックスは0から始まるため、整合性を取るために0枚のガチャ券として初期化する
  var distributionData: List[DistributionType] = List(DistributionType(0, "normal", 0))

  var addedList: List[Int] = List.empty

}
