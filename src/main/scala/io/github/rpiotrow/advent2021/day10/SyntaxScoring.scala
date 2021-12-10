package io.github.rpiotrow.advent2021.day10

import io.github.rpiotrow.advent2021.{Input, Solution}
import zio.{Console, ZIO}

object SyntaxScoring:

  val solution: Solution =
    Input
      .readLines("day10.input")
      .mapZIO(ChunkState.fromString)
      .broadcast(2, 10)
      .use {
        case streamCopy1 :: streamCopy2 :: Nil =>
          for
            fiber1 <- streamCopy1
              .map(_.errorScore)
              .runSum
              .fork
            fiber2 <- streamCopy2
              .filter(_.errorScore == 0)
              .mapZIO(_.completionScore)
              .runCollect
              .map(_.toList.sorted)
              .flatMap { sortedList =>
                val size = sortedList.size
                if size % 2 == 0 then ZIO.fail("There suppose to be odd number of incomplete chunks!!!")
                else ZIO.succeed(sortedList(size / 2))
              }
              .fork
            errorScore <- fiber1.join
            completionScore <- fiber2.join
            _ <- Console.printLine(s"The total syntax error score is $errorScore")
            _ <- Console.printLine(s"The total completion score is $completionScore")
          yield (errorScore, completionScore)
        case _ => ZIO.dieMessage("impossible")
      }
