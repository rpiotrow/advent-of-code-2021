package io.github.rpiotrow.advent2021.day01

import io.github.rpiotrow.advent2021.{Input, Solution}
import zio.{Console, ZIO}
import zio.stream.ZSink

object SonarSweep:

  private def countIncreases =
    Input
      .readLines("day01.input")
      .mapZIO(line => ZIO.attempt(line.toLong).orElseFail(s"invalid number: $line"))
      .zipWithPrevious
      .map {
        case (Some(previous), current) if previous < current => true
        case _                                               => false
      }
      .filter(identity)
      .runCount

  val solution: Solution =
    for
      count <- countIncreases
      _ <- Console.printLine(s"The number of times a depth measurement increases is $count")
    yield (count, 0L)
