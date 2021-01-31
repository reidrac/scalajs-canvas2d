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

  def onload(resources: Either[Throwable, Map[String, js.Object]]): Unit =
    resources match {
      case Left(error) =>
        val p = dom.document.createElement("p")
        p.textContent = s"ERROR loading resources: $error"
        dom.document.body.appendChild(p)

      case Right(resources) =>
        new MyGame(
          renderer,
          new KeyboardController(),
          new Audio(resources),
          resources
        ).run
    }
}
