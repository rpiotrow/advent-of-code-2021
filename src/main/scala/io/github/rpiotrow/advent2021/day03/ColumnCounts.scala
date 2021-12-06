package io.github.rpiotrow.advent2021.day03

import io.github.rpiotrow.advent2021.Input
import zio.ZIO

case class Column(zeros: Int, ones: Int):
  def addZero(): Column = this.copy(zeros = this.zeros + 1)
  def addOne(): Column = this.copy(ones = this.ones + 1)

class ColumnCounts(private val counts: List[Column]):

  def gammaRate: Int =
    Integer.parseInt(gammaRateBinary, 2)

  def epsilonRate: Int =
    val epsilonBinary = gammaRateBinary.map {
      case '0' => '1'
      case '1' => '0'
    }
    Integer.parseInt(epsilonBinary, 2)

  private[day03] def gammaRateBinary: String =
    counts.map { column =>
      if column.zeros > column.ones then '0' else '1'
    }.mkString

object ColumnCounts:
  private val numberOfBits = 12
  def compute(inputFileName: String): ZIO[Any, String, ColumnCounts] =
    for
      counts <- Input
        .readLines(inputFileName)
        .map(_.toList)
        .runFold(List.fill(numberOfBits)(Column(0, 0))) { case (acc, binary) =>
          acc.zip(binary).map { case (column, bit) =>
            bit match
              case '0' => column.addZero()
              case '1' => column.addOne()
          }
        }
    yield new ColumnCounts(counts)
