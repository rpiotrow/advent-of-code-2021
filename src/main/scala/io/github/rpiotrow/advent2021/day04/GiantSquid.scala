package io.github.rpiotrow.advent2021.day04

import zio.{Console, ZIO}
import zio.stream.ZStream
import io.github.rpiotrow.advent2021.Solution

object GiantSquid:

  val solution: Solution =
    for
      setup <- BingoSetup.parse
      winningStateWithNumber <- ZStream
        .fromIterable(setup.drawnNumbers.list)
        .runFoldWhile(Option.empty[Int] -> setup.startState)(_._2.winner.isEmpty) { case ((_, game), number) =>
          Some(number) -> game.mark(number)
        }
      number <- ZIO.getOrFailWith("last used number not exists")(winningStateWithNumber._1)
      winningBoard <- ZIO.getOrFailWith("winning board not exists")(winningStateWithNumber._2.winner)
      winningBoardScore = winningBoard.sumUnmarked * number
      _ <- Console.printLine(s"The score of the winning board is $winningBoardScore")
    yield (winningBoardScore, 0L)
