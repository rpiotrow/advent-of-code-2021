package io.github.rpiotrow.advent2021

import io.github.rpiotrow.advent2021.day04.GiantSquid
import zio.test.Assertion.*
import zio.test.*

object Day04GiantSquidSolutionSpec extends DefaultRunnableSpec:
  def spec = suite("GiantSquidSolutionSpec")(
    test("GiantSquid solution") {
      for solution <- GiantSquid.solution
      yield assert(solution)(equalTo((51776L, 0L)))
    }
  )
