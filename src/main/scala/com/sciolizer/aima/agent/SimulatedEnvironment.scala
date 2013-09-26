package com.sciolizer.aima.agent

// First created by Joshua Ball on 9/25/13 at 9:34 PM
trait SimulatedEnvironment[State,Percept,Action] {
  def apply(action: Action, state: State): State
  def perceive(state: State): Percept
}
