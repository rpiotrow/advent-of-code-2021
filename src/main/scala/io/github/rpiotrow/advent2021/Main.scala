package io.github.rpiotrow.advent2021

import scopt.OParser
import io.github.rpiotrow.advent2021.day01.SonarSweep
import io.github.rpiotrow.advent2021.day02.Dive
import io.github.rpiotrow.advent2021.day03.BinaryDiagnostic
import io.github.rpiotrow.advent2021.day04.GiantSquid
import io.github.rpiotrow.advent2021.day05.HydrothermalVenture
import io.github.rpiotrow.advent2021.day06.Lanternfish
import io.github.rpiotrow.advent2021.day07.TheTreacheryOfWhales
import io.github.rpiotrow.advent2021.day08.SevenSegmentSearch
import io.github.rpiotrow.advent2021.day10.SyntaxScoring
import zio.*

type Solution = ZIO[ZEnv, String | java.io.IOException, (Long, Long)]

private val days: Map[Int, Solution] = Map(
  1 -> SonarSweep.solution,
  2 -> Dive.solution,
  3 -> BinaryDiagnostic.solution,
  4 -> GiantSquid.solution,
  5 -> HydrothermalVenture.solution,
  6 -> Lanternfish.solution,
  7 -> TheTreacheryOfWhales.solution,
  8 -> SevenSegmentSearch.solution,
  10 -> SyntaxScoring.solution
)

object Main extends ZIOAppDefault:

  private def parseArgs(args: Seq[String]) =
    OParser.parse(CliConfig.parser, args, CliConfig()) match
      case Some(config) =>
        ZIO.succeed(config)
      case _ =>
        ZIO.fail("Invalid parameters!!!")

  private def solution(day: Int) =
    for
      _ <- Console.printLine(s"Day: $day")
      _ <- days.getOrElse(day, ZIO.fail("There is no such day!!!"))
    yield ()

  def run =
    for
      args <- getArgs
      cli <- parseArgs(args)
      result <- cli.day.map(solution).getOrElse(ZIO.foreach(days.keys)(solution))
    yield result
