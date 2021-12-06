package io.github.rpiotrow.advent2021.day06

import io.github.rpiotrow.advent2021.Solution
import zio.{Console, ZIO}
import zio.stream.ZStream

object Lanternfish:

  val solution: Solution =
    for
      lanternfishHerd <- LanternfishHerd.parse
      generation80Option <- ZStream
        .iterate(lanternfishHerd)(s => s.nextGeneration)
        .drop(80)
        .runHead
      generation80 <- ZIO.getOrFailWith("no solution found")(generation80Option)
      count1 = generation80.size
      _ <- Console.printLine(s"There will be $count1 lanternfish after 80 days")
    yield (count1, 0L)
