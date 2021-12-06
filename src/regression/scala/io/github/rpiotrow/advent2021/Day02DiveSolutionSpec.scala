package io.github.rpiotrow.advent2021

import io.github.rpiotrow.advent2021.day02.Dive
import zio.test.Assertion.*
import zio.test.*

object Day02DiveSolutionSpec extends DefaultRunnableSpec:
  def spec = suite("DiveSolutionSpec")(
    test("Dive solution") {
      for solution <- Dive.solution
      yield assert(solution)(equalTo((1840243L, 1727785422L)))
    }
  )
