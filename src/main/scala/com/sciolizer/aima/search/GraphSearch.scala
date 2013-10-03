package com.sciolizer.aima.search


// First created by Joshua Ball on 9/30/13 at 9:24 PM
class GraphSearch {}

//class GraphSearch extends Search {
//
//  def search[State, Action](problem: Problem[State, Action]): Option[List[Action]] = {
//    var frontier: Map[State,Node[State,Action]] = Map((problem.initialState,Node(problem.initialState, None, 0.0)))
//    var expanded: Map[State,Node[State,Action]] = Map()
//    while (!frontier.isEmpty) {
//      val entry: (State, Node[State, Action]) = frontier.head
//      val node = entry._2
//      frontier = frontier - node.state
//      if (problem.isGoal(node.state)) {
//        return Some(node.getPathBack.reverse)
//      }
//      if (!expanded.contains(node.state) || expanded(node.state).cost < node.cost) {
//        expanded = expanded + entry
//        val actions: Set[Action] = problem.actions(node.state)
//        for (action <- actions) {
//          val newState: State = problem.transition(node.state, action)
//          frontier = frontier +
//        }
//      }
//      problem.
//    }
//    None
//  }
//}


case class Step[State, Action](parent: ChildNode[State, Action], action: Action, cost: Double)
