package com.sciolizer.aima.search

// First created by Joshua Ball on 10/5/13 at 12:37 PM
class IterativeDeepeningSearch extends Search {
  def search[State, Action](problem: Problem[State, Action]): SearchResult[State, Action] = {
    for (depth <- 0 until Integer.MAX_VALUE) {
      val result = IterativeDeepeningSearch.depthLimitedSearch(problem, depth)
      result.searchResult match {
        case Some(x) => return x
        case None =>
      }
    }
    Failure() // unreachable, probably
  }
}

object IterativeDeepeningSearch {
  def depthLimitedSearch[State, Action](problem: Problem[State, Action], limit: Int): ExtendedSearchResult[State, Action] = {
    recursiveDls(ParentNode(problem.initialState), problem, limit)
  }

  def recursiveDls[State, Action](
                                   node: Node[State, Action],
                                   problem: Problem[State, Action],
                                   limit: Int): ExtendedSearchResult[State, Action] = {
    if (problem.goalTest(node.state)) return ExtendedSolution(node)
    if (limit <= 0) return Cutoff()
    var cutoffOccurred = false
    for (action <- problem.actions(node.state)) {
      val child = ChildNode.viaProblem(problem, node, action)
      val result = recursiveDls(child, problem, limit - 1)
      result match {
        case Cutoff() => cutoffOccurred = true
        case ExtendedSolution(n) => return ExtendedSolution(n)
        case _ =>
      }
    }
    if (cutoffOccurred) Cutoff() else ExtendedFailure()
  }
}

sealed trait ExtendedSearchResult[+State, +Action] {
  def searchResult: Option[SearchResult[State, Action]]
}

case class ExtendedSolution[+State, +Action](node: Node[State, Action]) extends ExtendedSearchResult[State, Action] {
  def searchResult: Option[SearchResult[State, Action]] = Some(Solution(node))
}

case class ExtendedFailure[+State, +Action]() extends ExtendedSearchResult[State, Action] {
  def searchResult: Option[SearchResult[State, Action]] = Some(Failure())
}

case class Cutoff[+State, +Action]() extends ExtendedSearchResult[State, Action] {
  def searchResult: Option[SearchResult[State, Action]] = None
}
