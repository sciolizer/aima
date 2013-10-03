package com.sciolizer.aima.search

// First created by Joshua Ball on 9/30/13 at 9:09 PM
trait Problem[State, Action] {
  val initialState: State

  def actions(state: State): Set[Action]

  def result(state: State, action: Action): State

  def goalTest(state: State): Boolean

  def stepCost(state: State, action: Action): Double
}
