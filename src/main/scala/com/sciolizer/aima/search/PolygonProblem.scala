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


  //  private val lineSegments: Set[Line2D] = {
  //    var result: Set[Line2D] = Set()
  //    for (polygon <- polygons; i <- 0 until polygon.size) {
  //      if (i < polygon.size - 1) {
  //        result = result + new Line2D.Double(polygon(i), polygon(i + 1))
  //      } else if (i == polygon.size - 1) {
  //        result = result + new Line2D.Double(polygon(i), polygon(0))
  //      } // else polygon.size <= 1, in which case there are no lines to draw
  //    }
  //    result
  //  }

  //  val polygonAreas: List[Area] = polygons.map(new Area(_))

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

  //  val reachable: Map[Point, Set[Point]] = {
  //    // initial point
  //    var result: Map[Point, Set[Point]] = Map()
  //
  //    var reachableFromInitial: Set[Point] = Set()
  //    for (polygon <- polygons; point <- polygon) {
  //      if (unblocked(initialPoint, point)) {
  //        reachableFromInitial = reachableFromInitial + point
  //      }
  //    }
  //    if (unblocked(initialPoint, finalPoint)) {
  //      reachableFromInitial = reachableFromInitial + finalPoint
  //    }
  //    result = result + (initialPoint -> reachableFromInitial)
  //
  //    for (polygonI <- 0 until polygons.size; sourcePointI <- 0 until polygons(polygonI).size) {
  //      val sourcePoint: Point = polygons(polygonI)(sourcePointI)
  //      var reachableFromSourcePoint: Set[Point] = Set()
  //      for (
  //        polygonJ <- 0 until polygons.size;
  //        targetPointI <- 0 until polygons(polygonJ).size
  //        if polygonI != polygonJ || math.abs(sourcePointI - targetPointI) == 1) {
  //        val targetPoint: Point = polygons(polygonJ)(targetPointI)
  //        if (polygonI == polygonJ || unblocked(sourcePoint, targetPoint)) {
  //          reachableFromSourcePoint = reachableFromSourcePoint + targetPoint
  //        }
  //      }
  //      result = result + (sourcePoint -> reachableFromSourcePoint)
  //    }
  //
  //    var reachableFromFinal: Set[Point] = Set()
  //    for (polygon <- polygons; point <- polygon) {
  //      if (unblocked(finalPoint, point)) {
  //        reachableFromFinal = reachableFromFinal + point
  //      }
  //    }
  //    if (unblocked(initialPoint, finalPoint)) {
  //      reachableFromFinal = reachableFromFinal + initialPoint
  //    }
  //    result = result + (finalPoint -> reachableFromInitial)
  //    result
  //  }

  //  private def unblocked(p1: Point, p2: Point): Boolean = {
  //    println("p1: " + p1 + ", p2: " + p2)
  //    val candidate = new Line2D.Double(p1, p2)
  //    lineSegments.forall(x =>
  //      if (!candidate.intersectsLine(x)) {
  //        true
  //      } else {
  //        // todo: move some of this outside
  //        val stroke: BasicStroke = new BasicStroke(1)
  //        val candidateLine: Area = new Area(stroke.createStrokedShape(candidate))
  //        val otherStrokedLine = stroke.createStrokedShape(x)
  //        candidateLine.intersect(new Area(otherStrokedLine))
  //        val bounds2D: Rectangle2D = candidateLine.getBounds2D
  //        println("width: " + bounds2D.getWidth + ", height: " + bounds2D.getHeight)
  //        bounds2D.getWidth <= 2 && bounds2D.getHeight <= 2
  //      }
  //    )
  //  }

  def actions(state: Point): Set[Point] = reachable(state)

  def result(state: Point, action: Point): Point = action

  def goalTest(state: Point): Boolean = state == finalPoint

  def stepCost(state: Point, action: Point): Double = action.distance(state)
}

//case class PolygonProblem2(
//initialPoint: Point,
//finalPoint: Point,
//polygons: List[Shape])
//extends Problem[Point, Point] {
//  def sandbox() {
//    val polygons1: Shape = polygons(0)
//    val iterator: PathIterator = polygons1.getPathIterator(new AffineTransform())
//    iterator.
//  }
//}
