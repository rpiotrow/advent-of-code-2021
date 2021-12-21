package io.github.rpiotrow.advent2021.day21

import io.github.rpiotrow.advent2021.Input.parseInt
import io.github.rpiotrow.advent2021.Input
import zio.ZIO

case class GameState(
    moves: Int,
    player1Move: Boolean,
    player1Position: Int,
    player2Position: Int,
    player1Score: Int,
    player2Score: Int
):
  def move(diceValues: (Int, Int, Int)): GameState =
    if player1Move then
      val player1NextPosition = nextPosition(player1Position, diceValues)
      val player1NextScore = player1Score + player1NextPosition + 1
      GameState(moves + 1, false, player1NextPosition, player2Position, player1NextScore, player2Score)
    else
      val player2NextPosition = nextPosition(player2Position, diceValues)
      val player2NextScore = player2Score + player2NextPosition + 1
      GameState(moves + 1, true, player1Position, player2NextPosition, player1Score, player2NextScore)

  private def nextPosition(currentPosition: Int, diceValues: (Int, Int, Int)) =
    (currentPosition + diceValues._1 + diceValues._2 + diceValues._3) % 10

object GameState:
  def parseInitial: ZIO[Any, String, GameState] =
    for
      input <- Input.readLines("day21.input").runCollect.map(_.toList)
      gameState <- input match
        case player1Line :: player2Line :: Nil =>
          for
            player1Position <- parsePlayerPosition(1, player1Line)
            player2Position <- parsePlayerPosition(2, player2Line)
          yield GameState(0, true, player1Position - 1, player2Position - 1, 0, 0)
        case _ => ZIO.fail("invalid input")
    yield gameState

  private def parsePlayerPosition(playerNumber: Int, string: String) =
    string match
      case s"Player $playerNumber starting position: $positionStr" =>
        parseInt(positionStr)
      case _ =>
        ZIO.fail(s"cannot parse player $playerNumber position")
