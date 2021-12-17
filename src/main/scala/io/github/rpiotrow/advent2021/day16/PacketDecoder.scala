package io.github.rpiotrow.advent2021.day16

import io.github.rpiotrow.advent2021.{Input, Solution}
import scodec.bits.BitVector
import zio.{Console, ZIO}

object PacketDecoder:

  val solution: Solution =
    for
      line <- Input.readLines("day16.input").runHead
      inputHex <- ZIO.getOrFailWith("no input")(line)
      bits <- ZIO.getOrFailWith("not parseable input")(BitVector.fromHex(inputHex))
      packet <- Packet.parse(bits)
      versionNumbersSum = packet.versionNumbers.map(_.toLong).sum
      _ <- Console.printLine(s"Sum of all version numbers is $versionNumbersSum")
    yield (versionNumbersSum, 0L)
