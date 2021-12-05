package io.github.rpiotrow.advent2021.day05

import io.github.rpiotrow.advent2021.Solution
import zio.Console

object HydrothermalVenture:

  val solution: Solution =
    for
      vents <- Vents.parse
      diagram = Diagram(vents)
      count1 <- diagram.countOverlappingPointsForVerticalAndHorizontalVentsLines()
      _ <- Console.printLine(s"The number of points where at least two lines overlap is $count1")
    yield (count1, 0L)
