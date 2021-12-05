package io.github.rpiotrow.advent2021.day05

import io.github.rpiotrow.advent2021.Input
import zio.ZIO

case class Point(x: Int, y: Int)

sealed trait VentsLine:
  def points: List[Point]

private def fromTo(a: Int, b: Int): List[Int] =
  val start = if a < b then a else b
  val end = if a < b then b else a
  (start to end).toList

case class VerticalLine(x: Int, yStart: Int, yEnd: Int) extends VentsLine:
  def points: List[Point] = fromTo(yStart, yEnd).map(y => Point(x, y))

case class HorizontalLine(y: Int, xStart: Int, xEnd: Int) extends VentsLine:
  def points: List[Point] = fromTo(xStart, xEnd).map(x => Point(x, y))

case class DiagonalLine(xStart: Int, yStart: Int, xEnd: Int, yEnd: Int) extends VentsLine:
  def points: List[Point] = ???

case class Vents(vertical: List[VerticalLine], horizontal: List[HorizontalLine], diagonal: List[DiagonalLine])

object Vents:
  private def parseInt(s: String) = ZIO.attempt(s.toInt).mapError(_.getMessage)
  def parse: ZIO[Any, String, Vents] =
    Input
      .readLines("day05.input")
      .mapZIO {
        case s"$x1s,$y1s -> $x2s,$y2s" =>
          for
            x1 <- parseInt(x1s)
            y1 <- parseInt(y1s)
            x2 <- parseInt(x2s)
            y2 <- parseInt(y2s)
          yield
            if x1 == x2 then VerticalLine(x1, y1, y2)
            else if y1 == y2 then HorizontalLine(y1, x1, x2)
            else DiagonalLine(x1, y1, x2, y2)
        case s => ZIO.fail(s"cannot parse $s as vents line")
      }
      .runFold((List.empty[VerticalLine], List.empty[HorizontalLine], List.empty[DiagonalLine])) { case ((vs, hs, ds), line) =>
        line match
          case v: VerticalLine   => (v :: vs, hs, ds)
          case h: HorizontalLine => (vs, h :: hs, ds)
          case d: DiagonalLine   => (vs, hs, d :: ds)
      }
      .map { case (vs, hs, ds) => Vents(vs, hs, ds) }
