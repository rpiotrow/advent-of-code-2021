package io.github.rpiotrow.advent2021.day21

import zio.stream.ZStream
import zio.test.*
import zio.test.Assertion.*

object DeterministicDiceSpec extends DefaultRunnableSpec:

  def spec = suite("day21: DeterministicDice")(
    test("stream") {
      for list <- DeterministicDice.stream.take(150).runCollect.map(_.toList)
      yield assert(list)(equalTo(1.to(100) ++ 1.to(50)))
    }
  )
