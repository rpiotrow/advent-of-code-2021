package io.github.rpiotrow.advent2021.day09

import zio.ZIO

case class Zipper[A](left: LazyList[A], focus: A, right: LazyList[A]):

  def toList: List[A] = left.toList.reverse ++ (focus :: right.toList)

  def moveLeft: Option[Zipper[A]] =
    left match
      case head #:: tail => Some(Zipper(tail, head, focus #:: right))
      case _             => None
  def moveRight: Option[Zipper[A]] =
    right match
      case head #:: tail => Some(Zipper(focus #:: left, head, tail))
      case _             => None

  def unsafeMoveLeft: Zipper[A] =
    Zipper(left.tail, left.head, focus #:: right)
  def unsafeMoveRight: Zipper[A] =
    Zipper(focus #:: left, right.head, right.tail)

object Zipper:
  def fromList[A](list: List[A]): ZIO[Any, String, Zipper[A]] =
    if list.isEmpty then ZIO.fail("list is empty")
    else
      ZIO.succeed {
        val rev = list.reverse
        Zipper(LazyList.from(rev.tail), rev.head, LazyList.empty)
      }

  given Comonad[Zipper] with
    extension [A](zipper: Zipper[A])
      override def extract: A = zipper.focus

      override def coflatMap[B](f: Zipper[A] => B): Zipper[B] =
        duplicate.map(f)

      override def map[B](f: A => B): Zipper[B] =
        Zipper(zipper.left.map(f), f(zipper.focus), zipper.right.map(f))

      private def duplicate: Zipper[Zipper[A]] =
        val duplicateLefts: LazyList[Zipper[A]] =
          LazyList.unfold(zipper)(z => z.moveLeft.map(x => (x, x)))
        val duplicateRights: LazyList[Zipper[A]] =
          LazyList.unfold(zipper)(z => z.moveRight.map(x => (x, x)))
        Zipper(duplicateLefts, zipper, duplicateRights)
