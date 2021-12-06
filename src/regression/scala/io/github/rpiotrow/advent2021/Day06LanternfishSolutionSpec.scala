package io.github.rpiotrow.advent2021

import io.github.rpiotrow.advent2021.day06.Lanternfish
import zio.test.*
import zio.test.Assertion.*

object Day06LanternfishSolutionSpec extends DefaultRunnableSpec:
  def spec = suite("LanternfishSolutionSpec")(
    test("Lanternfish solution") {
      for solution <- Lanternfish.solution
      yield assert(solution)(equalTo((363101L, 1644286074024L)))
    }
  )
