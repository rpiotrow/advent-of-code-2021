package io.github.rpiotrow.advent2021

import io.github.rpiotrow.advent2021.day03.BinaryDiagnostic
import zio.test.Assertion.*
import zio.test.*

object Day03BinaryDiagnosticSolutionSpec extends DefaultRunnableSpec:
  def spec = suite("BinaryDiagnosticSolutionSpec")(
    test("BinaryDiagnostic solution") {
      for solution <- BinaryDiagnostic.solution
        yield assert(solution)(equalTo((741950L, 0L)))
    }
  )
