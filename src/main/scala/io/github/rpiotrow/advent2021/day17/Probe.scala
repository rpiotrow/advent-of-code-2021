package io.github.rpiotrow.advent2021.day17

import scala.annotation.tailrec

case class Point(x: Int, y: Int)

case class Velocity(dx: Int, dy: Int)

case class Probe(position: Point, velocity: Velocity):
  def step: Probe =
    Probe(
      position = Point(x = position.x + velocity.dx, y = position.y + velocity.dy),
      velocity = Velocity(
        dx = if velocity.dx > 0 then velocity.dx - 1 else if velocity.dx < 0 then velocity.dx + 1 else 0,
        dy = velocity.dy - 1
      )
    )
  def in(targetArea: TargetArea): Boolean =
    targetArea.leftTop.x <= position.x && position.x <= targetArea.rightBottom.x &&
      targetArea.leftTop.y >= position.y && position.y >= targetArea.rightBottom.y
  def after(targetArea: TargetArea): Boolean =
    position.x > targetArea.rightBottom.x || position.y < targetArea.rightBottom.y

object Probe:
  def highestYReachingTarget(startVelocity: Velocity, targetArea: TargetArea): Option[Int] =
    @tailrec
    def compute(probe: Probe, highestY: Int): Option[Int] =
      if probe.in(targetArea) then Some(highestY)
      else if probe.after(targetArea) then None
      else compute(probe.step, Math.max(highestY, probe.position.y))
    compute(Probe(Point(0, 0), startVelocity), 0)
