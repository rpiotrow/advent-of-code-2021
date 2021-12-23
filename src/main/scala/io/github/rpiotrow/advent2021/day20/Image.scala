package io.github.rpiotrow.advent2021.day20

import scodec.bits.BitVector
import zio.ZIO

opaque type Image = Grid[Boolean]

object Image:
  def parse(strings: Vector[String]): ZIO[Any, String, Image] =
    for
      lineLength <- ZIO.getOrFailWith("invalid image input")(strings.headOption.map(_.length))
      _ <- checkLength(lineLength, strings)
      _ <- checkChars(strings)
      bs = strings.map(_.toVector.map {
        case '.' => false
        case '#' => true
      })
    yield Grid(bs)

  private def checkLength(l: Int, strings: Seq[String]): ZIO[Any, String, Unit] =
    if strings.forall(_.length == l) then ZIO.unit
    else ZIO.fail("invalid image input!!!")

  private def checkChars(strings: Seq[String]): ZIO[Any, String, Unit] =
    if strings.forall(_.forall(c => c == '.' || c == '#')) then ZIO.unit
    else ZIO.fail("invalid image input!!!")

  import Grid.*
  extension (image: Image)
    def countLit: Long = image.count(identity)

    def neighbourhoodPixels(x: Int, y: Int): List[Boolean] =
      List(
        image.get(x - 1, y - 1),
        image.get(x - 1, y),
        image.get(x - 1, y + 1),
        image.get(x, y - 1),
        image.get(x, y),
        image.get(x, y + 1),
        image.get(x + 1, y - 1),
        image.get(x + 1, y),
        image.get(x + 1, y + 1)
      ).collect { case Some(a) => a }

    def enhance(alg: ImageEnhancementAlgorithm, infinityValue: Boolean): ZIO[Any, String, Image] =
      image
        .expand(infinityValue, 2)
        .coflatMapWithIndex { case (x, y, img) =>
          val bits = img.neighbourhoodPixels(x, y)
          if bits.length == 9 then alg.getPixel(BitVector.bits(bits).toInt(signed = false))
          else img.unsafeGet(x, y)
        }
        .shrink(1)
