package io.github.rpiotrow.advent2021.day03

import zio.stream.ZStream
import zio.test.Assertion.*
import zio.test.*

object ColumnCountsSpec extends DefaultRunnableSpec:
  private val counts = ColumnCounts(
    List(
      Column(zeros = 5, ones = 7),
      Column(zeros = 7, ones = 5),
      Column(zeros = 4, ones = 8),
      Column(zeros = 5, ones = 7),
      Column(zeros = 7, ones = 5)
    )
  )

  def spec = suite("day03: BinaryDiagnostic")(
    test("gamma rate binary") {
      assert(counts.gammaRateBinary)(equalTo("10110"))
    },
    test("gamma rate") {
      assert(counts.gammaRate)(equalTo(22))
    },
    test("epsilon rate") {
      assert(counts.epsilonRate)(equalTo(9))
    }
  )
