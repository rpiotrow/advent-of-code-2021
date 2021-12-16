package io.github.rpiotrow.advent2021.day14

import io.github.rpiotrow.advent2021.{Input, Solution}
import zio.{Console, ZIO}
import zio.stream.ZSink

object ExtendedPolymerization:

  val solution: Solution =
    Input
      .readLines("day14.input")
      .peel(ZSink.head)
      .use { case (firstLine, restOfLines) =>
        for
          templateLine <- ZIO.getOrFailWith("no template")(firstLine)
          template = PolymerTemplate(templateLine)
          rules <- InsertionRules.parse(restOfLines.drop(1))
          counter = PolymerElementsCounter.from(template, rules)

          counter10 = counter.steps(9)
          stats10 = counter10.stats
          leastCommon10 = stats10.head
          mostCommon10 = stats10.last
          subtraction10 = mostCommon10._2 - leastCommon10._2
          _ <- Console.printLine(s"Subtraction of most and least common element after 10 steps is $subtraction10")

          stats40 = counter10.steps(30).stats
          leastCommon40 = stats40.head
          mostCommon40 = stats40.last
          subtraction40 = mostCommon40._2 - leastCommon40._2
          _ <- Console.printLine(s"Subtraction of most and least common element after 40 steps is $subtraction40")
        yield (subtraction10, subtraction40)
      }
