package com.sciolizer.aima.agent

// First created by Joshua Ball on 9/25/13 at 9:36 PM
class DiscreteAgentSimulator[State,+Percept,-Action](
    initialState: State, 
    environment: SimulatedEnvironment[State,Percept,Action],
    agent: DiscreteAgent[Percept,Action],
    performance: PerformanceEvaluator[State,Action],
    lifetime: Long) {
  
  var state: State = initialState
  var steps: Long = 0
  var score: Long = 0

  def step() {
    val percept: Percept = environment.perceive(state)
    val action: Action = agent.act(percept)
    state = environment.apply(action, state)
    score += performance.evaluate(state, action)
    steps += 1
  }

  def run() {
    while (steps < lifetime) {
      step()
    }
  }
}
