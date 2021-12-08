package io.github.rpiotrow.advent2021.day08

import io.github.rpiotrow.advent2021.{Input, Solution}
import zio.Console
import zio.stream.ZStream

object SevenSegmentSearch:

  case class InputLine(patterns: List[String], output: List[String])
  object InputLine:
    def parse: ZStream[Any, String, InputLine] =
      Input
        .readLines("day08.input")
        .map {
          case s"$p1 $p2 $p3 $p4 $p5 $p6 $p7 $p8 $p9 $p10 | $o1 $o2 $o3 $o4" =>
            InputLine(List(p1,p2,p3,p4,p5,p6,p7,p8,p9,p10), List(o1,o2,o3,o4))
        }

  val solution: Solution =
    for
      count <- InputLine
        .parse
        .flatMap(line => ZStream.fromIterable(line.output))
        .filter(o => o.length == 2 || o.length == 4 || o.length == 3 || o.length == 7)
        .runCount
      _ <- Console.printLine(s"Digits 1, 4, 7, or 8 appear in the output values $count times")
    yield (count, 0L)
