package io.github.rpiotrow.advent2021.day03

import io.github.rpiotrow.advent2021.{Input, Solution}
import zio.Console

object BinaryDiagnostic:

  val solution: Solution =
    for
      columnsCounts <- ColumnCounts.compute("day03.input")
      powerConsumption = columnsCounts.gammaRate * columnsCounts.epsilonRate
      _ <- Console.printLine(s"The power consumption of the submarine is $powerConsumption")
    yield (powerConsumption, 0L)
