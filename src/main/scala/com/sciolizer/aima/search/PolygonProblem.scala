package com.sciolizer.aima.search

import java.awt.{Shape, BasicStroke, Point}
import java.awt.geom._

// First created by Joshua Ball on 9/30/13 at 9:16 PM
case class PolygonProblem(
                           initialPoint: Point,
                           finalPoint: Point,
                           polygons: List[Shape])
  extends Problem[Point, Point] {

  val initialState: Point = initialPoint

  private val allPoints = {
    def corners(shape: Shape): List[Point] = {
      val iterator: PathIterator = shape.getPathIterator(null)
      var result: List[Point] = List()
      val points: Array[Double] = Array(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
      while (!iterator.isDone) {
        val segment: Int = iterator.currentSegment(points)
        if (segment == PathIterator.SEG_MOVETO || segment == PathIterator.SEG_LINETO) {
          result = result :+ new Point(points(0).toInt, points(1).toInt)
        }
        iterator.next()
      }
      result
    }
    polygons.map(corners).flatten.toSet + initialPoint + finalPoint
  }

  val reachable: Map[Point, Set[Point]] = {
    val stroke: BasicStroke = new BasicStroke(1)
    (for (source <- allPoints) yield {
      val reachables: Set[Point] = (for (target <- allPoints if source != target) yield {
        val strokedLine = new Area(stroke.createStrokedShape(new Line2D.Double(source, target)))
        def crosses(polygon: Shape): Boolean = {
          val area: Area = new Area(polygon)
          area.intersect(strokedLine)
          val bounds2D: Rectangle2D = area.getBounds2D
          bounds2D.getWidth >= 2 && bounds2D.getHeight >= 2
        }
        if (polygons.forall(!crosses(_))) Some(target) else None
      }).flatten
      source -> reachables
    }).toMap
  }

  def actions(state: Point): Set[Point] = reachable(state)

  def result(state: Point, action: Point): Point = action

  def goalTest(state: Point): Boolean = state == finalPoint

  def stepCost(state: Point, action: Point): Double = action.distance(state)
}
