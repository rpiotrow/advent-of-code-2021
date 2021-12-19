package io.github.rpiotrow.advent2021.day17

import io.github.rpiotrow.advent2021.{Input, Solution}
import zio.stream.ZStream
import zio.{Clock, Console, ZIO}

import java.util.concurrent.TimeUnit

object TrickShot:

  val solution: Solution =
    for
      inputLine <- Input.readLines("day17.input").runHead
      input <- ZIO.getOrFailWith("no input!!!")(inputLine)
      targetArea <- TargetArea.parse(input)
      startTime <- Clock.currentTime(TimeUnit.MILLISECONDS)
      highestY <- ZStream
        .fromIterable(heuristicallyChosenVelocities)
        .map(velocity => Probe.highestYReachingTarget(velocity, targetArea))
        .collectSome
        .runCollect
        .map(_.max)
      endTime <- Clock.currentTime(TimeUnit.MILLISECONDS)
      _ <- Console.printLine(s"Highest y position reaching target area is $highestY (it took ${endTime - startTime}ms)")
    yield (highestY, 0L)

  private def heuristicallyChosenVelocities: List[Velocity] =
    (for
      dx <- 1.to(200)
      dy <- 1.to(200)
    yield Velocity(dx, dy)).toList
