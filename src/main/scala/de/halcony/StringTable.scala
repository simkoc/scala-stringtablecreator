package de.halcony

import scala.collection.mutable.ListBuffer

class StringTable(header : Seq[String], padding : Int = 1, maxWidth : Int = 10, horizontalBorder : String = "-", verticalBorder : String = "|", cornerElement : String = "+", align : String = "left") {

  private val rows : ListBuffer[Seq[Cell]] = {
    val buff = new ListBuffer[Seq[Cell]]()
    buff.addOne(header.map(Cell))
    buff
  }

  case class Cell(content : String) {
    private val rows: List[String] = content.split("\n").toList.flatMap {
      line =>
        if (line.length <= maxWidth) {
          List(line)
        } else {
          line.toList.grouped(maxWidth).map(_.mkString(""))
        }
    }
    def getRowIterator: Iterator[String] = rows.iterator
    def getWidth : Int = rows.head.length
  }

  private def getColumnMaxWidth : Seq[Int] = {
    rows.foldLeft(rows.head.map(_ => 0)) {
      case (lhs, novel) =>
        (lhs zip novel).map {
          case (width, cell) => List(width, cell.getWidth).max
        }
    }
  }

  def addRow(row : Seq[String]) : Unit = {
    if(row.length == rows.head.length) {
      rows.addOne(row.map(Cell))
    } else {
      throw new RuntimeException("The row has to have the same amount of elements as the header")
    }
  }

  private def printSeparatorRow(width : Seq[Int]) : String = {
    width.map(count => horizontalBorder.repeat(count + 2)).mkString(cornerElement,cornerElement,cornerElement) + "\n"
  }

  private def printCellRow(row : Seq[Cell], widths : Seq[Int]) : String = {
    val sb : StringBuilder = new StringBuilder
    val iterators = row.map(_.getRowIterator)
    while(iterators.exists(_.hasNext)) {
      val line = (widths zip iterators).map {
        case (width, iterator) =>
          val content: String = if (iterator.hasNext) {
            iterator.next()
          } else {
            ""
          }
          align match {
            case "left" => " ".repeat(padding) + content + " ".repeat( List(0,width - content.length).max + padding)
            case "right" => " ".repeat( List(0,width - content.length).max + padding) + content + " ".repeat(padding)
          }
      }.mkString(verticalBorder, verticalBorder, verticalBorder)
      sb.append(line + "\n")
    }
    sb.toString()
  }

  def getTable : String = {
    val widths = getColumnMaxWidth
    val separatorRow = printSeparatorRow(widths)
    rows.map(row => printCellRow(row, widths)).mkString(separatorRow, separatorRow, separatorRow)
  }
}
