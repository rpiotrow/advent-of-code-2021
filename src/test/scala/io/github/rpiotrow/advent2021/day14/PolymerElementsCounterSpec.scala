package io.github.rpiotrow.advent2021.day14

import zio.test.*
import zio.test.Assertion.*

object PolymerElementsCounterSpec extends DefaultRunnableSpec:

  private val insertionRules = InsertionRules(List(
    InsertionRule(('C','H') -> 'B'),
    InsertionRule(('H','H') -> 'N'),
    InsertionRule(('C','B') -> 'H'),
    InsertionRule(('N','H') -> 'C'),
    InsertionRule(('H','B') -> 'C'),
    InsertionRule(('H','C') -> 'B'),
    InsertionRule(('H','N') -> 'C'),
    InsertionRule(('N','N') -> 'C'),
    InsertionRule(('B','H') -> 'H'),
    InsertionRule(('N','C') -> 'B'),
    InsertionRule(('N','B') -> 'B'),
    InsertionRule(('B','N') -> 'B'),
    InsertionRule(('B','B') -> 'N'),
    InsertionRule(('B','C') -> 'B'),
    InsertionRule(('C','C') -> 'N'),
    InsertionRule(('C','N') -> 'C')
  ))
  private val counter = PolymerElementsCounter('N', insertionRules, Map(
    InsertionRule(('N','N') -> 'C') -> 1L,
    InsertionRule(('N','C') -> 'B') -> 1L,
    InsertionRule(('C','B') -> 'H') -> 1L
  ))

  def spec = suite("day14: PolymerTemplate")(
    test("stats") {
      val result = counter.steps(9).stats
      assert(result)(equalTo(List(
        'H' -> 161, 'C' -> 298, 'N' -> 865, 'B' -> 1749
      )))
    }
  )
