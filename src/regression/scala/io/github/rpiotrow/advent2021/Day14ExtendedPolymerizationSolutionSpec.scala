package io.github.rpiotrow.advent2021

import io.github.rpiotrow.advent2021.day14.ExtendedPolymerization
import zio.test.*
import zio.test.Assertion.*

object Day14ExtendedPolymerizationSolutionSpec extends DefaultRunnableSpec:
  def spec = suite("ExtendedPolymerizationSpec")(
    test("ExtendedPolymerization solution") {
      for solution <- ExtendedPolymerization.solution
      yield assert(solution)(equalTo((2509L, 0L)))
    }
  )
