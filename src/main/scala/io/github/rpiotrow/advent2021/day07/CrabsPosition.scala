package io.github.rpiotrow.advent2021.day07

import io.github.rpiotrow.advent2021.Input.parseInt
import zio.ZIO
import zio.stream.ZStream

case class Crabs(position: Int, amount: Long)
object Crabs:
  def parseGroupAndOrder(string: String): ZIO[Any, String, List[Crabs]] =
    for
      ints <- ZIO.foreach(string.split(',').toList)(parseInt)
      crabs = ints.groupBy(identity).map { case (position, list) => Crabs(position, list.size) }.toList.sortBy(_.position)
    yield crabs

case class CrabsPosition(position: Int, crabs: List[Crabs])
object CrabsPosition:
  def linearFuelCost(crabsPosition: CrabsPosition): Long =
    crabsPosition.crabs.foldLeft(0L) { case (sum, crabs) =>
      val linearCost = Math.abs(crabsPosition.position - crabs.position)
      sum + linearCost * crabs.amount
    }

  def increasingFuelCost(crabsPosition: CrabsPosition): Long =
    crabsPosition.crabs.foldLeft(0L) { case (sum, crabs) =>
      val delta = Math.abs(crabsPosition.position - crabs.position)
      val increasingCost = (1 + delta) * delta / 2
      sum + increasingCost * crabs.amount
    }
