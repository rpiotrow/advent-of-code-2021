package io.github.rpiotrow.advent2021.day14

import zio.ZIO
import zio.stream.ZStream

opaque type InsertionRules = Map[(Char, Char), Char]

object InsertionRules:
  def apply(map: Map[(Char, Char), Char]): InsertionRules = map

  def parse(stream: ZStream[Any, String, String]): ZIO[Any, String, InsertionRules] =
    stream
      .mapZIO {
        case s"$key -> $insert" if key.length == 2 && insert.length == 1 =>
          ZIO.succeed((key(0), key(1)) -> insert(0))
        case _ =>
          ZIO.fail("invalid insertion rule")
      }
      .runCollect
      .map(_.toList.toMap)

  extension (rules: InsertionRules)
    def getValue(key: List[Char]): Option[Char] = key match
      case c1 :: c2 :: Nil => rules.get((c1, c2))
      case _               => None
