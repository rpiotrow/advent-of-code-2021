package io.github.rpiotrow.advent2021.day09

import zio.ZIO

case class GridZipper[A](value: Zipper[Zipper[A]]):

  def toList: List[A] = value.map(_.toList).toList.flatten

  def neighbors: List[A] =
    List(north, east, south, west).collect { case Some(a) => a }.map(_.extract)

  private def north: Option[GridZipper[A]] =
    value.moveLeft.map(GridZipper(_))
  private def south: Option[GridZipper[A]] =
    value.moveRight.map(GridZipper(_))
  private def east: Option[GridZipper[A]] =
    if value.extract.right.isEmpty then None
    else Some(GridZipper(value.map(_.unsafeMoveRight)))
  private def west: Option[GridZipper[A]] =
    if value.extract.left.isEmpty then None
    else Some(GridZipper(value.map(_.unsafeMoveLeft)))

object GridZipper:
  def fromList[A](list: List[List[A]]): ZIO[Any, String, GridZipper[A]] =
    for
      listOfZippers <- ZIO.foreach(list)(Zipper.fromList)
      zipperOfZippers <- Zipper.fromList(listOfZippers)
    yield GridZipper(zipperOfZippers)

  given Comonad[GridZipper] with
    extension [A](w: GridZipper[A])
      override def extract: A = w.value.focus.focus

      override def map[B](f: A => B): GridZipper[B] =
        GridZipper(w.value.map(_.map(f)))

      override def coflatMap[B](f: GridZipper[A] => B): GridZipper[B] =
        duplicate.map(f)

      private def duplicate: GridZipper[GridZipper[A]] =
        GridZipper(nest(nest(w.value))).map(GridZipper(_))

      private def nest[B](s: Zipper[Zipper[B]]): Zipper[Zipper[Zipper[B]]] =
        val duplicateLefts: LazyList[Zipper[Zipper[B]]] =
          LazyList.unfold(s)(zipper =>
            if zipper.extract.left.isEmpty then None
            else
              val x = zipper.map(_.unsafeMoveLeft)
              Some(x, x)
          )
        val duplicateRights: LazyList[Zipper[Zipper[B]]] =
          LazyList.unfold(s)(zipper =>
            if zipper.extract.right.isEmpty then None
            else
              val x = zipper.map(_.unsafeMoveRight)
              Some(x, x)
          )
        Zipper(duplicateLefts, s, duplicateRights)
