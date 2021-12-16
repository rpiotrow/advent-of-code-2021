package io.github.rpiotrow.advent2021.day14

case class PolymerElementsCounter(
    firstElement: Char,
    insertionRules: InsertionRules,
    appliedRules: Map[InsertionRule, Long] = Map.empty
):
  def stats: List[(Char, Long)] =
    val list = (firstElement, 1L) :: appliedRules.toList.flatMap { case (rule, count) =>
      List((rule.c2, count), (rule.insert, count))
    }
    list.groupMapReduce(_._1)(_._2)(_ + _).toList.sortBy(_._2)
  def step: PolymerElementsCounter =
    val newRules = appliedRules.toList
      .flatMap { case (rule, count) => insertionRules.successors(rule).map(r => (r, count)) }
      .groupMapReduce(_._1)(_._2)(_ + _)
    copy(appliedRules = newRules)
  def steps(n: Int): PolymerElementsCounter =
    1.to(n).foldLeft(this) { case (counter, _) => counter.step }

object PolymerElementsCounter:
  def from(template: PolymerTemplate, insertionRules: InsertionRules) =
    val firstElement = template.firstChar
    val appliedRules = template.charList
      .sliding(2)
      .map {
        case c1 :: c2 :: Nil => insertionRules.getRule((c1, c2))
        case _               => throw new RuntimeException("impossible")
      }
      .toList
      .collect { case Some(e) => e -> 1L }
      .toMap
    PolymerElementsCounter(firstElement, insertionRules, appliedRules)
