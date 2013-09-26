package com.sciolizer.aima.agent

// First created by Joshua Ball on 9/25/13 at 10:42 PM
trait PerformanceEvaluator[-State,-Action] {
  def evaluate(state: State, action: Action): Long
}
