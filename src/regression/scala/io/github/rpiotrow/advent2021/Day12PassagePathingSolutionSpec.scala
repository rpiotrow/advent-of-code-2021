package io.github.rpiotrow.advent2021

import io.github.rpiotrow.advent2021.day12.PassagePathing
import zio.test.Assertion.*
import zio.test.*

object Day12PassagePathingSolutionSpec extends DefaultRunnableSpec:
  def spec = suite("PassagePathingSpec")(
    test("PassagePathing solution") {
      for solution <- PassagePathing.solution
        yield assert(solution)(equalTo((4720L, 0L)))
    }
  )

