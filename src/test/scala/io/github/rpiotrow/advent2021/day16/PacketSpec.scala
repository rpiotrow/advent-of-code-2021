package io.github.rpiotrow.advent2021.day16

import scodec.bits.*
import zio.test.*
import zio.test.Assertion.*

object PacketSpec extends DefaultRunnableSpec:

  def spec = suite("day14: Packet")(
    test("parse literal value") {
      for packet <- Packet.parse(hex"D2FE28".toBitVector)
      yield assert(packet.version)(equalTo(6)) &&
        assert(packet)(isSubtype[LiteralValue](Assertion.anything)) &&
        assert(packet.asInstanceOf[LiteralValue].value)(equalTo(2021L))
    },
    test("parse operator value") {
      for packet <- Packet.parse(hex"38006F45291200".toBitVector)
      yield assert(packet.version)(equalTo(1)) &&
        assert(packet)(isSubtype[Operator.LessThan](Assertion.anything)) // LessThen(literal 10, literal 20)
    },
    test("parse operator value") {
      for packet <- Packet.parse(hex"EE00D40C823060".toBitVector)
      yield assert(packet.version)(equalTo(7)) &&
        assert(packet)(isSubtype[Operator.Maximum](Assertion.anything)) &&
        assert(packet.asInstanceOf[Operator.Maximum].args.size)(equalTo(3)) // List(literal 1, literal 2. literal 3)
    },
    test("evaluate sum") {
      for packet <- Packet.parse(hex"C200B40A82".toBitVector)
      yield assert(packet.evaluate)(equalTo(3))
    },
    test("evaluate product") {
      for packet <- Packet.parse(hex"04005AC33890".toBitVector)
        yield assert(packet.evaluate)(equalTo(54))
    },
    test("evaluate minimum") {
      for packet <- Packet.parse(hex"880086C3E88112".toBitVector)
        yield assert(packet.evaluate)(equalTo(7))
    },
    test("evaluate maximum") {
      for packet <- Packet.parse(hex"CE00C43D881120".toBitVector)
        yield assert(packet.evaluate)(equalTo(9))
    },
    test("evaluate less than") {
      for packet <- Packet.parse(hex"D8005AC2A8F0".toBitVector)
        yield assert(packet.evaluate)(equalTo(1))
    },
    test("evaluate greater than") {
      for packet <- Packet.parse(hex"F600BC2D8F".toBitVector)
        yield assert(packet.evaluate)(equalTo(0))
    },
    test("evaluate equal to") {
      for packet <- Packet.parse(hex"9C005AC2F8F0".toBitVector)
        yield assert(packet.evaluate)(equalTo(0))
    },
    test("evaluate equal to more complex") {
      for packet <- Packet.parse(hex"9C0141080250320F1802104A08".toBitVector)
        yield assert(packet.evaluate)(equalTo(1))
    }
  )
