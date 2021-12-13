package io.github.rpiotrow.advent2021.day13

import io.github.rpiotrow.advent2021.Input.parseInt
import io.github.rpiotrow.advent2021.day13.FoldInstruction.*
import zio.{Console, ZIO}

case class Dot(x: Int, y: Int)

opaque type TransparentPaper = Set[Dot]
object TransparentPaper:
  def apply(dots: Set[Dot]): TransparentPaper = dots
  def fromStrings(strings: List[String]): ZIO[Any, String, TransparentPaper] =
    ZIO
      .foreach(strings) {
        case s"$x,$y" =>
          for
            x <- parseInt(x)
            y <- parseInt(y)
          yield Dot(x, y)
        case _ => ZIO.fail("invalid dot on transparent paper")
      }
      .map(_.toSet)

  extension (dots: TransparentPaper)
    def numberOfDots = dots.size

    def fold(instruction: FoldInstruction): TransparentPaper = instruction match
      case FoldHorizontal(foldY) =>
        dots.map {
          case Dot(x, y) if y > foldY => Dot(x, y - 2 * (y - foldY))
          case d                      => d
        }
      case FoldVertical(foldX) =>
        dots.map {
          case Dot(x, y) if x > foldX => Dot(x - 2 * (x - foldX), y)
          case d                      => d
        }

    def print: ZIO[Console, java.io.IOException, Unit] =
      val maxX = dots.map(_.x).max
      val maxY = dots.map(_.y).max
      ZIO
        .foreach(0.to(maxY)) { y =>
          ZIO.foreach(0.to(maxX)) { x =>
            if dots.contains(Dot(x, y)) then Console.print("X") else Console.print(".")
          } *> Console.printLine("")
        }
        .unit
