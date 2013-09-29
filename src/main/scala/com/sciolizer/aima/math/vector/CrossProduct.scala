package com.sciolizer.aima.math.vector

// First created by Joshua Ball on 9/27/13 at 8:25 PM
trait CrossProduct {
  implicit class Crossable[X <: { def -(x: X): X; def *(x: X): X }](xs: Traversable[X]) {
    def cross(ys: Traversable[X]): X = {
      // v*x = vx wy − vywx
      val xList: scala.List[X] = xs.take(3).toList
      if (xList.size != 2) throw new IllegalArgumentException("xs must be size 2")
      val yList: List[X] = ys.take(3).toList
      if (yList.size != 2) throw new IllegalArgumentException("ys must be size 2")
      (xList(0) * yList(1)) - (xList(1) * yList(0))
    }
  }
}

object CrossProduct {
  import CrossProduct._
  def main(args: Array[String]) {
//    List(1, 2) cross List(3, 4)
  }
}
