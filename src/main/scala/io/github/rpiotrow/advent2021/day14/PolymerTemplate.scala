package io.github.rpiotrow.advent2021.day14

opaque type PolymerTemplate = String

object PolymerTemplate:
  def apply(string: String): PolymerTemplate = string

  extension (template: PolymerTemplate)
    def stats: List[(Char, Long)] =
      template.toList.groupMapReduce(identity)(_ => 1L)(_ + _).toList.sortBy(_._2)
    def process(rules: InsertionRules, steps: Int): PolymerTemplate =
      1.to(steps).foldLeft(template) { case (t, step) => t.process(rules) }
    def charList: List[Char] = template.toList
    def firstChar: Char = template.head
    private def process(rules: InsertionRules): PolymerTemplate =
      template.toList
        .sliding(2)
        .map { window =>
          rules.getValue(window) match
            case Some(e) => List(window.head, e).mkString
            case None    => window.head.toString
        }
        .mkString + template.last
