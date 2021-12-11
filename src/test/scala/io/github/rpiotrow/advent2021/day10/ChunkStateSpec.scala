package io.github.rpiotrow.advent2021.day10

import zio.test.Assertion.*
import zio.test.{DefaultRunnableSpec, *}

object ChunkStateSpec extends DefaultRunnableSpec:

  def spec = suite("day05: ChunkStateSpec")(
    test("errorScore {") {
      for chunkState <- ChunkState.fromString("{([(<{}[<>[]}>{[]{[(<()>")
      yield assert(chunkState.errorScore)(equalTo(1197))
    },
    test("errorScore (") {
      for chunkState <- ChunkState.fromString("[[<[([]))<([[{}[[()]]]")
      yield assert(chunkState.errorScore)(equalTo(3))
    },
    test("errorScore (") {
      for chunkState <- ChunkState.fromString("[{[{({}]{}}([{[{{{}}([]")
      yield assert(chunkState.errorScore)(equalTo(57))
    },
    test("errorScore <") {
      for chunkState <- ChunkState.fromString("<{([([[(<>()){}]>(<<{{")
      yield assert(chunkState.errorScore)(equalTo(25137))
    },
    test("completionScore 1") {
      for
        chunkState <- ChunkState.fromString("[({(<(())[]>[[{[]{<()<>>")
        completionScore <- chunkState.completionScore
      yield assert(completionScore)(equalTo(288957L))
    },
    test("completionScore 2") {
      for
        chunkState <- ChunkState.fromString("[(()[<>])]({[<{<<[]>>(")
        completionScore <- chunkState.completionScore
      yield assert(completionScore)(equalTo(5566L))
    },
    test("completionScore 3") {
      for
        chunkState <- ChunkState.fromString("(((({<>}<{<{<>}{[]{[]{}")
        completionScore <- chunkState.completionScore
      yield assert(completionScore)(equalTo(1480781L))
    },
    test("completionScore 4") {
      for
        chunkState <- ChunkState.fromString("{<[[]]>}<{[{[{[]{()[[[]")
        completionScore <- chunkState.completionScore
      yield assert(completionScore)(equalTo(995444L))
    },
    test("completionScore 5") {
      for
        chunkState <- ChunkState.fromString("<{([{{}}[<[[[<>{}]]]>[]]")
        completionScore <- chunkState.completionScore
      yield assert(completionScore)(equalTo(294L))
    }
  )
