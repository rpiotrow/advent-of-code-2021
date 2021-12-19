package io.github.rpiotrow.advent2021

import io.github.rpiotrow.advent2021.day17.TrickShot
import zio.test.*
import zio.test.Assertion.*

object Day17TrickShotSolutionSpec extends DefaultRunnableSpec:
  def spec = suite("TrickShotSpec")(
    test("TrickShot solution") {
      for solution <- TrickShot.solution
        yield assert(solution)(equalTo((9870L, 5523L)))
    }
  )
