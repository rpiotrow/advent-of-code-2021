package io.github.rpiotrow.advent2021.day04

import io.github.rpiotrow.advent2021.Input
import zio.ZIO
import zio.stream.{ZSink, ZStream}

import java.io.IOException

case class BingoSetup(drawnNumbers: Bingo.DrawnNumbers, startState: Bingo.Game)

object BingoSetup:
  def parse: ZIO[Any, String, BingoSetup] =
    Input
      .readLines("day04.input")
      .peel(ZSink.head)
      .use { case (headOption, stream) =>
        for
          drawnNumbers <- parseDrawNumbers(headOption)
          game <- parseBoards(stream)
        yield BingoSetup(drawnNumbers, game)
      }

  private[day04] def parseDrawNumbers(line: Option[String]): ZIO[Any, String, Bingo.DrawnNumbers] =
    for
      head <- ZIO.getOrFailWith("no drawn numbers")(line)
      numbers <- ZIO.foreach(head.split(','))(parseInt)
    yield Bingo.DrawnNumbers(numbers.toList)

  private[day04] def parseBoards(stream: ZStream[Any, String, String]): ZIO[Any, String, Bingo.Game] =
    stream
      .drop(1)
      .grouped(6)
      .zipWithIndex
      .mapZIO { case (chunk, index) =>
        ZIO
          .collectAll(chunk.toList.take(5).map { line =>
            ZIO.foreach(line.split(' ').toList.filter(_.nonEmpty))(parseInt)
          })
          .map(rows => Bingo.Board(index, rows))
      }
      .runCollect
      .map(boards => Bingo.Game(boards.toList))

  private def parseInt(s: String) = ZIO.attempt(s.toInt).mapError(_.getMessage)
