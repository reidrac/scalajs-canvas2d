package game

import scala.scalajs.js
import org.scalajs.dom

import net.usebox.game._

class MyLoader(canvasElementId: String) extends Loader {

  val renderer = new CanvasRenderer(canvasElementId)

  val sources = List(
    imageLoader("font", "resources/font.png"),
    audioLoader("effect", "resources/test.ogg"),
    objectLoader("map", "resources/test.json")
  )

  def onload(resources: Map[String, js.Object]): Unit =
    new MyGame(
      renderer,
      new KeyboardController(),
      new Audio(resources),
      resources
    ).run
}
