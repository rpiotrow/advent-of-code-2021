package io.github.rpiotrow.advent2021.day02

case class SubmarinePosition(horizontalPosition: Int, depth: Int)
object SubmarinePosition:
  val startPosition: SubmarinePosition = SubmarinePosition(0, 0)

trait Submarine:
  def position: SubmarinePosition
  def forward(value: Int): Submarine
  def down(value: Int): Submarine
  def up(value: Int): Submarine

case class SimpleSubmarine(override val position: SubmarinePosition = SubmarinePosition.startPosition) extends Submarine:
  override def forward(value: Int): Submarine =
    SimpleSubmarine(position.copy(horizontalPosition = position.horizontalPosition + value))
  override def down(value: Int): Submarine =
    SimpleSubmarine(position.copy(depth = position.depth + value))
  override def up(value: Int): Submarine =
    SimpleSubmarine(position.copy(depth = position.depth - value))

case class AimSubmarine(
    override val position: SubmarinePosition = SubmarinePosition.startPosition,
    aim: Int = 0
) extends Submarine:
  override def forward(value: Int): Submarine =
    copy(position =
      SubmarinePosition(
        horizontalPosition = position.horizontalPosition + value,
        depth = position.depth + (aim * value)
      )
    )
  override def down(value: Int): Submarine =
    copy(aim = aim + value)
  override def up(value: Int): Submarine =
    copy(aim = aim - value)
