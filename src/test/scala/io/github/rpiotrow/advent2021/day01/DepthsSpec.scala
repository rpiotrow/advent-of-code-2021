package io.github.rpiotrow.advent2021.day01

import zio.stream.ZStream
import zio.test.Assertion.*
import zio.test.*

object DepthsSpec extends DefaultRunnableSpec:
  def spec = suite("day01: DepthsSpec")(
    test("sliding window") {
      for
        lists <- Depths
          .slidingWindow(
            ZStream.fromIterator(Seq(199L, 200L, 208L, 210L, 200L, 207L, 240L, 269L, 260L, 263L).iterator).mapError(_.getMessage),
            3
          )
          .runCollect
          .map(_.toList)
      yield assert(lists)(
        equalTo(
          List(
            List(199L, 200L, 208L),
            List(200L, 208L, 210L),
            List(208L, 210L, 200L),
            List(210L, 200L, 207L),
            List(200L, 207L, 240L),
            List(207L, 240L, 269L),
            List(240L, 269L, 260L),
            List(269L, 260L, 263L)
          )
        )
      )
    }
  )
