package game

import scala.collection.mutable

import scala.scalajs.js
import org.scalajs.dom

import net.usebox.game._

class MyGame(val renderer: CanvasRenderer, resources: Map[String, js.Object])
    extends GameLoop {

  def update: Unit = {}

  def draw: Unit =
    renderer.render {
      // renderer.draw(d)
    }

  def run: Unit = ()
}

class MyLoader(canvasElementId: String)(implicit
    executionContext: scala.concurrent.ExecutionContextExecutor
) extends Loader {

  val renderer = new CanvasRenderer(canvasElementId)

  def run: Unit =
    load(
      List(imageLoader("font", "resources/font.png"))
    ) {
      case Left(error) =>
        val p = dom.document.createElement("p")
        p.textContent = s"ERROR loading resources: $error"
        dom.document.body.appendChild(p)

      case Right(resources) => new MyGame(renderer, resources).run
    }
}

object Main {
  implicit def executionContext =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  def main(args: Array[String]): Unit = {
    dom.document.addEventListener(
      "DOMContentLoaded",
      { (e: dom.Event) => new MyLoader("game").run }
    )
  }
}
