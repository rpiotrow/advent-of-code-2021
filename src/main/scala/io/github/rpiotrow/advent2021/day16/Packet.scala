package io.github.rpiotrow.advent2021.day16

import scodec.bits.*
import zio.ZIO
import zio.stream.ZStream

enum Packet(val version: Byte, val typeId: Byte):

  def versionNumbers: List[Byte] =
    this match
      case LiteralValue(version, _)         => List(version)
      case Operator(version, _, subPackets) => version :: subPackets.flatMap(_.versionNumbers)

  case LiteralValue(override val version: Byte, val value: Long) extends Packet(version, 4)
  case Operator(override val version: Byte, override val typeId: Byte, val subPackets: List[Packet])
      extends Packet(version, typeId)

object Packet:

  def parse(bits: BitVector): ZIO[Any, String, Packet] = parsePacket(bits).map(_._1)

  extension (bits: BitVector)
    private def toByteZIO: ZIO[Any, String, Byte] =
      ZIO.attempt(bits.toByte(signed = false)).mapError(ex => s"Cannot covert bits to byte ${ex.getMessage}")
    private def toLongZIO: ZIO[Any, String, Long] =
      ZIO.attempt(bits.toLong(signed = false)).mapError(ex => s"Cannot covert bits to long ${ex.getMessage}")
    private def splitAtZIO(n: Long): ZIO[Any, String, (BitVector, BitVector)] =
      ZIO.attempt(bits.splitAt(n)).mapError(ex => s"cannot split bits ${ex.getMessage}")

  private def parsePacket(bits: BitVector): ZIO[Any, String, (Packet, BitVector)] =
    for
      versionTuple <- bits.splitAtZIO(3)
      (versionBits, versionLessBits) = versionTuple
      version <- versionBits.toByteZIO
      typeIdTuple <- versionLessBits.splitAtZIO(3)
      (typeIdBits, typeIdLessBits) = typeIdTuple
      typeId <- typeIdBits.toByteZIO
      result <-
        if typeId == 4 then parseLiteral(version, typeIdLessBits)
        else parseOperator(version, typeId, typeIdLessBits)
    yield result

  private def parseLiteral(version: Byte, bits: BitVector): ZIO[Any, String, (Packet, BitVector)] =
    for
      tuple <- ZStream
        .unfoldZIO((1, List.empty[BitVector], bits)) { case (previousFlag, acc, bits0) =>
          if previousFlag == 0 then ZIO.none
          else
            for
              flagTuple <- bits0.splitAtZIO(1)
              (flagBits, flagLessBits) = flagTuple
              flag <- flagBits.toByteZIO
              valueBitsTuple <- flagLessBits.splitAtZIO(4)
              (valueBits, valueBitsLessBits) = valueBitsTuple
              valueBitsList = valueBits :: acc
            yield Some((valueBitsList, valueBitsLessBits), (flag, valueBitsList, valueBitsLessBits))
        }
        .runLast
        .flatMap(ZIO.getOrFailWith("cannot parse literal value"))
      (valueBitsList, valueBitsLessBits) = tuple
      valueBits = valueBitsList.reverse.reduce(_ ++ _)
      value <- valueBits.toLongZIO
    yield (LiteralValue(version, value), valueBitsLessBits)

  private def parseOperator(version: Byte, typeId: Byte, bits: BitVector): ZIO[Any, String, (Operator, BitVector)] =
    for
      lengthIdTuple <- bits.splitAtZIO(1)
      (lengthIdBits, lengthIdLessBits) = lengthIdTuple
      lengthId <- lengthIdBits.toByteZIO
      subPacketsTuple <-
        if lengthId == 1 then parsePackageListCount(lengthIdLessBits)
        else parsePackageListLength(lengthIdLessBits)
      (subPackets, subPacketsLessBits) = subPacketsTuple
    yield (Operator(version, typeId, subPackets), subPacketsLessBits)

  private def parsePackageListCount(bits: BitVector): ZIO[Any, String, (List[Packet], BitVector)] =
    for
      countTuple <- bits.splitAtZIO(11)
      (countBits, countLessBits) = countTuple
      count <- countBits.toLongZIO
      result <- ZIO.foldRight(1L.to(count))((List.empty[Packet], countLessBits)) { case (_, (list, bits)) =>
        for
          tuple <- parsePacket(bits)
          (packet, packetLessBits) = tuple
        yield (packet :: list, packetLessBits)
      }
    yield result

  private def parsePackageListLength(bits: BitVector): ZIO[Any, String, (List[Packet], BitVector)] =
    for
      lengthTuple <- bits.splitAtZIO(15)
      (lengthBits, lengthLessBits) = lengthTuple
      length <- lengthBits.toLongZIO
      subPacketsTuple <- lengthLessBits.splitAtZIO(length)
      (subPacketsBits, subPacketsLessBits) = subPacketsTuple
      packets <- ZStream
        .unfoldZIO((subPacketsBits, List.empty[Packet])) { case (bits, acc) =>
          if bits.isEmpty then ZIO.none
          else
            for
              tuple <- parsePacket(bits)
              (packet, packetLessBits) = tuple
              packets = packet :: acc
            yield Some(packets, (packetLessBits, packets))
        }
        .runLast
        .flatMap(ZIO.getOrFailWith("cannot parse package list with length"))
    yield (packets, subPacketsLessBits)
