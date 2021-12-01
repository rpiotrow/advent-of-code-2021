package io.github.rpiotrow.advent2021

import io.github.rpiotrow.advent2021.day01.SonarSweep
import zio.*

type Solution = ZIO[ZEnv, String | java.io.IOException, (Long, Long)]

private val days: Map[Int, Solution] = Map(
  1 -> SonarSweep.solution
)

private def solution(day: Int) =
  for
    _ <- Console.printLine(s"Day: $day")
    _ <- days.getOrElse(day, ZIO.fail("There is no such day!!!"))
  yield ()

object Main extends ZIOAppDefault:
  def run =
    ZIO.collectAll(days.keys.map(solution))
