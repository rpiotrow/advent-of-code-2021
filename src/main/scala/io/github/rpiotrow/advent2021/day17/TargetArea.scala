package io.github.rpiotrow.advent2021.day17

import io.github.rpiotrow.advent2021.Input.parseInt
import zio.ZIO

case class TargetArea(leftTop: Point, rightBottom: Point)

object TargetArea:
  def parse(string: String): ZIO[Any, String, TargetArea] =
    string match
      case s"target area: x=$x1s..$x2s, y=$y1s..$y2s" =>
        for
          x1 <- parseInt(x1s)
          x2 <- parseInt(x2s)
          y1 <- parseInt(y1s)
          y2 <- parseInt(y2s)
        yield TargetArea(Point(x1, y2), Point(x2, y1))
      case _ => ZIO.fail("invalid input")
