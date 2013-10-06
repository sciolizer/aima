package com.sciolizer.aima.search

// First created by Joshua Ball on 10/5/13 at 1:12 PM
// What would be a good visualization of this?
class RecursiveBestFirstSearch[State](heuristic: State => Double) extends Search[State] {


  def search[Action](problem: Problem[State, Action]): SearchResult[State, Action] = {
    val initialState: State = problem.initialState
    val h: Double = heuristic(initialState)
    rbfs(problem, Successor(ParentNode(initialState), h, h), Double.PositiveInfinity).searchResult
  }

  protected def rbfs[Action](
                              problem: Problem[State, Action],
                              successor: Successor[State, Action],
                              fLimit: Double): QuantifiedSearchResult[State, Action] = {
    show("rbfs(problem, " + successor + ", " + fLimit)
    if (problem.goalTest(successor.node.state)) return QuantifiedSolution(successor.node)
    val successors: Set[Successor[State, Action]] = problem.actions(successor.node.state) map {
      x =>
        val n: Node[State, Action] = ChildNode.viaProblem(problem, successor.node, x)
        val h: Double = heuristic(n.state)
        val f = n.pathCost + h
        Successor(n, f, h)
    }
    show("successors: " + successors)
    if (successors.isEmpty) return QuantifiedFailure(Double.PositiveInfinity)
    for (s <- successors) {
      // Update f with value from previous search, if any
      s.f = math.max(s.node.pathCost + s.h, s.f)
    }
    show("updated successors: " + successors)
    var result: QuantifiedSearchResult[State, Action] = QuantifiedFailure(0.0)
    while (result.isInstanceOf[QuantifiedFailure[State, Action]]) {
      val best: Successor[State, Action] = successors.minBy(_.f)
      if (best.f > fLimit) {
        successor.f = best.f
        return QuantifiedFailure(best.f)
      }
      val alternatives: Set[Successor[State, Action]] = successors - best
      val alternative: Double = if (alternatives.isEmpty) Double.PositiveInfinity else alternatives.minBy(_.f).f // inefficient after already making a pass on successors above
      result = rbfs(problem, best, math.min(fLimit, alternative))
    }
    result
  }

  protected def show(message: => String) {

  }
}

object RecursiveBestFirstSearch {
}

case class Successor[+State, +Action](node: Node[State, Action], var f: Double, h: Double)

sealed trait QuantifiedSearchResult[+State, +Action] {
  def searchResult: SearchResult[State, Action]
}

case class QuantifiedSolution[+State, +Action](node: Node[State, Action]) extends QuantifiedSearchResult[State, Action] {
  def searchResult = Solution(node)
}

case class QuantifiedFailure[+State, +Action](fLimit: Double) extends QuantifiedSearchResult[State, Action] {
  def searchResult = Failure()
}