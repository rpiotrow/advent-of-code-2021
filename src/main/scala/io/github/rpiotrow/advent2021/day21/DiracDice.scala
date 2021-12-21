package io.github.rpiotrow.advent2021.day21

import io.github.rpiotrow.advent2021.Solution
import zio.{Console, ZIO}

object DiracDice:

  val solution: Solution =
    for
      initial <- GameState.parseInitial
      finalState <- DeterministicDice.stream
        .grouped(3)
        .map(chunk => (chunk(0), chunk(1), chunk(2)))
        .mapAccum(initial) { case (gameState, diceValues) =>
          val g = gameState.move(diceValues)
          (g, g)
        }
        .dropWhile { gameState => gameState.player1Score < 1000 && gameState.player2Score < 1000 }
        .runHead
        .flatMap(ZIO.getOrFailWith("no solution!!!"))
      multiplication = Math.min(finalState.player1Score, finalState.player2Score) * finalState.moves * 3
      _ <- Console.printLine(
        s"Multiplication of the score of the losing player and the number of times the die was rolled is $multiplication"
      )
    yield (multiplication, 0L)
