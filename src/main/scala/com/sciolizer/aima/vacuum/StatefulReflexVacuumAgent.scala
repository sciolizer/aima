package com.sciolizer.aima.vacuum

import com.sciolizer.aima.agent.DiscreteAgent
import scala.collection.mutable

// First created by Joshua Ball on 9/25/13 at 10:49 PM
class StatefulReflexVacuumAgent extends DiscreteAgent[VacuumPercept,VacuumAction] {
  var movedLeft = false
  var movedRight = false
  def act(percept: VacuumPercept): VacuumAction = {
    if (percept.isDirty) {
      Suck()
    } else if (!movedLeft) {
      movedLeft = true
      Left()
    } else if (!movedRight) {
      movedRight = true
      Right()
    } else {
      Suck()
    }
  }
}

object StatefulReflexVacuumAgent {
  def main(args: Array[String]) {
    for (start <- List(0, 1); leftDirty <- List(false, true); rightDirty <- List(false, true)) {
      val state: VacuumWorldState = VacuumWorldState(start, mutable.IndexedSeq(leftDirty, rightDirty))
      print(state + ":  ")
      val simulator: PenalizingVacuumSimulator = new PenalizingVacuumSimulator(state, new SimpleReflexVacuumAgent())
      simulator.run()
      println(simulator.score)
    }
  }
}
