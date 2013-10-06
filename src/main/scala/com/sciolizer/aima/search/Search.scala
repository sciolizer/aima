package com.sciolizer.aima.search

// First created by Joshua Ball on 9/30/13 at 9:13 PM
trait Search[State] {
  def search[Action](problem: Problem[State, Action]): SearchResult[State, Action]
}

sealed trait SearchResult[+State, +Action]

case class Solution[+State, +Action](node: Node[State, Action]) extends SearchResult[State, Action]

case class Failure[+State, +Action]() extends SearchResult[State, Action]

