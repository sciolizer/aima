package com.sciolizer.aima.math.vector

// First created by Joshua Ball on 9/27/13 at 8:52 PM
case class LineSegment[T](origin: Vector2D[T], delta: Vector2D[T]) {
  def intersect(that: LineSegment[T]): Intersection[T] = {
    // t = (q − p) × s / (r × s)
    // u = (q − p) × r / (r × s)
    null
  }
}

object LineSegment {
  def between[T](origin: Vector2D[T], destination: Vector2D[T]): LineSegment[T] = {
    LineSegment(origin, destination.minus(origin))
  }
}

sealed trait Intersection[T]

case class Coincide[T]() extends Intersection[T]
case class Parallel[T]() extends Intersection[T]
case class NonOverlapping[T]() extends Intersection[T]
case class Overlapping[T](at: Vector2D[T]) extends Intersection[T]
