package io.github.rpiotrow.advent2021.day09

trait Comonad[F[_]]:
  extension [A] (x: F[A])
    def extract: A
    def coflatMap[B](f: F[A] => B): F[B]
    def map[B](f: A => B): F[B]
