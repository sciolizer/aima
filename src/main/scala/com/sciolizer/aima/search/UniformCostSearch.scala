package com.sciolizer.aima.search

import scala.collection.mutable

class UniformCostSearch[State] extends Search[State] {
  def search[Action](problem: Problem[State, Action]): SearchResult[State, Action] = {
    var node: Node[State, Action] = ParentNode(problem.initialState)
    val ordering: Ordering[Node[State, Action]] = Ordering.by(_.pathCost)
    //    if (problem.goalTest(node.state)) return Solution(node)
    var frontier: mutable.PriorityQueue[Node[State, Action]] = mutable.PriorityQueue(node)(ordering.reverse)
    var explored: Set[State] = Set()
    while (!frontier.isEmpty) {
      node = frontier.dequeue()
      if (problem.goalTest(node.state)) return Solution(node)
      explored = explored + node.state
      for (action <- problem.actions(node.state)) {
        val child = ChildNode.viaProblem(problem, node, action)
        val x: List[Int] = List()
        x.contains()
        frontier.find(_.state == child.state) match {
          case None => if (!explored.contains(child.state)) frontier.enqueue(child)
          case Some(similar) =>
            if (similar.pathCost > child.pathCost) {
              // inefficient
              val adjusted: Set[Node[State, Action]] = frontier.toSet - similar + child
              frontier.clear()
              for (node <- adjusted) {
                frontier += node
              }
            }
        }
      }
    }
    Failure()
  }
}


object TestPriorityQueuePicksLowestPathCost {
  def main(args: Array[String]) {
    val ordering: Ordering[ChildNode[Any, String]] = Ordering.by(_.pathCost)
    val queue = mutable.PriorityQueue(
      ChildNode("Hello", ParentNode(), "Goodbye", 1.0),
      ChildNode("Hello", ParentNode(), "Goodbye", 2.0))(ordering.reverse)
    println(queue.dequeue())
  }
}