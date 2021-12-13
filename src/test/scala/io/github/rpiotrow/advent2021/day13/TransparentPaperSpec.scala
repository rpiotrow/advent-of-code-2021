package io.github.rpiotrow.advent2021.day13

import zio.test.*
import zio.test.Assertion.*

object TransparentPaperSpec extends DefaultRunnableSpec:

  private val transparentPaper = TransparentPaper(
    Set(
      Dot(6, 10),
      Dot(0, 14),
      Dot(9, 10),
      Dot(0, 3),
      Dot(10, 4),
      Dot(4, 11),
      Dot(6, 0),
      Dot(6, 12),
      Dot(4, 1),
      Dot(0, 13),
      Dot(10, 12),
      Dot(3, 4),
      Dot(3, 0),
      Dot(8, 4),
      Dot(1, 10),
      Dot(2, 14),
      Dot(8, 10),
      Dot(9, 0)
    )
  )

  def spec = suite("day13: TransparentPaper")(
    test("fold horizontal") {
      val fold = transparentPaper.fold(FoldInstruction.FoldHorizontal(7))
      assert(fold.numberOfDots)(equalTo(17))
    },
    test("fold vertical") {
      val fold = transparentPaper
        .fold(FoldInstruction.FoldHorizontal(7))
        .fold(FoldInstruction.FoldVertical(5))
      assert(fold.numberOfDots)(equalTo(16))
    }
  )
