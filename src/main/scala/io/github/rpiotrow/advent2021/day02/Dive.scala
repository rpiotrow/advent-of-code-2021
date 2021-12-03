package io.github.rpiotrow.advent2021.day02

import io.github.rpiotrow.advent2021.{Input, Solution}
import zio.Console

object Dive:

  private[day02] case class Submarine(horizontalPosition: Int, depth: Int):
    def forward(value: Int): Submarine = this.copy(horizontalPosition = this.horizontalPosition + value)
    def down(value: Int): Submarine = this.copy(depth = this.depth + value)
    def up(value: Int): Submarine = this.copy(depth = this.depth - value)
  private[day02] object Submarine:
    val startPosition: Submarine = Submarine(0, 0)

  val solution: Solution =
    for
      finalPosition <- Input
        .readLines("day02.input")
        .runFold(Submarine.startPosition) { case (position, command) =>
          command match
            case s"forward $n" => position.forward(n.toInt)
            case s"down $n"    => position.down(n.toInt)
            case s"up $n"      => position.up(n.toInt)
        }
      multiplication = finalPosition.horizontalPosition * finalPosition.depth
      _ <- Console.printLine(s"Multiplication of final horizontal position by final depth is $multiplication")
    yield (multiplication, 0L)

