package io.github.rpiotrow.advent2021

import io.github.rpiotrow.advent2021.day09.SmokeBasin
import zio.test.Assertion.*
import zio.test.*

object Day09SmokeBasinSolutionSpec extends DefaultRunnableSpec:
  def spec = suite("SmokeBasinSpec")(
    test("SmokeBasin solution") {
      for solution <- SmokeBasin.solution
      yield assert(solution)(equalTo((575L, 0L)))
    }
  )
