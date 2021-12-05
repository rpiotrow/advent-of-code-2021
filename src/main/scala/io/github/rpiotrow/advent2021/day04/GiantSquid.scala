package io.github.rpiotrow.advent2021.day04

import zio.{Console, ZIO}
import zio.stream.{ZSink, ZStream}
import io.github.rpiotrow.advent2021.Solution

object GiantSquid:

  val solution: Solution =
    for
      setup <- BingoSetup.parse
      solution <- ZStream
        .fromIterable(setup.drawnNumbers.list)
        .peel(ZSink.fold(Option.empty[Int] -> setup.startState) { case (_, game) =>
          game.ranking.isEmpty
        }(foldGame))
        .use { case ((firstWinningBoardNumberOption, firstWinningBoardState), restOfStream) =>
          for
            firstWinningBoardNumber <- ZIO.getOrFailWith("first winning board number does not exist")(
              firstWinningBoardNumberOption
            )
            firstWinningBoard <- ZIO.getOrFailWith("first winning board not exists")(firstWinningBoardState.ranking.headOption)
            firstWinningBoardScore = score(firstWinningBoardNumber, firstWinningBoard)
            lastNumberAndState <- restOfStream
              .runFoldWhile(firstWinningBoardNumberOption -> firstWinningBoardState) { case (_, game) =>
                game.ranking.size < game.boards.size
              }(foldGame)
            (lastWinningBoardNumberOption, lastWinningBoardState) = lastNumberAndState
            lastWinningBoardNumber <- ZIO.getOrFailWith("last winning board number does not exist")(lastWinningBoardNumberOption)
            lastWinningBoard <- ZIO.getOrFailWith("last winning board not exists")(lastWinningBoardState.ranking.lastOption)
            lastWinningBoardScore = score(lastWinningBoardNumber, lastWinningBoard)
          yield (firstWinningBoardScore.toLong, lastWinningBoardScore.toLong)
        }
    yield solution

  private def foldGame(state: (Option[Int], Bingo.Game), number: Int): (Option[Int], Bingo.Game) =
    val (_, game) = state
    Some(number) -> game.mark(number)

  private def score(number: Int, board: Bingo.Board) =
    board.sumUnmarked * number
