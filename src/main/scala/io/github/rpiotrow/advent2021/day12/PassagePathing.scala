package io.github.rpiotrow.advent2021.day12

import io.github.rpiotrow.advent2021.{Input, Solution}
import zio.Console

object PassagePathing:

  val solution: Solution =
    for
      lines <- Input.readLines("day12.input").runCollect.map(_.toList)
      caves <- Caves.parse(lines)
      count1 <- caves.countPathsWithSmallCavesOnlyOnce
      _ <- Console.printLine(s"There are $count1 paths when visiting small caves at most once.")
    yield (count1, 0L)
