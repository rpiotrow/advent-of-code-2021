package io.github.rpiotrow.advent2021.day16

import io.github.rpiotrow.advent2021.day16.Operator.GreaterThan
import scodec.bits.*
import zio.ZIO
import zio.stream.ZStream

sealed trait Packet(val version: Byte):
  def versionNumbers: List[Byte]
  def evaluate: Long

case class LiteralValue(override val version: Byte, value: Long) extends Packet(version):
  override def versionNumbers: List[Byte] = List(version)
  override def evaluate: Long = value

enum Operator(override val version: Byte) extends Packet(version):

  override def evaluate: Long = this match {
    case Sum(_, args) => args.map(_.evaluate).sum
    case Product(_, args) => args.map(_.evaluate).product
    case Minimum(_, args) => args.map(_.evaluate).min
    case Maximum(_, args) => args.map(_.evaluate).max
    case GreaterThan(_, first, second) => if first.evaluate > second.evaluate then 1L else 0L
    case LessThan(_, first, second) => if first.evaluate < second.evaluate then 1L else 0L
    case EqualTo(_, first, second) => if first.evaluate == second.evaluate then 1L else 0L
  }

  override def versionNumbers: List[Byte] = version :: (this match {
    case Sum(_, args) => args.flatMap(_.versionNumbers)
    case Product(_, args) => args.flatMap(_.versionNumbers)
    case Minimum(_, args) => args.flatMap(_.versionNumbers)
    case Maximum(_, args) => args.flatMap(_.versionNumbers)
    case GreaterThan(_, first, second) => first.versionNumbers ++ second.versionNumbers
    case LessThan(_, first, second) => first.versionNumbers ++ second.versionNumbers
    case EqualTo(_, first, second) => first.versionNumbers ++ second.versionNumbers
  })

  case Sum(override val version: Byte, args: List[Packet]) extends Operator(version)
  case Product(override val version: Byte, args: List[Packet]) extends Operator(version)
  case Minimum(override val version: Byte, args: List[Packet]) extends Operator(version)
  case Maximum(override val version: Byte, args: List[Packet]) extends Operator(version)
  case GreaterThan(override val version: Byte, first: Packet, second: Packet) extends Operator(version)
  case LessThan(override val version: Byte, first: Packet, second: Packet) extends Operator(version)
  case EqualTo(override val version: Byte, first: Packet, second: Packet) extends Operator(version)

object Operator:
  def from(version: Byte, typeId: Byte, subPackets: List[Packet]): ZIO[Any, String, Operator] =
    typeId match
      case 0 => ZIO.succeed(Sum(version, subPackets))
      case 1 => ZIO.succeed(Product(version, subPackets))
      case 2 => ZIO.succeed(Minimum(version, subPackets))
      case 3 => ZIO.succeed(Maximum(version, subPackets))
      case 5 => subPackets match {
        case second :: first :: nil => ZIO.succeed(GreaterThan(version, first, second))
        case _ => ZIO.fail("There should be only two sub-packets for GreaterThan")
      }
      case 6 => subPackets match {
        case second :: first :: nil => ZIO.succeed(LessThan(version, first, second))
        case _ => ZIO.fail("There should be only two sub-packets for LessThan")
      }
      case 7 => subPackets match {
        case second :: first :: nil => ZIO.succeed(EqualTo(version, first, second))
        case _ => ZIO.fail("There should be only two sub-packets for EqualTo")
      }
      case _ => ZIO.fail("Ups! That's not an operator packet!")

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
      operator <- Operator.from(version, typeId, subPackets)
    yield (operator, subPacketsLessBits)

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
