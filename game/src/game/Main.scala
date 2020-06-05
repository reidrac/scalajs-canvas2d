package game

import scala.collection.mutable

import scala.scalajs.js
import org.scalajs.dom

import net.usebox.game._

class MyGame(
    renderer: CanvasRenderer,
    controller: Controller,
    audio: Audio,
    resources: Map[String, js.Object]
) extends GameLoop {

  val font =
    new BitmapFont(resources("font").asInstanceOf[dom.html.Image], 6, 11)

  def update: Unit = {

    if (controller.buttonA) {
      println("Played")
      audio.play("effect")
    }

  }

  def draw: Unit =
    renderer.render {
      renderer.ctx.clearRect(0, 0, renderer.width, renderer.height)
      renderer.draw(font.renderText(10, 10, "Testing font"))
    }

  def run: Unit = startLoop
}

class MyLoader(canvasElementId: String)(implicit
    executionContext: scala.concurrent.ExecutionContextExecutor
) extends Loader {

  val renderer = new CanvasRenderer(canvasElementId)
  val controller = new KeyboardController()

  def run: Unit =
    load(
      List(
        imageLoader("font", "/resources/font.png"),
        audioLoader("effect", "/resources/test.ogg")
      )
    ) {
      case Left(error) =>
        val p = dom.document.createElement("p")
        p.textContent = s"ERROR loading resources: $error"
        dom.document.body.appendChild(p)

      case Right(resources) =>
        new MyGame(renderer, controller, new Audio(resources), resources).run
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
