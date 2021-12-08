package io.github.rpiotrow.advent2021

import io.github.rpiotrow.advent2021.day07.TheTreacheryOfWhales
import zio.test.Assertion.*
import zio.test.*

object Day07TheTreacheryOfWhalesSolutionSpec extends DefaultRunnableSpec:
  def spec = suite("TheTreacheryOfWhalesSpec")(
    test("TheTreacheryOfWhales solution") {
      for solution <- TheTreacheryOfWhales.solution
        yield assert(solution)(equalTo((329389L, 86397080L)))
    }
  )
