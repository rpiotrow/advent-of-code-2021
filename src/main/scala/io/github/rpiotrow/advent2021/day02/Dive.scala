package io.github.rpiotrow.advent2021.day02

import io.github.rpiotrow.advent2021.{Input, Solution}
import zio.stream.ZStream
import zio.{Console, ZIO}

object Dive:

  val solution: Solution =
    Input.readLines("day02.input").broadcast(2, 10).use {
      case streamCopy1 :: streamCopy2 :: Nil =>
        for
          f1 <- process(streamCopy1, SimpleSubmarine()).fork
          f2 <- process(streamCopy2, AimSubmarine()).fork
          multiplication1 <- f1.join
          multiplication2 <- f2.join
          _ <- Console.printLine(s"Multiplication of final horizontal position by final depth is $multiplication1")
          _ <- Console.printLine(s"Multiplication of final horizontal position by final depth using aim is $multiplication2")
        yield (multiplication1, multiplication2)
      case _ => ZIO.dieMessage("impossible")
    }

  private def process(commands: ZStream[Any, String, String], startState: Submarine): ZIO[Any, String, Long] =
    commands
      .runFold(startState) { case (submarine, command) =>
        command match
          case s"forward $n" => submarine.forward(n.toInt)
          case s"down $n"    => submarine.down(n.toInt)
          case s"up $n"      => submarine.up(n.toInt)
      }
      .map { submarine =>
        val finalPosition = submarine.position
        finalPosition.horizontalPosition * finalPosition.depth
      }
