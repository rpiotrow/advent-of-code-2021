package io.github.rpiotrow.advent2021.day05

import zio.test.Assertion.*
import zio.test.*

object DiagramSpec extends DefaultRunnableSpec:

  private def diagram: Diagram = Diagram(
    Vents(
      /*
        2,2 -> 2,1
        7,0 -> 7,4
       */
      vertical = List(VerticalLine(2, 2, 1), VerticalLine(7, 0, 4)),
      /*
        3,4 -> 1,4
        9,4 -> 3,4
        0,9 -> 2,9
        0,9 -> 5,9
       */
      horizontal = List(HorizontalLine(4, 3, 1), HorizontalLine(4, 9, 3), HorizontalLine(9, 0, 2), HorizontalLine(9, 0, 5)),
      diagonal = List.empty[DiagonalLine]
    )
  )

  def spec = suite("day05: DiagramSpec")(
    test("overlapping horizontal and vertical") {
      assertM(diagram.countOverlappingPointsForVerticalAndHorizontalVentsLines())(equalTo(5))
    }
  )
