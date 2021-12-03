package io.github.rpiotrow.advent2021

import io.github.rpiotrow.advent2021.day01.SonarSweep
import zio.test.Assertion.*
import zio.test.*

object Day01SonarSweepSolutionSpec extends DefaultRunnableSpec:
  def spec = suite("SonarSweepSolutionSpec")(
    test("SonarSweep solution") {
      for solution <- SonarSweep.solution
      yield assert(solution)(equalTo((1475L, 1516L)))
    }
  )
