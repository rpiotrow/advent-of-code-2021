package io.github.rpiotrow.advent2021.day06

import io.github.rpiotrow.advent2021.Input
import io.github.rpiotrow.advent2021.Input.parseInt
import zio.ZIO

case class LanternfishCount(age: Int, count: Long)

case class LanternfishHerd(counts: List[LanternfishCount]):

  def size: Long = counts.map(_.count).sum

  def compact: LanternfishHerd =
    LanternfishHerd(LanternfishHerd.compact(counts))

  def nextGeneration: LanternfishHerd =
    LanternfishHerd {
      val newCounts = counts.flatMap {
        case LanternfishCount(0, count)   => List(LanternfishCount(8, count), LanternfishCount(6, count))
        case LanternfishCount(age, count) => List(LanternfishCount(age - 1, count))
      }
      if newCounts.size > 15 then LanternfishHerd.compact(newCounts) else newCounts
    }

object LanternfishHerd:

  def parse: ZIO[Any, String, LanternfishHerd] =
    for
      lineOption <- Input.readLines("day06.input").runHead
      line <- ZIO.getOrFailWith("no input")(lineOption)
      ages <- ZIO.foreach(line.split(',').toList)(parseInt)
    yield LanternfishHerd(ages.map(a => LanternfishCount(a, 1L))).compact

  private def compact(list: List[LanternfishCount]): List[LanternfishCount] =
    list
      .groupBy(_.age)
      .map { case (age, group) =>
        group.reduce((a, b) => LanternfishCount(age, a.count + b.count))
      }
      .toList
