package io.github.rpiotrow.advent2021.day04

import io.github.rpiotrow.advent2021.day04.BingoSampleInputData.*
import zio.test.*
import zio.test.Assertion.*

object BingoSpec extends DefaultRunnableSpec:

  def spec = suite("day04: BingoSpec")(
    test("numbers") {
      val numbers = startingBoard1.rows.head

      val afterFive = numbers.mark(7).mark(4).mark(9).mark(5).mark(11)
      val afterSixMore = afterFive.mark(17).mark(23).mark(2).mark(0).mark(14).mark(21)

      assert(afterFive.unmarked)(equalTo(List(22, 13, 17, 0))) &&
      assert(afterSixMore.unmarked)(equalTo(List(22, 13)))
    },
    test("board creating") {
      val expected = List(
        Bingo.Numbers(List(22, 8, 21, 6, 1)),
        Bingo.Numbers(List(13, 2, 9, 10, 12)),
        Bingo.Numbers(List(17, 23, 14, 3, 20)),
        Bingo.Numbers(List(11, 4, 16, 18, 15)),
        Bingo.Numbers(List(0, 24, 7, 5, 19))
      )
      assert(startingBoard1.columns)(equalTo(expected))
    },
    test("loosing board") {
      val board = startingBoard1

      val afterFive = board.mark(7).mark(4).mark(9).mark(5).mark(11)
      val afterSixMore = afterFive.mark(17).mark(23).mark(2).mark(0).mark(14).mark(21)
      val afterStillOneMore = afterSixMore.mark(24)

      assert(afterFive.isWinner)(equalTo(false)) &&
      assert(afterSixMore.isWinner)(equalTo(false)) &&
      assert(afterStillOneMore.isWinner)(equalTo(false))
    },
    test("winning board") {
      val board = startingBoard3

      val afterFive = board.mark(7).mark(4).mark(9).mark(5).mark(11)
      val afterSixMore = afterFive.mark(17).mark(23).mark(2).mark(0).mark(14).mark(21)
      val afterStillOneMore = afterSixMore.mark(24)

      assert(afterFive.isWinner)(equalTo(false)) &&
      assert(afterSixMore.isWinner)(equalTo(false)) &&
      assert(afterStillOneMore.isWinner)(equalTo(true))
    },
    test("sum unmarked on board") {
      val markedBoard = startingBoard3
        .mark(7).mark(4).mark(9).mark(5).mark(11)
        .mark(17).mark(23).mark(2).mark(0).mark(14).mark(21)
        .mark(24)

      assert(markedBoard.sumUnmarked)(equalTo(188))
    },
    test("game") {
      val gameStart = Bingo.Game(List(startingBoard1, startingBoard2, startingBoard3))

      val afterFive = gameStart.mark(7).mark(4).mark(9).mark(5).mark(11)
      val afterSixMore = afterFive.mark(17).mark(23).mark(2).mark(0).mark(14).mark(21)
      val afterStillOneMore = afterSixMore.mark(24)

      assert(afterFive.winner.isDefined)(equalTo(false)) &&
      assert(afterSixMore.winner.isDefined)(equalTo(false)) &&
      assert(afterStillOneMore.winner.isDefined)(equalTo(true))
    }
  )
