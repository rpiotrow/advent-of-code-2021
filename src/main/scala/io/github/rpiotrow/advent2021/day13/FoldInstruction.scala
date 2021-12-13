package io.github.rpiotrow.advent2021.day13

import io.github.rpiotrow.advent2021.Input.parseInt
import zio.ZIO

enum FoldInstruction:
  case FoldHorizontal(y: Int)
  case FoldVertical(x: Int)

object FoldInstruction:
  def fromStrings(strings: List[String]): ZIO[Any, String, List[FoldInstruction]] =
    ZIO.foreach(strings) {
      case s"fold along y=$y" => parseInt(y).map(FoldHorizontal.apply)
      case s"fold along x=$x" => parseInt(x).map(FoldVertical.apply)
      case _                  => ZIO.fail("invalid fold instruction")
    }
