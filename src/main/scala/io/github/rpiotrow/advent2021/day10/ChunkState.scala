package io.github.rpiotrow.advent2021.day10

import zio.ZIO
import zio.stream.ZStream

case class ChunkState(private val stack: List[Char], errorScore: Int):
  def completionScore: ZIO[Any, String, Long] =
    ZIO.foldLeft(stack)(0L) { case (score, char) =>
      char match
        case '(' => ZIO.succeed(5 * score + 1)
        case '[' => ZIO.succeed(5 * score + 2)
        case '{' => ZIO.succeed(5 * score + 3)
        case '<' => ZIO.succeed(5 * score + 4)
    }

object ChunkState:
  val empty: ChunkState = ChunkState(List.empty, 0)

  def fromString(string: String): ZIO[Any, String, ChunkState] =
    ZIO.foldLeft(string.toList)(ChunkState.empty) {
      case (ChunkState(stack, 0), char) =>
        char match
          case '<' => ZIO.succeed(ChunkState('<' :: stack, 0))
          case '{' => ZIO.succeed(ChunkState('{' :: stack, 0))
          case '[' => ZIO.succeed(ChunkState('[' :: stack, 0))
          case '(' => ZIO.succeed(ChunkState('(' :: stack, 0))
          case '>' =>
            ZIO.succeed(
              if stack.headOption.contains('<') then ChunkState(stack.tail, 0)
              else ChunkState(stack, 25137)
            )
          case '}' =>
            ZIO.succeed(
              if stack.headOption.contains('{') then ChunkState(stack.tail, 0)
              else ChunkState(stack, 1197)
            )
          case ']' =>
            ZIO.succeed(
              if stack.headOption.contains('[') then ChunkState(stack.tail, 0)
              else ChunkState(stack, 57)
            )
          case ')' =>
            ZIO.succeed(
              if stack.headOption.contains('(') then ChunkState(stack.tail, 0)
              else ChunkState(stack, 3)
            )
          case _ => ZIO.fail("invalid input")
      case (scored @ ChunkState(_, _), _) => ZIO.succeed(scored)
    }
