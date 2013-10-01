package com.sciolizer.aima.search

import javax.swing.{JLabel, JFrame}
import scala.swing._
import scala.swing.Swing._
import java.awt.{Color, Graphics2D, Point, geom}
import scala.swing.event.MousePressed
import scala.swing.event.MouseDragged
import scala.swing.event.KeyTyped
import scala.swing.event.MouseReleased
import scala.swing.event.FocusLost
import java.awt.event.MouseEvent
import scala.collection.mutable

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
    preferredSize = (200,200)

    focusable = true
    listenTo(mouse.clicks, mouse.moves, keys)

    reactions += {
      case e: MousePressed  =>
        moveTo(e.point)
        requestFocusInWindow()
      case e: MouseDragged  => lineTo(e.point)
      case e: MouseReleased => lineTo(e.point)
      case KeyTyped(_,'c',_,_) =>
        path = new geom.GeneralPath
        repaint()
      case _: FocusLost => repaint()
    }

    /* records the dragging */
    var path = new geom.GeneralPath

    def lineTo(p: Point) { path.lineTo(p.x, p.y); repaint() }
    def moveTo(p: Point) { path.moveTo(p.x, p.y); repaint() }

    override def paintComponent(g: Graphics2D) = {
      super.paintComponent(g)
      g.setColor(new Color(100,100,100))
      g.drawString("Press left mouse button and drag to paint." +
        (if(hasFocus) " Press 'c' to clear." else ""), 10, size.height-10)
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
    preferredSize = (200,200)

    focusable = true
    listenTo(mouse.clicks, mouse.moves, keys)

    reactions += {
      case e: MousePressed  =>
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
      case KeyTyped(_,'c',_,_) =>
        polygons = List()
        repaint()
      case _: FocusLost => repaint()
    }

    /* records the dragging */
//    var path = new geom.GeneralPath
    var polygons: List[List[Point]] = List()

//    def lineTo(p: Point) { path.lineTo(p.x, p.y); repaint() }
//    def moveTo(p: Point) { path.moveTo(p.x, p.y); repaint() }

    override def paintComponent(g: Graphics2D) = {
      super.paintComponent(g)
      g.setColor(new Color(100,100,100))
      g.drawString("Press left mouse button and drag to paint." +
        (if(hasFocus) " Press 'c' to clear." else ""), 10, size.height-10)
      g.setColor(Color.black)
      val path = new geom.GeneralPath
      for (polygon <- polygons) {
        if (polygon.size > 2) {
          val start = polygon.head
          path.moveTo(start.x, start.y)
          for (point <- polygon) {
            path.lineTo(point.x, point.y)
          }
          path.lineTo(start.x, start.y)
        }
      }
      g.draw(path)
    }

//    moveTo((0, 0))
  }

  def top = new MainFrame {
    title = "Simple Line Painting Demo"
    contents = ui
  }
}