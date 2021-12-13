package io.github.rpiotrow.advent2021.day12

import io.github.rpiotrow.advent2021.day12.Caves.CountPaths.QueueEntry
import zio.test.Assertion.*
import zio.test.*

object CavesSpec extends DefaultRunnableSpec:

  private val expectedCaves = Caves(
    Map(
      Cave("start") -> Set(Cave("A"), Cave("b")),
      Cave("c") -> Set(Cave("A")),
      Cave("A") -> Set(Cave("start"), Cave("c"), Cave("end"), Cave("b")),
      Cave("b") -> Set(Cave("start"), Cave("A"), Cave("end"), Cave("d")),
      Cave("d") -> Set(Cave("b")),
      Cave("end") -> Set(Cave("A"), Cave("b"))
    )
  )

  def spec = suite("day12: CavesSpec")(
    test("parse") {
      for
        caves <- Caves.parse(
          List(
            "start-A",
            "start-b",
            "A-c",
            "A-b",
            "b-d",
            "A-end",
            "b-end"
          )
        )
      yield assert(caves)(equalTo(expectedCaves))
    },
    test("count paths with small caves only once") {
      assertM(expectedCaves.countPathsWithSmallCavesOnlyOnce)(equalTo(10L))
    },
    test("count paths with one small cave twice") {
      assertM(expectedCaves.countPathsWithOneSmallCaveTwice)(equalTo(36L))
    }
  )
