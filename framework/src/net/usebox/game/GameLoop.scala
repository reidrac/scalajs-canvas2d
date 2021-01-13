package net.usebox.game

import org.scalajs.dom.window

trait GameLoop {
  val updatesPerSecond: Double = 1 / 80.toDouble
  val framesPerSecond: Double = 1 / 60.toDouble

  var dt: Double = 0
  var tThen: Double = 0

  def update: Unit
  def draw: Unit

  def loop(tNow: Double): Unit = {
    dt += Math.min(framesPerSecond, tNow - tThen)
    while (dt >= updatesPerSecond) {
      update
      dt -= updatesPerSecond
    }

    draw

    tThen = tNow
    window.requestAnimationFrame(loop)
  }

  def startLoop: Unit = loop(0)
}
