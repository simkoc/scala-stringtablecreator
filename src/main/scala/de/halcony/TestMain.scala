package de.halcony

object TestMain {

  def main(args : Array[String]) : Unit = {
    printFirstTestTable()
    printSecondTestTable()
  }


  def printFirstTestTable() : Unit = {
    println()
    println("3 rows, normal borders, align left")
    val table = new StringTable(List("first","second","third"))
    table.addRow(List("alle","meine", "entchen"))
    println(table.getTable)
  }

  def printSecondTestTable() : Unit = {
    println()
    println("5 rows, normal borders, align left, break within cell")
    val table = new StringTable(List("first","second","third","fourth","fifth"),maxWidth = 3)
    table.addRow(List("123456","123","123","1234567","1234567890"))
    table.addRow(List("123456","123","123","1234567","1234567890"))
    println(table.getTable)
  }

}
