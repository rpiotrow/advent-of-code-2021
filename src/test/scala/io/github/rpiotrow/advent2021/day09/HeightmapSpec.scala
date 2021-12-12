package io.github.rpiotrow.advent2021.day09

import io.github.rpiotrow.advent2021.day09.Heightmap.*
import zio.ZIO
import zio.stream.ZStream
import zio.test.*
import zio.test.Assertion.*

object HeightmapSpec extends DefaultRunnableSpec:

  private val testGridZipper: ZIO[Any, String, GridZipper[Position]] = GridZipper.fromList(
    List(
      List(Position(0,0,2), Position(0,1,1), Position(0,2,9), Position(0,3,9), Position(0,4,9), Position(0,5,4), Position(0,6,3), Position(0,7,2), Position(0,8,1), Position(0,9,0)),
      List(Position(1,0,3), Position(1,1,9), Position(1,2,8), Position(1,3,7), Position(1,4,8), Position(1,5,9), Position(1,6,4), Position(1,7,9), Position(1,8,2), Position(1,9,1)),
      List(Position(2,0,9), Position(2,1,8), Position(2,2,5), Position(2,3,6), Position(2,4,7), Position(2,5,8), Position(2,6,9), Position(2,7,8), Position(2,8,9), Position(2,9,2)),
      List(Position(3,0,8), Position(3,1,7), Position(3,2,6), Position(3,3,7), Position(3,4,8), Position(3,5,9), Position(3,6,6), Position(3,7,7), Position(3,8,8), Position(3,9,9)),
      List(Position(4,0,9), Position(4,1,8), Position(4,2,9), Position(4,3,9), Position(4,4,9), Position(4,5,6), Position(4,6,5), Position(4,7,6), Position(4,8,7), Position(4,9,8))
    )
  )

  def spec = suite("day05: HeightmapSpec")(
    test("parse") {
      for
        expected <- testGridZipper
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
          .toHeightmap
      yield assert(parsed)(equalTo(expected))
    },
    test("low points") {
      for
        grid <- testGridZipper
        heightmap = Heightmap(grid)
      yield assert(heightmap.lowPoints)(equalTo(List(Position(0, 1, 1), Position(0, 9, 0), Position(2, 2, 5), Position(4, 6, 5))))
    }
  )
