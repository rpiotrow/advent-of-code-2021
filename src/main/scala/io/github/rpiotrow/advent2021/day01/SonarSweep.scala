package io.github.rpiotrow.advent2021.day01

import io.github.rpiotrow.advent2021.Solution
import zio.{Console, ZIO}

object SonarSweep:

  val solution: Solution =
    Depths.read.broadcast(2, 10).use {
      case streamCopy1 :: streamCopy2 :: Nil =>
        for
          fiber1 <- Depths.countIncreases(streamCopy1).fork
          fiber2 <- Depths.countSlidingWindowIncreases(streamCopy2, 3).fork
          count1 <- fiber1.join
          count2 <- fiber2.join
          _ <- fiber2.join
          _ <- Console.printLine(s"The number of times a depth measurement increases is $count1")
          _ <- Console.printLine(s"The number of times a sliding window depth increases is $count2")
        yield (count1, count2)
      case _ => ZIO.dieMessage("impossible")
    }
