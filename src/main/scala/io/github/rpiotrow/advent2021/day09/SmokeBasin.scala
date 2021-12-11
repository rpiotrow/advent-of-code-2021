package io.github.rpiotrow.advent2021.day09

import io.github.rpiotrow.advent2021.{Input, Solution}
import zio.stream.ZStream
import zio.{Console, ZIO}

object SmokeBasin:

  val solution: Solution =
    for
      grid <- Input.readLines("day09.input").toIntGridZipper
      lowPointsRiskLevelSum = sumLowPointsRiskLevel(grid)
      _ <- Console.printLine(s"The sum of the risk levels of all low points is $lowPointsRiskLevelSum")
    yield (lowPointsRiskLevelSum, 0L)

  extension (stream: ZStream[Any, String, String])
    private[day09] def toIntGridZipper: ZIO[Any, String, GridZipper[Int]] =
      stream
        .map(_.toList.map(char => char.toInt - '0'.toInt))
        .runCollect
        .map(_.toList)
        .flatMap(GridZipper.fromList)

  private[day09] def sumLowPointsRiskLevel(grid: GridZipper[Int]): Int =
    grid
      .coflatMap { grid =>
        val e = grid.extract
        if grid.neighbors.forall(_ > e) then e + 1 else 0
      }
      .toList
      .sum
