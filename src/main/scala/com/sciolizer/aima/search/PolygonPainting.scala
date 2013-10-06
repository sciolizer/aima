package com.sciolizer.aima.search

import scala.swing.Swing._

// implicitly converts tuples to Point2D's

import scala.swing.{MainFrame, Panel, SimpleSwingApplication}
import java.awt._
import java.awt.event.MouseEvent
import scala.swing.event.MousePressed
import scala.List
import scala.swing.event.KeyTyped
import scala.swing.event.FocusLost
import java.awt.geom.Path2D

object PolygonPainting extends SimpleSwingApplication {
  lazy val ui = new Panel {
    background = Color.white
    preferredSize = (200, 200)

    focusable = true
    listenTo(mouse.clicks, mouse.moves, keys)

    reactions += {
      case e: MousePressed =>
        if (e.peer.getButton == MouseEvent.BUTTON3) {
          polygons = List(e.point) +: polygons
        } else {
          if (polygons.isEmpty) {
            polygons = List() +: polygons
          }
          polygons = (e.point +: polygons.head) +: polygons.tail
        }
        repaint()
        requestFocusInWindow()
      case KeyTyped(_, 'c', _, _) =>
        polygons = List()
        repaint()
      case _: FocusLost => repaint()
    }

    var polygons: List[List[Point]] = List()

    override def paintComponent(g: Graphics2D) = {
      super.paintComponent(g)
      g.setColor(new Color(100, 100, 100))
      g.drawString("Press left mouse button and drag to paint." +
        (if (hasFocus) " Press 'c' to clear." else ""), 10, size.height - 10)
      g.setColor(Color.black)
      val path = new geom.GeneralPath
      for (polygon <- polygons) {
        if (polygon.size > 0) {
          val start = polygon.head
          path.moveTo(start.x, start.y)
          for (point <- polygon) {
            path.lineTo(point.x, point.y)
          }
          path.lineTo(start.x, start.y)
        }
      }
      g.draw(path)
      val bounds: Rectangle = g.getDeviceConfiguration.getBounds
      val optimal: List[Point] = OptimalPath.get(polygons, (0, 0), (bounds.width, bounds.height))
      val optimalPath = new geom.GeneralPath
      optimalPath.moveTo(0, 0)
      for (point <- optimal) {
        optimalPath.lineTo(point.x, point.y)
      }
      g.draw(optimalPath)
    }

  }

  def top = new MainFrame {
    title = "Simple Line Painting Demo"
    contents = ui
  }
}

object OptimalPath {
  def get(polygons: List[List[Point]], start: Point, end: Point): List[Point] = {
    val shapes: List[Shape] = for (polygon <- polygons) yield {
      val path = new Path2D.Double()
      var first = true
      for (point <- polygon) {
        if (first) {
          path.moveTo(point.x, point.y)
          first = false
        } else {
          path.lineTo(point.x, point.y)
        }
      }
      path
    }
    val problem = PolygonProblem(start, end, shapes)
    println(problem.reachable)
    def heuristic(p: Point): Double = p.distance(end)
    val search: SearchResult[Point, Point] = new RecursiveBestFirstSearch(heuristic).search(problem)
    search match {
      case Failure() => throw new Exception("failure")
      case Solution(node) => node.actions
    }
  }
}

