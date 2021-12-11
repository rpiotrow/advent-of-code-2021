package io.github.rpiotrow.advent2021.day09

import io.github.rpiotrow.advent2021.day09.SmokeBasin.toIntGridZipper
import zio.ZIO
import zio.stream.ZStream
import zio.test.Assertion.*
import zio.test.*

object SmokeBasinSpec extends DefaultRunnableSpec:

  private val testFridZipper: ZIO[Any, String, GridZipper[Int]] = GridZipper.fromList(
    List(
      List(2, 1, 9, 9, 9, 4, 3, 2, 1, 0),
      List(3, 9, 8, 7, 8, 9, 4, 9, 2, 1),
      List(9, 8, 5, 6, 7, 8, 9, 8, 9, 2),
      List(8, 7, 6, 7, 8, 9, 6, 7, 8, 9),
      List(9, 8, 9, 9, 9, 6, 5, 6, 7, 8)
    )
  )

  def spec = suite("day05: SmokeBasinSpec")(
    test("parse") {
      for
        expected <- testFridZipper
        parsed <- ZStream
          .fromIterable(
            List(
              "2199943210",
              "3987894921",
              "9856789892",
              "8767896789",
              "9899965678"
            )
          )
          .toIntGridZipper
      yield assert(parsed)(equalTo(expected))
    },
    test("risk level sum") {
      for
        grid <- testFridZipper
        lowPointsRiskLevelSum = SmokeBasin.sumLowPointsRiskLevel(grid)
      yield assert(lowPointsRiskLevelSum)(equalTo(15))

    }
  )
