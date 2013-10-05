package com.sciolizer.aima.search

sealed trait Node[+State, +Action] {
  val state: State
  val pathCost: Double
  lazy val actions: List[Action] = reversedActions.reverse
  val reversedActions: List[Action]

  //  def compare(that: Node[State, Action]): Int = pathCost.compareTo(that.pathCost)
}

case class ParentNode[+State, +Action](state: State) extends Node[State, Action] {
  val pathCost: Double = 0.0
  lazy val reversedActions = List()
}

case class ChildNode[+State, +Action](state: State, parent: Node[State, Action], action: Action, pathCost: Double)
  extends Node[State, Action] {
  lazy val reversedActions = action +: parent.reversedActions

  //  def getPathBack: List[Action] = {
  //    step match {
  //      case None => List()
  //      case Some(Step(parent, action, _)) => action +: parent.getPathBack
  //    }
  //  }
  //  def cost: Double = {
  //    step match {
  //      case None => 0.0
  //      case Some(Step(parent, _, c)) => c + parent.cost
  //    }
  //  }
}

object ChildNode {
  def viaProblem[State, Action](
                                 problem: Problem[State, Action],
                                 parent: Node[State, Action],
                                 action: Action):
  ChildNode[State, Action] = {

    ChildNode(
      problem.result(parent.state, action),
      parent,
      action,
      parent.pathCost + problem.stepCost(parent.state, action))
  }
}