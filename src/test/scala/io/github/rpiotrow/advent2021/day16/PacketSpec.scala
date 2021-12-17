package io.github.rpiotrow.advent2021.day16

import scodec.bits.*
import zio.test.*
import zio.test.Assertion.*

object PacketSpec extends DefaultRunnableSpec:

  def spec = suite("day14: Packet")(
    test("parse literal value") {
      for packet <- Packet.parse(hex"D2FE28".toBitVector)
      yield assert(packet.version)(equalTo(6)) &&
        assert(packet.typeId)(equalTo(4)) &&
        assert(packet)(isSubtype[Packet.LiteralValue](Assertion.anything)) &&
        assert(packet.asInstanceOf[Packet.LiteralValue].value)(equalTo(2021L))
    },
    test("parse operator value") {
      for packet <- Packet.parse(hex"38006F45291200".toBitVector)
      yield assert(packet.version)(equalTo(1)) &&
        assert(packet.typeId)(equalTo(6)) &&
        assert(packet)(isSubtype[Packet.Operator](Assertion.anything)) &&
        assert(packet.asInstanceOf[Packet.Operator].subPackets.size)(equalTo(2)) // List(literal 10, literal 20)
    },
    test("parse operator value") {
      for packet <- Packet.parse(hex"EE00D40C823060".toBitVector)
      yield assert(packet.version)(equalTo(7)) &&
        assert(packet.typeId)(equalTo(3)) &&
        assert(packet)(isSubtype[Packet.Operator](Assertion.anything)) &&
        assert(packet.asInstanceOf[Packet.Operator].subPackets.size)(equalTo(3)) // List(literal 1, literal 2. literal 3)
    }
  )
