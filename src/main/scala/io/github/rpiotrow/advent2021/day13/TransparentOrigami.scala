package io.github.rpiotrow.advent2021.day13

import io.github.rpiotrow.advent2021.{Input, Solution}
import zio.stream.ZSink
import zio.{Console, ZIO}

object TransparentOrigami:

  val solution: Solution =
    for
      data <- readInput
      count1 = data.transparentPaper.fold(data.foldInstructions.head).numberOfDots
      code = data.foldInstructions.foldLeft(data.transparentPaper) { case (transparentPaper, instruction) =>
        transparentPaper.fold(instruction)
      }
      _ <- Console.printLine(s"Number of dots after first fold is $count1")
      _ <- code.print
    yield (count1, code.numberOfDots)

  private case class TransparentOrigamiInput(transparentPaper: TransparentPaper, foldInstructions: List[FoldInstruction])

  private def readInput: ZIO[Any, String, TransparentOrigamiInput] =
    Input
      .readLines("day13.input")
      .peel(ZSink.collectAllWhile(_.size > 0))
      .use { case (dots, restOfString) =>
        for
          transparentPaper <- TransparentPaper.fromStrings(dots.toList)
          foldInstructions <- restOfString.runCollect.map(_.toList).flatMap(FoldInstruction.fromStrings)
        yield TransparentOrigamiInput(transparentPaper, foldInstructions)
      }
