package com.sciolizer.aima.search

import scala.collection.mutable

class UniformCostSearch extends Search {
  def search[State, Action](problem: Problem[State, Action]): SearchResult[State, Action] = {
    var node: Node[State, Action] = ParentNode(problem.initialState)
    //    if (problem.goalTest(node.state)) return Solution(node)
    var frontier: mutable.PriorityQueue[Node[State, Action]] = mutable.PriorityQueue(node)(Ordering.by(_.pathCost))
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
  //  def main(args: Array[String]) {
  //    val queue: mutable.PriorityQueue[ChildNode[String, String]] = mutable.PriorityQueue[ChildNode[String,String]]()(Ordering.ordered[ChildNode[String,String]])
  //    queue += ChildNode(null, null, null, 1.0)
  //    queue += ChildNode(null, null, null, 2.0)
  //    val dequeue: ChildNode[Any, Any] = queue.dequeue()
  //    println(dequeue)
  //  }
}