package io.github.rpiotrow.advent2021

import io.github.rpiotrow.advent2021.day21.DiracDice
import zio.test.*
import zio.test.Assertion.*

object Day21DiracDiceSolutionSpec extends DefaultRunnableSpec:
  def spec = suite("DiracDiceSpec")(
    test("DiracDice solution") {
      for solution <- DiracDice.solution
        yield assert(solution)(equalTo((897798L, 0)))
    }
  )
