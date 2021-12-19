package io.github.rpiotrow.advent2021.day17

import io.github.rpiotrow.advent2021.day17.TrickShot.heuristicallyChosenVelocities
import zio.Console
import zio.stream.ZStream
import zio.test.*
import zio.test.Assertion.*

object ProbeSpec extends DefaultRunnableSpec:

  private def heuristicallyChosenVelocities: List[Velocity] =
    (for
      dx <- 1.to(50)
      dy <- (-50).to(50)
    yield Velocity(dx, dy)).toList

  def spec = suite("day17: Probe")(
    test("check") {
      val targetArea = TargetArea(Point(20, -5), Point(30, -10))
      for
        count <- ZStream
          .fromIterable(heuristicallyChosenVelocities)
          .map(velocity => (velocity, Probe.highestYReachingTarget(velocity, targetArea)))
          .filter {
            case (_, Some(_)) => true
            case _            => false
          }
          .runCount
      yield assert(count)(equalTo(112))
    }
  )
