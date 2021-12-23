package io.github.rpiotrow.advent2021.day20

import zio.ZIO

opaque type ImageEnhancementAlgorithm = Map[Int, Boolean]

object ImageEnhancementAlgorithm:
  def parse(string: String): ZIO[Any, String, ImageEnhancementAlgorithm] =
    if string.length != 512 then ZIO.fail("invalid input for Image Enhancement Algorithm!!!")
    else if string.exists(c => c != '.' && c != '#') then ZIO.fail("invalid input for Image Enhancement Algorithm!!!")
    else
      ZIO.succeed(string.toList.zipWithIndex.map {
        case ('.', index) => index -> false
        case ('#', index) => index -> true
      }.toMap)

  extension (alg: ImageEnhancementAlgorithm)
    def getPixel(index: Int): Boolean =
      if index < 0 || index >= 512 then throw new IllegalArgumentException("invalid image enhancement algorithm index")
      else alg(index)
