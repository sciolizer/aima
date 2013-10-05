package com.sciolizer.aima.search

import javax.swing.{JLabel, JFrame}
import scala.swing._
import scala.swing.Swing._
import java.awt._
import java.awt.event.MouseEvent
import java.awt.geom._
import java.awt.Point
import scala.swing.Panel
import scala.swing.Button
import java.awt.Graphics2D
import scala.List
import java.awt.Color
import scala.swing.event.MousePressed
import scala.swing.event.MouseReleased
import scala.swing.event.MouseDragged
import scala.swing.event.KeyTyped
import scala.swing.event.FocusLost

// First created by Joshua Ball on 9/28/13 at 9:20 PM
class Polygons {
  def go() {
    val frame: JFrame = new JFrame("HelloWorldSwing")
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

    //Add the ubiquitous "Hello World" label.
    val label: JLabel = new JLabel("Hello World")
    frame.getContentPane.add(label)

    //Display the window.
    frame.pack()
    frame.setVisible(true)
  }
}

object Polygons {
  def main(args: Array[String]) {
    new Polygons().go()
  }
}

/**
 * A simple swing demo.
 */
object HelloWorld extends SimpleSwingApplication {
  def top = new MainFrame {
    title = "Hello, World!"
    contents = new Button("Click Me!")
  }
}

object LinePainting extends SimpleSwingApplication {
  lazy val ui = new Panel {
    background = Color.white
    preferredSize = (200, 200)

    focusable = true
    listenTo(mouse.clicks, mouse.moves, keys)

    reactions += {
      case e: MousePressed =>
        moveTo(e.point)
        requestFocusInWindow()
      case e: MouseDragged => lineTo(e.point)
      case e: MouseReleased => lineTo(e.point)
      case KeyTyped(_, 'c', _, _) =>
        path = new geom.GeneralPath
        repaint()
      case _: FocusLost => repaint()
    }

    /* records the dragging */
    var path = new geom.GeneralPath

    def lineTo(p: Point) {
      path.lineTo(p.x, p.y)
      repaint()
    }

    def moveTo(p: Point) {
      path.moveTo(p.x, p.y)
      repaint()
    }

    override def paintComponent(g: Graphics2D) = {
      super.paintComponent(g)
      g.setColor(new Color(100, 100, 100))
      g.drawString("Press left mouse button and drag to paint." +
        (if (hasFocus) " Press 'c' to clear." else ""), 10, size.height - 10)
      g.setColor(Color.black)
      g.draw(path)
    }
  }

  def top = new MainFrame {
    title = "Simple Line Painting Demo"
    contents = ui
  }
}

object PolygonPainting extends SimpleSwingApplication {
  lazy val ui = new Panel {
    background = Color.white
    preferredSize = (200, 200)

    focusable = true
    listenTo(mouse.clicks, mouse.moves, keys)

    reactions += {
      case e: MousePressed =>
        if (e.peer.getButton == MouseEvent.BUTTON2) {
          polygons = List(e.point) +: polygons
        } else {
          if (polygons.isEmpty) {
            polygons = List() +: polygons
          }
          polygons = (e.point +: polygons.head) +: polygons.tail
        }
        repaint()
        requestFocusInWindow()
      //      case e: MouseDragged  => lineTo(e.point)
      //      case e: MouseReleased => lineTo(e.point)
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
    val search: SearchResult[Point, Point] = new UniformCostSearch().search(problem)
    search match {
      case Failure() => throw new Exception("failure")
      case Solution(node) => node.actions
    }
  }
}

object AreaSandbox {
  def main(args: Array[String]) {
    //    val area1: Area = new Area(new Line2D.Double(0, 0, 10, 10))
    //    println(area1.isEmpty)
    //    val area2: Area = new Area(new Line2D.Double(10, 10, 20, 20))
    //    println(area2.isEmpty)
    //    val intersects: Any = area1.intersects(area2)
    //    println(intersects)
    val path = new Path2D.Double()
    path.moveTo(10, 10)
    path.lineTo(10, 20)
    path.lineTo(20, 20)
    path.lineTo(20, 10)
    path.closePath()
    val iterator: PathIterator = path.getPathIterator(null)
    while (!iterator.isDone) {
      //      iterator.next()
      val vls: Array[Double] = Array(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
      val segment: Int = iterator.currentSegment(vls)
      println(segment)
      println(vls.toList)
      iterator.next()
    }
    println("end of iteration")
    val area: Area = new Area(path)
    val line = new Line2D.Double(0, 0, 5, 5)
    val strokedLine: Shape = new BasicStroke(1).createStrokedShape(line)
    println(area.isEmpty)
    area.intersect(new Area(strokedLine))
    val rect: Rectangle2D = area.getBounds2D
    println(rect.getWidth)
    println(rect.getHeight)
    //    println(area)
    //    println(area.isEmpty)
    //    println(area.isPolygonal)
  }
}