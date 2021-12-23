package io.github.rpiotrow.advent2021.day20

import zio.test.*
import zio.test.Assertion.*

object ImageSpec extends DefaultRunnableSpec:

  def spec = suite("day20: Image")(
    test("parse") {
      for image <- Image.parse(Vector("#..#.", "#....", "##..#", "..#..", "..###"))
      yield assert(image.countLit)(equalTo(10))
    },
    test("enhance") {
      for
        image <- Image.parse(Vector("#..#.", "#....", "##..#", "..#..", "..###"))
        alg <- ImageEnhancementAlgorithm.parse("..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..###..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#..#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#......#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.....####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.......##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#")
        enhanced <- image.enhance(alg, false)
        enhandecTwice <- enhanced.enhance(alg, false)
      yield assert(enhandecTwice.countLit)(equalTo(35))
    }
  )
