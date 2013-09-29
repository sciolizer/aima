package com.sciolizer.aima.search

import javax.swing.{JLabel, JFrame}

// First created by Joshua Ball on 9/28/13 at 9:20 PM
class Polygons {
  def go() {
    val frame: JFrame = new JFrame("HelloWorldSwing")
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

    //Add the ubiquitous "Hello World" label.
    val label: JLabel = new JLabel("Hello World")
    frame.getContentPane().add(label)

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
