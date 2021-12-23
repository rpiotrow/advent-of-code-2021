package io.github.rpiotrow.advent2021

import io.github.rpiotrow.advent2021.day20.TrenchMap
import zio.test.*
import zio.test.Assertion.*

object Day20TrenchMapSolutionSpec extends DefaultRunnableSpec:
  def spec = suite("TrenchMapSpec")(
    test("TrenchMap solution") {
      for solution <- TrenchMap.solution
        yield assert(solution)(equalTo((5597L, 18723L)))
    }
  )
