package io.github.rpiotrow.advent2021.day07

import io.github.rpiotrow.advent2021.{Input, Solution}
import zio.{Console, ZIO}
import zio.stream.ZStream

object TheTreacheryOfWhales:

  val solution: Solution =
    for
      lineOption <- Input.readLines("day07.input").runHead
      line <- ZIO.getOrFailWith("no input")(lineOption)
      crabs <- Crabs.parseGroupAndOrder(line)
      from = crabs.head.position
      to = crabs.last.position
      solution <- ZStream
        .fromIterable(from.to(to))
        .map(p => CrabsPosition(p, crabs))
        .broadcast(2, 10)
        .use {
          case streamCopy1 :: streamCopy2 :: Nil =>
            for
              fiber1 <- findSolution(streamCopy1, CrabsPosition.linearFuelCost).fork
              fiber2 <- findSolution(streamCopy2, CrabsPosition.increasingFuelCost).fork
              linearFuelCost <- fiber1.join
              increasingFuelCost <- fiber2.join
              _ <- Console.printLine(s"Linear fuel cost for optimal crabs position is $linearFuelCost")
              _ <- Console.printLine(s"Increasing fuel cost for optimal crabs position is $increasingFuelCost")
            yield (linearFuelCost, increasingFuelCost)
          case _ => ZIO.dieMessage("impossible")
        }
    yield solution

  private def findSolution(
      stream: ZStream[Any, String | java.io.IOException, CrabsPosition],
      fuelFunction: CrabsPosition => Long
  ) =
    stream
      .map(fuelFunction)
      .runCollect
      .map(_.toList.min)
