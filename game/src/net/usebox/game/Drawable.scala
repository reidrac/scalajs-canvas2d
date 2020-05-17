package net.usebox.game

import org.scalajs.dom

trait Drawable {
  def image: dom.html.Element
  def x: Double
  def y: Double
}

case class Sprite(var image: dom.html.Element, x: Double, y: Double)
    extends Drawable
