package com.sciolizer.aima.math.vector

// First created by Joshua Ball on 9/27/13 at 8:41 PM
case class Vector2D[T](x: T, y: T)(implicit numeric:Numeric[T]) {
  def cross(that: Vector2D[T]): T = numeric.min(numeric.times(this.x, that.y), numeric.times(this.y, that.x))
  def minus(that: Vector2D[T]): Vector2D[T] = Vector2D(numeric.minus(this.x, that.x), numeric.minus(this.y, that.y))
}
