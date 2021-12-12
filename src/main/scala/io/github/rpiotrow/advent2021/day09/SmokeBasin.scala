package io.github.rpiotrow.advent2021.day09

import io.github.rpiotrow.advent2021.{Input, Solution}
import io.github.rpiotrow.advent2021.day09.Heightmap.*
import zio.Console

object SmokeBasin:

  val solution: Solution =
    for
      heightmap <- Input.readLines("day09.input").toHeightmap
      lowPointsRiskLevelSum = heightmap.lowPoints.map(_.height + 1).sum
      _ <- Console.printLine(s"The sum of the risk levels of all low points is $lowPointsRiskLevelSum")
    yield (lowPointsRiskLevelSum, 0L)
