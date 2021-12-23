package io.github.rpiotrow.advent2021.day20

import io.github.rpiotrow.advent2021.{Input, Solution}
import zio.stream.{ZSink, ZStream}
import zio.{Console, ZEnv, ZIO}

object TrenchMap:

  val solution: Solution =
    for
      lines <- Input.readLines("day20.input").runCollect.map(_.toVector)
      algLine <- ZIO.getOrFailWith("no valid algorithm input!!!")(lines.headOption)
      alg <- ImageEnhancementAlgorithm.parse(algLine)
      imageLines <- ZIO.attempt(lines.slice(2, lines.length)).mapError(_ => "no valid image input!!!")
      image <- Image.parse(imageLines)
      solution <- ZStream
        .iterate(false)(!_)
        .mapAccumZIO(image) { case (image, infinityValue) =>
          for enhanced <- image.enhance(alg, infinityValue)
          yield (enhanced, enhanced)
        }
        .drop(1)
        .peel(ZSink.head)
        .use[ZEnv, String | java.io.IOException, (Long, Long)] { case (headOption2, restOfStream) =>
          for
            imageEnhancedTwice <- ZIO.getOrFailWith("no solution for part 1!!!")(headOption2)
            count1 = imageEnhancedTwice.countLit
            _ <- Console.printLine(s"There is $count1 pixels lit in resulting (enhanced twice) image")
            headOption50 <- restOfStream.drop(47).runHead
            imageEnhanced50Times <- ZIO.getOrFailWith("no solution for part 2!!!")(headOption50)
            count2 = imageEnhanced50Times.countLit
            _ <- Console.printLine(s"There is $count2 pixels lit in resulting (enhanced 50 times) image")
          yield (count1, count2)
        }
    yield solution
