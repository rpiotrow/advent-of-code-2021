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

  export CountPaths.countPathsWithSmallCavesOnlyOnce

  object CountPaths:
    private case class QueueEntry(cave: Cave, visited: Set[Cave])

    extension (caves: Caves)
      def countPathsWithSmallCavesOnlyOnce: ZIO[Any, String, Long] =
        val initial = (Queue(QueueEntry(startCave, Set(startCave))), 0L)
        ZStream
          .iterate(initial) { case (queue, count) =>
            queue.dequeueOption match
              case Some(queueEntry, dequeued) =>
                (
                  dequeued.appendedAll(nextToVisitWithSmallCaveAlwaysOnce(queueEntry)),
                  if queueEntry.cave == endCave then count + 1 else count
                )
              case None => (queue, count)
          }
          .dropWhile { case (queue, _) => queue.nonEmpty }
          .runHead
          .flatMap(ZIO.getOrFailWith("solution not found (impossible!)"))
          .map(_._2)

      private def connections(cave: Cave): Set[Cave] = caves.getOrElse(cave, Set.empty)

      private def nextToVisitWithSmallCaveAlwaysOnce(queueEntry: QueueEntry): Set[QueueEntry] =
        import Cave.isBig
        connections(queueEntry.cave).filter(c => c.isBig || !queueEntry.visited.contains(c)).map { cave =>
          QueueEntry(cave, if !cave.isBig then queueEntry.visited + cave else queueEntry.visited)
        }
