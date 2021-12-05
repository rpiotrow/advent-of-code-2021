package io.github.rpiotrow.advent2021.day05

import zio.test.Assertion.*
import zio.test.*

object VentsLineSpec extends DefaultRunnableSpec:

  def spec = suite("day05: VentsLineSpec")(
    test("points of vertical line") {
      val line = VerticalLine(x = 7, yStart = 0, yEnd = 4)
      assert(line.points)(equalTo(List(Point(7, 0), Point(7, 1), Point(7, 2), Point(7, 3), Point(7, 4))))
    },
    test("points of vertical line backward") {
      val line = VerticalLine(x = 7, yStart = 4, yEnd = 0)
      assert(line.points)(equalTo(List(Point(7, 0), Point(7, 1), Point(7, 2), Point(7, 3), Point(7, 4))))
    },
    test("points of horizontal line") {
      val line = HorizontalLine(y = 4, xStart = 1, xEnd = 3)
      assert(line.points)(equalTo(List(Point(1, 4), Point(2, 4), Point(3, 4))))
    },
    test("points of horizontal line backward") {
      val line = HorizontalLine(y = 4, xStart = 3, xEnd = 1)
      assert(line.points)(equalTo(List(Point(1, 4), Point(2, 4), Point(3, 4))))
    }
  )
