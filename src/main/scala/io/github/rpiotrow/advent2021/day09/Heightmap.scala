package io.github.rpiotrow.advent2021.day09

import zio.ZIO
import zio.stream.ZStream

case class Position(x: Int, y: Int, height: Int)

opaque type Heightmap = GridZipper[Position]

object Heightmap:

  def apply(grid: GridZipper[Position]): Heightmap = grid

  extension (stream: ZStream[Any, String, String])
    def toHeightmap: ZIO[Any, String, Heightmap] =
      stream
        .zipWithIndex
        .map { case (line, x) =>
          line.toList.zipWithIndex.map { case (char, y) =>
            val height = char.toInt - '0'.toInt
            Position(x.toInt, y, height)
          }
        }
        .runCollect
        .map(_.toList)
        .flatMap(GridZipper.fromList)

  extension (heightmap: Heightmap)
    def lowPoints: List[Position] =
      heightmap
        .coflatMap { grid =>
          val position = grid.extract
          if grid.neighbors.forall(_.height > position.height) then Some(position) else None
        }
        .toList
        .collect { case Some(position) => position }
