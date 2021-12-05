package io.github.rpiotrow.advent2021.day04

object Bingo:

  val boardSize: Int = 5

  case class Numbers private (unmarked: List[Int]):
    def mark(number: Int): Numbers =
      if unmarked.contains(number) then new Numbers(unmarked.filterNot(_ == number)) else this
  object Numbers:
    def apply(values: List[Int]) = new Numbers(values)

  case class Board private (id: Long, rows: List[Numbers], columns: List[Numbers], isWinner: Boolean):
    def mark(number: Int): Board =
      val newRows = rows.map(_.mark(number))
      val newColumns = columns.map(_.mark(number))
      val newIsWinner = isWinner || check(newRows) || check(newColumns)
      new Board(id, newRows, newColumns, newIsWinner)
    def sumUnmarked: Int = rows.map(_.unmarked.sum).sum
    private def check(list: List[Numbers]): Boolean = list.exists(_.unmarked.isEmpty)
  object Board:
    def apply(id: Long, rows: List[List[Int]]): Board =
      val columns = rows match
        case List(
              List(a11, a21, a31, a41, a51),
              List(a12, a22, a32, a42, a52),
              List(a13, a23, a33, a43, a53),
              List(a14, a24, a34, a44, a54),
              List(a15, a25, a35, a45, a55)
            ) =>
          List(
            Numbers(List(a11, a12, a13, a14, a15)),
            Numbers(List(a21, a22, a23, a24, a25)),
            Numbers(List(a31, a32, a33, a34, a35)),
            Numbers(List(a41, a42, a43, a44, a45)),
            Numbers(List(a51, a52, a53, a54, a55))
          )
        case _ => throw new IllegalArgumentException("it's not a bingo data!")
      new Board(id, rows.map(Numbers.apply), columns, false)

  case class Game private (boards: List[Board], ranking: List[Board]):
    def mark(number: Int): Game =
      val newBoards = boards.map(_.mark(number))
      val newWinners = newBoards.filter(_.isWinner).filterNot(b => ranking.exists(_.id == b.id))
      new Game(newBoards, ranking.appendedAll(newWinners))
  object Game:
    def apply(boards: List[Board]) = new Game(boards, List.empty[Board])

  case class DrawnNumbers(list: List[Int])
