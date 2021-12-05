package io.github.rpiotrow.advent2021.day05

import zio.ZIO
import zio.stream.ZStream

class Diagram(private val vents: Vents):

  def countOverlappingPointsForVerticalAndHorizontalVentsLines(): ZIO[Any, Nothing, Int] =
    countOverlappingPointsForVentsLines(vents.vertical ++ vents.horizontal)

  def countOverlappingPointsForVerticalHorizontalAndDiagonalVentsLines(): ZIO[Any, Nothing, Int] =
    countOverlappingPointsForVentsLines(vents.vertical ++ vents.horizontal ++ vents.diagonal)

  private def countOverlappingPointsForVentsLines(lines: List[VentsLine]): ZIO[Any, Nothing, Int] =
    ZStream.fromIterable(lines)
      .flatMap(ventsLine => ZStream.fromIterable(ventsLine.points))
      .runFold(Map.empty[Point, Int]) { case (map, point) =>
        map.updatedWith(point) {
          case Some(currentValue) => Some(currentValue + 1)
          case None               => Some(1)
        }
      }
      .map(_.count { case (_, count) => count > 1 })
