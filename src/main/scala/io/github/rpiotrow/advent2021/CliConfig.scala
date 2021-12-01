package io.github.rpiotrow.advent2021

import scopt.OParser

case class CliConfig(day: Option[Int] = None)

object CliConfig:
  private val configBuilder = OParser.builder[CliConfig]
  val parser: OParser[Unit, CliConfig] =
    import configBuilder._
    OParser.sequence(
      programName("advent-of-code-2021"),
      head("Run Advent of Code 2021 solutions for selected day or for all days"),
      help('h', "help"),
      opt[Int]('d', "day")
        .action((x, c) => c.copy(day = Some(x)))
        .text("select day"),
    )
