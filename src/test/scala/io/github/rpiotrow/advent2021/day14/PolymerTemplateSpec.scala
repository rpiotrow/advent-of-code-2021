package io.github.rpiotrow.advent2021.day14

import zio.test.*
import zio.test.Assertion.*

object PolymerTemplateSpec extends DefaultRunnableSpec:

  private val template = PolymerTemplate("NNCB")
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

  def spec = suite("day14: PolymerTemplate")(
    test("process 1") {
      val result = template.process(insertionRules, 1)
      assert(result)(equalTo(PolymerTemplate("NCNBCHB")))
    },
    test("process 2") {
      val result = template.process(insertionRules, 2)
      assert(result)(equalTo(PolymerTemplate("NBCCNBBBCBHCB")))
    },
    test("process 3") {
      val result = template.process(insertionRules, 3)
      assert(result)(equalTo(PolymerTemplate("NBBBCNCCNBBNBNBBCHBHHBCHB")))
    },
    test("process 4") {
      val result = template.process(insertionRules, 4)
      assert(result)(equalTo(PolymerTemplate("NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB")))
    },
    test("stats") {
      val result = template.process(insertionRules, 10).stats
      assert(result)(equalTo(List(
        'H' -> 161, 'C' -> 298, 'N' -> 865, 'B' -> 1749
      )))
    }
  )
