package com.sciolizer.aima.search

import scala.collection.mutable

// First created by Joshua Ball on 10/1/13 at 8:30 AM
class BreadthFirstSearch[State] extends Search[State] {
  def search[Action](problem: Problem[State, Action]): SearchResult[State, Action] = {
    var node: Node[State, Action] = ParentNode(problem.initialState)
    if (problem.goalTest(node.state)) return Solution(node)
    val frontier: mutable.Queue[Node[State, Action]] = mutable.Queue(node)
    var explored: Set[State] = Set()
    while (!frontier.isEmpty) {
      node = frontier.dequeue()
      explored = explored + node.state
      for (action <- problem.actions(node.state)) {
        val child = ChildNode.viaProblem(problem, node, action)
        if (!explored.contains(child.state) && !frontier.contains(child.state)) {
          if (problem.goalTest(child.state)) return Solution(child)
          frontier.enqueue(child)
        }
      }
    }
    Failure()
  }
}
