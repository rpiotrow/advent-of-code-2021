package io.github.rpiotrow.advent2021.day07

import zio.test.Assertion.*
import zio.test.*

object CrabsPositionSpec extends DefaultRunnableSpec:

  private val crabs = List(
    Crabs(position = 0, amount = 1),
    Crabs(position = 1, amount = 2),
    Crabs(position = 2, amount = 3),
    Crabs(position = 4, amount = 1),
    Crabs(position = 7, amount = 1),
    Crabs(position = 14, amount = 1),
    Crabs(position = 16, amount = 1)
  )

  def spec = suite("day05: CrabsPositionSpec")(
    test("initial") {
      val p0 = CrabsPosition(0, crabs)
      assert(CrabsPosition.linearFuelCost(p0))(equalTo(49L)) &&
        assert(CrabsPosition.increasingFuelCost(p0))(equalTo(290L))
    },
    test("position 2") {
      val p2 = CrabsPosition(2, crabs)
      assert(CrabsPosition.linearFuelCost(p2))(equalTo(37L)) &&
        assert(CrabsPosition.increasingFuelCost(p2))(equalTo(206L))
    },
    test("position 5") {
      val p5 = CrabsPosition(5, crabs)
      assert(CrabsPosition.increasingFuelCost(p5))(equalTo(168L))
    }

  )

