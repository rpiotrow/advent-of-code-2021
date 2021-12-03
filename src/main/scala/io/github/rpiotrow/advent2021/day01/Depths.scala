package io.github.rpiotrow.advent2021.day01

import io.github.rpiotrow.advent2021.Input
import zio.ZIO
import zio.stream.ZStream

object Depths:

  def read: ZStream[Any, String, Long] =
    Input
      .readLines("day01.input")
      .mapZIO(line => ZIO.attempt(line.toLong).orElseFail(s"invalid number: $line"))

  def countIncreases(stream: ZStream[Any, String, Long]): ZIO[Any, String, Long] =
    stream.zipWithPrevious
      .map {
        case (Some(previous), current) if previous < current => true
        case _                                               => false
      }
      .filter(identity)
      .runCount

  def countSlidingWindowIncreases(stream: ZStream[Any, String, Long], windowSize: Int): ZIO[Any, String, Long] =
    countIncreases(
      slidingWindow(stream, windowSize).map(_.sum)
    )

  private[day01] def slidingWindow(stream: ZStream[Any, String, Long], windowSize: Int): ZStream[Any, String, List[Long]] =
    stream
      .mapAccum(scala.collection.immutable.Queue.empty[Long]) { (queue, a) =>
        val enqueued = queue.enqueue(a)
        val newQueue = if enqueued.size > windowSize then enqueued.dequeue._2 else enqueued
        val list = if newQueue.size == windowSize then Some(newQueue.toList) else None
        (newQueue, list)
      }
      .collectSome
