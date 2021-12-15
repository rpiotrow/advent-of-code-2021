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
          stats = template.process(rules, 10).stats
          leastCommon = stats.head
          mostCommon = stats.last
          subtraction = mostCommon._2 - leastCommon._2
          _ <- Console.printLine(s"Subtraction of most and least common element after 10 steps is $subtraction")
        yield (subtraction, 0L)
      }
