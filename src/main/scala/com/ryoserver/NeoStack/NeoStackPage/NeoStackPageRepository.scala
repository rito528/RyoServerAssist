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
    NeoStackPageEntity.pageData.foreach{case (rawNeoStackPageContext,invItems) =>
      val invItemsString = invItems.mkString(";")
      sql"UPDATE StackList SET invItem=$invItemsString WHERE category=${rawNeoStackPageContext.category.name} AND page=${rawNeoStackPageContext.page}"
      .execute()
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
      NeoStackPageEntity.pageData += (RawNeoStackPageContext(Category.values.filter(_.name == category).head,page),invItems)
    })
  }

  override def getCategoryPageBy(category:Category, page: Int): List[ItemStack] = {
    NeoStackPageEntity.pageData(RawNeoStackPageContext(category,page))
  }

}
