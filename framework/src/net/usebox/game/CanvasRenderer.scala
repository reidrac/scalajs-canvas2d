package net.usebox.game

import scala.scalajs.js
import scala.scalajs.js.annotation._
import org.scalajs.dom

trait Renderer {
  def render(block: => Unit): Unit
  def draw(drawable: Drawable): Unit
  def ctx: GameRenderingContext
  def width: Int
  def height: Int
}

@js.native
@JSGlobal
class GameRenderingContext extends dom.CanvasRenderingContext2D {
  var imageSmoothingEnabled: Boolean = js.native
}

class CanvasRenderer(
    canvasElementId: String,
    var backgroundColor: String = "rgb(21, 21, 21)",
    var width: Int = 320,
    var height: Int = 240,
    val maxScale: Int = 3
) extends Renderer {
  val canvas: dom.html.Canvas =
    dom.document
      .getElementById(canvasElementId)
      .asInstanceOf[dom.html.Canvas]
  val ctx: GameRenderingContext =
    canvas.getContext("2d").asInstanceOf[GameRenderingContext]

  var scale: Int = 1

  def render(block: => Unit): Unit = {
    ctx.save
    ctx.scale(scale, scale)
    block
    ctx.restore
  }

  def draw(drawable: Drawable): Unit =
    ctx.drawImage(
      drawable.image,
      Math.floor(drawable.x),
      Math.floor(drawable.y)
    )

  def onResize(event: dom.UIEvent): Unit = resize
  def resize: Unit = {
    scale = List[Int](
      Math.floor(dom.window.innerHeight / height.toDouble).toInt,
      maxScale
    ).min
    ctx.canvas.width = width * scale
    ctx.canvas.height = height * scale
    ctx.imageSmoothingEnabled = false
  }

  canvas.style.background = backgroundColor
  canvas.textContent = "Canvas 2D support is required"
  dom.window.onresize = onResize
  resize
}
