package io.github.rpiotrow.advent2021.day14

import zio.ZIO
import zio.stream.ZStream

opaque type InsertionRule = ((Char, Char), Char)
object InsertionRule:
  def apply(rule: ((Char, Char), Char)): InsertionRule = rule
  extension (rule: InsertionRule)
    def c1: Char = rule._1._1
    def c2: Char = rule._1._2
    def insert: Char = rule._2

opaque type InsertionRules = Map[(Char, Char), Char]

object InsertionRules:
  def apply(list: List[InsertionRule]): InsertionRules = list.toMap

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
    def getRule(key: (Char, Char)): Option[InsertionRule] =
      rules.get(key).map(insert => InsertionRule(key -> insert))

    def getValue(key: List[Char]): Option[Char] = key match
      case c1 :: c2 :: Nil => rules.get((c1, c2))
      case _               => None

    def successors(rule: InsertionRule): List[InsertionRule] =
      val ((c1, c2), insert) = rule
      List(getRule((c1, insert)), getRule((insert, c2))).collect { case Some(e) => e }
