package io.github.rpiotrow.advent2021.day20

import zio.ZIO

opaque type Grid[A] = Vector[Vector[A]]

object Grid:
  def apply[A](vectors: Vector[Vector[A]]): Grid[A] = vectors

  extension [A] (grid: Grid[A])
    def get(x: Int, y: Int): Option[A] =
      if grid.isDefinedAt(x) then
        val row = grid(x)
        if row.isDefinedAt(y) then Some(row(y)) else None
      else None

    def unsafeGet(x: Int, y: Int): A = grid(x)(y)

    def count(f: A => Boolean): Long = grid.map(_.count(f)).sum

    def coflatMapWithIndex[B](f: (Int, Int, Grid[A]) => B): Grid[B] =
      grid.zipWithIndex.map { case (row, index) => (row.zipWithIndex, index) }.map { case (row, x) =>
        row.map { case (_, y) =>
          f(x, y, grid)
        }
      }

    def expand(a: A, amount: Int): Grid[A] =
      val emptyRows = List.fill(amount)(Vector.fill(grid.head.size + amount * 2)(a))
      val margin = List.fill(amount)(a)
      grid
        .map(_.prependedAll(margin).appendedAll(margin))
        .prependedAll(emptyRows)
        .appendedAll(emptyRows)

    def shrink(amount: Int): ZIO[Any, String, Grid[A]] =
      if grid.length < amount || grid.head.length < amount then ZIO.fail("not enough elements")
      else
        ZIO.succeed(
          grid
            .slice(amount, grid.length - amount)
            .map(row => row.slice(amount, row.length - amount))
        )
