package io.github.rpiotrow.advent2021

import io.github.rpiotrow.advent2021.day10.SyntaxScoring
import zio.test.Assertion.*
import zio.test.*

object Day10SyntaxScoringSolutionSpec extends DefaultRunnableSpec:
  def spec = suite("SyntaxScoringSpec")(
    test("SyntaxScoring solution") {
      for solution <- SyntaxScoring.solution
      yield assert(solution)(equalTo((311949L, 3042730309L)))
    }
  )
