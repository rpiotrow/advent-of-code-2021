package io.github.rpiotrow.advent2021.day04

object BingoSampleInputData:

  private[day04] val startingBoard1 = Bingo.Board(
    rows = List(
      List(22, 13, 17, 11, 0),
      List(8, 2, 23, 4, 24),
      List(21, 9, 14, 16, 7),
      List(6, 10, 3, 18, 5),
      List(1, 12, 20, 15, 19)
    )
  )
  private[day04] val startingBoard2 = Bingo.Board(
    rows = List(
      List(3, 15, 0, 2, 22),
      List(9, 18, 13, 17, 5),
      List(19, 8, 7, 25, 23),
      List(20, 11, 10, 24, 4),
      List(14, 21, 16, 12, 6)
    )
  )
  private[day04] val startingBoard3 = Bingo.Board(
    rows = List(
      List(14, 21, 17, 24, 4),
      List(10, 16, 15, 9, 19),
      List(18, 8, 23, 26, 20),
      List(22, 11, 13, 6, 5),
      List(2, 0, 12, 3, 7)
    )
  )
