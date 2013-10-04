package com.sciolizer.aima.search

import java.awt.{BasicStroke, Point}
import java.awt.geom.{Rectangle2D, Area, Line2D}

// First created by Joshua Ball on 9/30/13 at 9:16 PM
case class PolygonProblem(
                           initialPoint: Point,
                           finalPoint: Point,
                           polygons: List[List[Point]])
  extends Problem[Point, Point] {

  val initialState: Point = initialPoint

  private val allPoints = polygons.flatten.toSet + initialPoint + finalPoint

  private val lineSegments: Set[Line2D] = {
    var result: Set[Line2D] = Set()
    for (polygon <- polygons; i <- 0 until polygon.size) {
      if (i < polygon.size - 1) {
        result = result + new Line2D.Double(polygon(i), polygon(i + 1))
      } else if (i == polygon.size - 1) {
        result = result + new Line2D.Double(polygon(i), polygon(0))
      } // else polygon.size <= 1, in which case there are no lines to draw
    }
    result
  }

  val reachable: Map[Point, Set[Point]] = {
    // initial point
    var result: Map[Point, Set[Point]] = Map()

    var reachableFromInitial: Set[Point] = Set()
    for (polygon <- polygons; point <- polygon) {
      if (unblocked(initialPoint, point)) {
        reachableFromInitial = reachableFromInitial + point
      }
    }
    if (unblocked(initialPoint, finalPoint)) {
      reachableFromInitial = reachableFromInitial + finalPoint
    }
    result = result + (initialPoint -> reachableFromInitial)

    for (polygonI <- 0 until polygons.size; sourcePointI <- 0 until polygons(polygonI).size) {
      val sourcePoint: Point = polygons(polygonI)(sourcePointI)
      var reachableFromSourcePoint: Set[Point] = Set()
      for (
        polygonJ <- 0 until polygons.size;
        targetPointI <- 0 until polygons(polygonJ).size
        if polygonI != polygonJ || math.abs(sourcePointI - targetPointI) == 1) {
        val targetPoint: Point = polygons(polygonJ)(targetPointI)
        if (polygonI == polygonJ || unblocked(sourcePoint, targetPoint)) {
          reachableFromSourcePoint = reachableFromSourcePoint + targetPoint
        }
      }
      result = result + (sourcePoint -> reachableFromSourcePoint)
    }

    var reachableFromFinal: Set[Point] = Set()
    for (polygon <- polygons; point <- polygon) {
      if (unblocked(finalPoint, point)) {
        reachableFromFinal = reachableFromFinal + point
      }
    }
    if (unblocked(initialPoint, finalPoint)) {
      reachableFromFinal = reachableFromFinal + initialPoint
    }
    result = result + (finalPoint -> reachableFromInitial)
    result
  }

  private def unblocked(p1: Point, p2: Point): Boolean = {
    println("p1: " + p1 + ", p2: " + p2)
    val candidate = new Line2D.Double(p1, p2)
    lineSegments.forall(x =>
      if (!candidate.intersectsLine(x)) {
        true
      } else {
        // todo: move some of this outside
        val stroke: BasicStroke = new BasicStroke(1)
        val candidateLine: Area = new Area(stroke.createStrokedShape(candidate))
        val otherStrokedLine = stroke.createStrokedShape(x)
        candidateLine.intersect(new Area(otherStrokedLine))
        val bounds2D: Rectangle2D = candidateLine.getBounds2D
        println("width: " + bounds2D.getWidth + ", height: " + bounds2D.getHeight)
        bounds2D.getWidth <= 2 && bounds2D.getHeight <= 2
      }
    )
  }

  def actions(state: Point): Set[Point] = reachable(state)

  def result(state: Point, action: Point): Point = action

  def goalTest(state: Point): Boolean = state == finalPoint

  def stepCost(state: Point, action: Point): Double = action.distance(state)
}
