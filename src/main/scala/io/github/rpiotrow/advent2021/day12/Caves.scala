package io.github.rpiotrow.advent2021.day12

import zio.ZIO
import zio.stream.ZStream

import scala.collection.immutable.Queue

opaque type Cave = String
object Cave:
  def apply(name: String): Cave = name
  extension (cave: Cave) def isBig: Boolean = cave.forall(_.isUpper)

opaque type Caves = Map[Cave, Set[Cave]]
object Caves:
  val startCave: Cave = Cave("start")
  val endCave: Cave = Cave("end")

  def apply(map: Map[Cave, Set[Cave]]): Caves = map

  def parse(input: List[String]): ZIO[Any, String, Caves] =
    for
      pairs <- ZIO.foreach(input) { line =>
        line.split("-") match
          case Array(c1, c2) => ZIO.succeed(c1 -> c2)
          case _             => ZIO.fail("invalid input")
      }
      caveMap = (pairs ++ pairs.map(_.swap))
        .groupMap(_._1)(_._2)
        .map { case (key, list) => (key, list.toSet) }
    yield Caves(caveMap)

  export CountPaths.{countPathsWithSmallCavesOnlyOnce, countPathsWithOneSmallCaveTwice}

  object CountPaths:
    private case class QueueEntry(cave: Cave, visited: Set[Cave], visitedTwice: Option[Cave] = None)

    extension (caves: Caves)
      def countPathsWithSmallCavesOnlyOnce: ZIO[Any, String, Long] =
        countPaths(nextToVisitWithSmallCaveAlwaysOnce)

      def countPathsWithOneSmallCaveTwice: ZIO[Any, String, Long] =
        countPaths(nextToVisitWithOneSmallCaveTwice)

      private def countPaths(nextToVisit: QueueEntry => Set[QueueEntry]): ZIO[Any, String, Long] =
        val initial = (Queue(QueueEntry(startCave, Set(startCave))), 0L)
        ZStream
          .iterate(initial) { case (queue, count) =>
            queue.dequeueOption match
              case Some(queueEntry, dequeued) =>
                (
                  dequeued.appendedAll(nextToVisit(queueEntry)),
                  if queueEntry.cave == endCave then count + 1
                  else count
                )
              case None => (queue, count)
          }
          .dropWhile { case (queue, _) => queue.nonEmpty }
          .runHead
          .flatMap(ZIO.getOrFailWith("solution not found (impossible!)"))
          .map(_._2)

      private def connections(cave: Cave): Set[Cave] =
        if cave == endCave then Set.empty
        else
          caves
            .getOrElse(cave, Set.empty)
            .filterNot(_ == startCave)

      private def nextToVisitWithSmallCaveAlwaysOnce(queueEntry: QueueEntry): Set[QueueEntry] =
        import Cave.isBig
        connections(queueEntry.cave)
          .filter(c => c.isBig || !queueEntry.visited.contains(c))
          .map {
            case cave if cave.isBig =>
              QueueEntry(cave, queueEntry.visited)
            case cave =>
              QueueEntry(cave, queueEntry.visited + cave)
          }

      private def nextToVisitWithOneSmallCaveTwice(queueEntry: QueueEntry): Set[QueueEntry] =
        import Cave.isBig
        connections(queueEntry.cave)
          .filter(c => c.isBig || !queueEntry.visited.contains(c) || queueEntry.visitedTwice.isEmpty)
          .map {
            case cave if cave.isBig =>
              QueueEntry(cave, queueEntry.visited, queueEntry.visitedTwice)
            case cave =>
              QueueEntry(
                cave,
                queueEntry.visited + cave,
                queueEntry.visitedTwice.orElse(
                  Option.when(queueEntry.visited.contains(cave))(cave)
                )
              )
          }
