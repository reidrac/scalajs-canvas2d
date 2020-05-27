package net.usebox.game

import org.scalajs.dom.window

trait GameLoop {
  val updatesPerSecond: Double = 1 / 80.toDouble
  val framesPerSecond: Double = 1 / 60.toDouble

  var dt: Double = 0
  var then: Double = 0

  def update: Unit
  def draw: Unit

  def loop(now: Double): Unit = {
    dt += Math.min(framesPerSecond, now - then)
    while (dt >= updatesPerSecond) {
      update
      dt -= updatesPerSecond
    }

    draw

    then = now
    window.requestAnimationFrame(loop)
  }

  def startLoop: Unit = loop(0)
}
