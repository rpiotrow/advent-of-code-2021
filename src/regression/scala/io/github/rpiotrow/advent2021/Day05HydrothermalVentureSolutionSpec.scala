package io.github.rpiotrow.advent2021

import io.github.rpiotrow.advent2021.day05.HydrothermalVenture
import zio.test.Assertion.equalTo
import zio.test.{DefaultRunnableSpec, assert}

object Day05HydrothermalVentureSolutionSpec extends DefaultRunnableSpec:
  def spec = suite("HydrothermalVentureSolutionSpec")(
    test("HydrothermalVenture solution") {
      for solution <- HydrothermalVenture.solution
      yield assert(solution)(equalTo((5169L, 0L)))
    }
  )
