package com.ryoserver.NeoStack.NeoStackPage

import com.ryoserver.NeoStack.{Category, RawNeoStackPageContext}
import com.ryoserver.util.Item
import org.bukkit.inventory.ItemStack
import scalikejdbc.{AutoSession, scalikejdbcSQLInterpolationImplicitDef}

class NeoStackPageRepository extends TNeoStackPageRepository {

  private implicit val session: AutoSession.type = AutoSession

  /**
   * ページデータをデータベースに格納します。
   */
  override def store(category: Category,page: Int): Unit = {
    NeoStackPageEntity.pageData.foreach{case (_,invItems) =>
      val invItemsString = invItems.map(data => if (data != null) Item.getStringFromItemStack(data) else null).mkString(";")
      sql"""INSERT INTO StackList (category,page,invItem) VALUES (${category.name},$page,$invItemsString)
           ON DUPLICATE KEY UPDATE
           invItem=$invItemsString""".execute().apply()
    }
  }

  /**
   * 既存のメモリ上のPageデータを上書きします。
   */
  override def restore(): Unit = {
    NeoStackPageEntity.pageData.clear()
    val stackListTable = sql"SELECT * FROM StackList"
    stackListTable.foreach(rs => {
      val category = rs.string("category")
      val page = rs.int("page")
      val invItems = rs.string("invItem").split(';').map(Item.getItemStackFromString).toList
      NeoStackPageEntity.pageData += RawNeoStackPageContext(Category.values.filter(_.name == category).head,page) -> invItems
    })
  }

  override def getCategoryPageBy(category:Category, page: Int): List[ItemStack] = {
    if (NeoStackPageEntity.pageData.contains(RawNeoStackPageContext(category,page))) {
      NeoStackPageEntity.pageData(RawNeoStackPageContext(category, page))
    } else {
      List.empty
    }
  }

  override def changeItem(category: Category,page: Int,invItems: List[ItemStack]): Unit = {
    NeoStackPageEntity.pageData += RawNeoStackPageContext(category,page) -> invItems
  }

  override def getAllItems: Set[ItemStack] = {
    NeoStackPageEntity.pageData.values.flatten.toSet
  }

}
