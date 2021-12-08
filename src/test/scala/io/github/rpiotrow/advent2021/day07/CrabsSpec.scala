package io.github.rpiotrow.advent2021.day07

import zio.test.Assertion.*
import zio.test.{DefaultRunnableSpec, *}

object CrabsSpec extends DefaultRunnableSpec:

  private val crabs = List(
    Crabs(position = 0, amount = 1),
    Crabs(position = 1, amount = 2),
    Crabs(position = 2, amount = 3),
    Crabs(position = 4, amount = 1),
    Crabs(position = 7, amount = 1),
    Crabs(position = 14, amount = 1),
    Crabs(position = 16, amount = 1)
  )

  def spec = suite("day05: CrabsSpec")(
    test("parse") {
      assertM(Crabs.parseGroupAndOrder("16,1,2,0,4,2,7,1,2,14"))(equalTo(crabs))
    }
  )
