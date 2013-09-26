package com.sciolizer.aima.agent

// First created by Joshua Ball on 9/25/13 at 9:39 PM
trait DiscreteAgent[Percept,Action] {
  def act(percept: Percept): Action
}
