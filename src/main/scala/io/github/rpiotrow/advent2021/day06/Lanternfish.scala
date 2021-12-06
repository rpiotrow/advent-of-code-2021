package io.github.rpiotrow.advent2021.day06

import io.github.rpiotrow.advent2021.Solution
import zio.stream.{ZSink, ZStream}
import zio.{Console, ZEnv, ZIO}

object Lanternfish:

  val solution: Solution =
    for
      lanternfishHerd <- LanternfishHerd.parse
      solution <- ZStream
        .iterate(lanternfishHerd)(s => s.nextGeneration)
        .drop(80)
        .peel(ZSink.head)
        .use[ZEnv, String | java.io.IOException, (Long, Long)] { case (generation80Option, restOfStream) =>
          for
            generation80 <- ZIO.getOrFailWith("no solution found for 80 days")(generation80Option)
            count1 = generation80.size
            _ <- Console.printLine(s"There will be $count1 lanternfish after 80 days")
            generation256Option <- restOfStream.drop(256 - 80 - 1).runHead
            generation256 <- ZIO.getOrFailWith("no solution found for 256 days")(generation256Option)
            count2 = generation256.size
            _ <- Console.printLine(s"There will be $count2 lanternfish after 256 days")
          yield (count1, count2)
        }
    yield solution
