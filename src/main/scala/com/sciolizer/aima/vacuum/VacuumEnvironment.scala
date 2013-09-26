package com.sciolizer.aima.vacuum

import com.sciolizer.aima.agent.{PerformanceEvaluator, DiscreteAgent, DiscreteAgentSimulator, SimulatedEnvironment}

// First created by Joshua Ball on 9/25/13 at 9:42 PM
class VacuumEnvironment extends SimulatedEnvironment[VacuumWorldState,VacuumPercept,VacuumAction] {
  def apply(action: VacuumAction, state: VacuumWorldState): VacuumWorldState = action match {
    case Right() =>
      state.agentPosition = math.min(state.dirt.size - 1, state.agentPosition + 1)
      state
    case Left() =>
      state.agentPosition = math.max(0, state.agentPosition - 1)
      state
    case Suck() =>
      state.dirt(state.agentPosition) = false
      state
  }

  def perceive(state: VacuumWorldState): VacuumPercept = VacuumPercept(state.dirt(state.agentPosition))
}

case class VacuumWorldState(var agentPosition: Int, dirt: collection.mutable.IndexedSeq[Boolean])

class VacuumSimulator(state: VacuumWorldState, agent: DiscreteAgent[VacuumPercept,VacuumAction])
    extends DiscreteAgentSimulator[VacuumWorldState,VacuumPercept,VacuumAction](
      state,
      new VacuumEnvironment(),
      agent,
      VacuumPerformance,
      1000)

object VacuumPerformance extends PerformanceEvaluator[VacuumWorldState,VacuumAction] {
  def evaluate(state: VacuumWorldState, action: VacuumAction): Long = state.dirt.count { !_ }
}

class PenalizingVacuumSimulator(state: VacuumWorldState, agent: DiscreteAgent[VacuumPercept,VacuumAction])
  extends DiscreteAgentSimulator[VacuumWorldState,VacuumPercept,VacuumAction](
    state,
    new VacuumEnvironment(),
    agent,
    PenalizingVacuumPerformance,
    1000)

object PenalizingVacuumPerformance extends PerformanceEvaluator[VacuumWorldState,VacuumAction] {
  def evaluate(state: VacuumWorldState, action: VacuumAction): Long = {
    val cleanSquares = state.dirt.count { !_ }
    val penalty = action match {
      case Left() => 1
      case Right() => 1
      case Suck() => 0
    }
    cleanSquares - penalty
  }
}
sealed trait VacuumAction
case class Right() extends VacuumAction
case class Left() extends VacuumAction
case class Suck() extends VacuumAction

case class VacuumPercept(isDirty: Boolean)