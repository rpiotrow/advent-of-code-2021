package io.github.rpiotrow.advent2021.day21

import zio.stream.ZStream
import zio.{Clock, Schedule}

object DeterministicDice:
  val stream: ZStream[Clock, Nothing, Int] = ZStream.range(1, 101).repeat(Schedule.forever)
