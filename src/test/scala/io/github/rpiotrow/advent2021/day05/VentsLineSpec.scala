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
    },
    test("points of diagonal line left, down") {
      val line = DiagonalLine(8, 0, 0, 8)
      assert(line.points)(
        equalTo(
          List(
            Point(8, 0),
            Point(7, 1),
            Point(6, 2),
            Point(5, 3),
            Point(4, 4),
            Point(3, 5),
            Point(2, 6),
            Point(1, 7),
            Point(0, 8)
          )
        )
      )
    },
    test("points of diagonal line left, up") {
      val line = DiagonalLine(6, 4, 2, 0)
      assert(line.points)(equalTo(List(Point(2, 0), Point(3, 1), Point(4, 2), Point(5, 3), Point(6, 4))))
    },
    test("points of diagonal line right, down") {
      val line = DiagonalLine(0, 0, 8, 8)
      assert(line.points)(
        equalTo(
          List(
            Point(0, 0),
            Point(1, 1),
            Point(2, 2),
            Point(3, 3),
            Point(4, 4),
            Point(5, 5),
            Point(6, 6),
            Point(7, 7),
            Point(8, 8)
          )
        )
      )
    },
    test("points of diagonal line right, up") {
      val line = DiagonalLine(5, 5, 8, 2)
      assert(line.points)(equalTo(List(Point(8, 2), Point(7, 3), Point(6, 4), Point(5, 5))))
    }
  )
