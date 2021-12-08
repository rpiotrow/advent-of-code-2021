package io.github.rpiotrow.advent2021

import io.github.rpiotrow.advent2021.day08.SevenSegmentSearch
import zio.test.Assertion.equalTo
import zio.test.{DefaultRunnableSpec, assert}

object Day08SevenSegmentSearchSolutionSpec extends DefaultRunnableSpec:
  def spec = suite("SevenSegmentSearchSpec")(
    test("SevenSegmentSearch solution") {
      for solution <- SevenSegmentSearch.solution
        yield assert(solution)(equalTo((476L, 0L)))
    }
  )
