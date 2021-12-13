package io.github.rpiotrow.advent2021

import io.github.rpiotrow.advent2021.day13.TransparentOrigami
import zio.test.*
import zio.test.Assertion.*

object Day13TransparentOrigamiSolutionSpec extends DefaultRunnableSpec:
  def spec = suite("TransparentOrigamiSpec")(
    test("TransparentOrigami solution") {
      for solution <- TransparentOrigami.solution
      // the second number is not the answer to the puzzle (it is just size of the resulting set)
      // the answer is REUPUPKR, it is encoded in set of points (aka ASCII art ;))(see print function in TransparentPaper)
      yield assert(solution)(equalTo((775L, 102L)))
    }
  )
