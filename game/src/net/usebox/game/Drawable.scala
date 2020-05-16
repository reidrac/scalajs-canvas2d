package net.usebox.game

import org.scalajs.dom

trait Drawable {
  def image: dom.html.Image
  def x: Double
  def y: Double
}
