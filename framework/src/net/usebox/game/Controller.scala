package net.usebox.game

import scala.collection.mutable

import org.scalajs.dom
import org.scalajs.dom.ext.KeyCode

trait Controller {
  def up: Boolean
  def down: Boolean
  def left: Boolean
  def right: Boolean
  def buttonA: Boolean
  def buttonB: Boolean
  def buttonC: Boolean
  def start: Boolean

  def isGamepad: Boolean
}

class KeyboardController() extends Controller {
  val keysState = mutable.Set.empty[Int]

  def up: Boolean = keysState(KeyCode.Up)
  def down: Boolean = keysState(KeyCode.Down)
  def left: Boolean = keysState(KeyCode.Left)
  def right: Boolean = keysState(KeyCode.Right)
  def buttonA: Boolean = keysState(KeyCode.Z)
  def buttonB: Boolean = keysState(KeyCode.X)
  def buttonC: Boolean = keysState(KeyCode.C)
  def start: Boolean = keysState(KeyCode.Enter)

  val isGamepad: Boolean = false

  def keydown(event: dom.KeyboardEvent): Unit =
    event.keyCode match {
      case KeyCode.Up | KeyCode.Down | KeyCode.Left | KeyCode.Right |
          KeyCode.Z | KeyCode.X | KeyCode.C | KeyCode.Enter =>
        keysState += event.keyCode
        event.preventDefault()
      case _ =>
    }

  def keyup(event: dom.KeyboardEvent): Unit = keysState -= event.keyCode

  dom.window.addEventListener("keydown", keydown, false)
  dom.window.addEventListener("keyup", keyup, false)
}
