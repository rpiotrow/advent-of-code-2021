package io.github.rpiotrow.advent2021

import io.github.rpiotrow.advent2021.day16.PacketDecoder
import zio.test.*
import zio.test.Assertion.*

object Day16PacketDecoderSolutionSpec extends DefaultRunnableSpec:
  def spec = suite("PacketDecoderSpec")(
    test("PacketDecoder solution") {
      for solution <- PacketDecoder.solution
      yield assert(solution)(equalTo((843L, 5390807940351L)))
    }
  )
