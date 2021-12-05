package io.github.rpiotrow.advent2021.day05

import io.github.rpiotrow.advent2021.Solution
import zio.Console

object HydrothermalVenture:

  val solution: Solution =
    for
      vents <- Vents.parse
      diagram = Diagram(vents)
      count1 <- diagram.countOverlappingPointsForVerticalAndHorizontalVentsLines()
      count2 <- diagram.countOverlappingPointsForVerticalHorizontalAndDiagonalVentsLines()
      _ <- Console.printLine(s"The number of points where at least two (vertical or horizontal) lines overlap is $count1")
      _ <- Console.printLine(s"The number of points where at least two (any) lines overlap is $count2")
    yield (count1, count2)
