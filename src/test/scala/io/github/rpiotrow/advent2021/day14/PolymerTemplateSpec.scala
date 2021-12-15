package io.github.rpiotrow.advent2021.day14

import zio.test.*
import zio.test.Assertion.*

object PolymerTemplateSpec extends DefaultRunnableSpec:

  private val template = PolymerTemplate("NNCB")
  private val insertionRules = InsertionRules(Map(
    ('C','H') -> 'B',
    ('H','H') -> 'N',
    ('C','B') -> 'H',
    ('N','H') -> 'C',
    ('H','B') -> 'C',
    ('H','C') -> 'B',
    ('H','N') -> 'C',
    ('N','N') -> 'C',
    ('B','H') -> 'H',
    ('N','C') -> 'B',
    ('N','B') -> 'B',
    ('B','N') -> 'B',
    ('B','B') -> 'N',
    ('B','C') -> 'B',
    ('C','C') -> 'N',
    ('C','N') -> 'C'
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
