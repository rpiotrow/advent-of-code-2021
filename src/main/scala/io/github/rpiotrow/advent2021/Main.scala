package io.github.rpiotrow.advent2021

import scopt.OParser
import io.github.rpiotrow.advent2021.day01.SonarSweep
import zio.*

type Solution = ZIO[ZEnv, String | java.io.IOException, (Long, Long)]

private val days: Map[Int, Solution] = Map(
  1 -> SonarSweep.solution
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