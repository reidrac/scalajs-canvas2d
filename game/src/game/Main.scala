package game

import scala.collection.mutable

import scala.scalajs.js
import org.scalajs.dom

import net.usebox.game._

class MyGame() extends Loader with GameLoop {
  implicit def executionContext =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  val renderer: CanvasRenderer = new CanvasRenderer("game")

  def update: Unit = {}

  def draw: Unit =
    renderer.render {
      // renderer.draw(d)
    }

  load(
    List(
      imageLoader("image", "resources/font.png"),
      imageLoader("image1", "resources/font.png"),
      imageLoader("image2", "resources/font.png"),
      imageLoader("image3", "resources/font.png"),
      imageLoader("image4", "resources/font.png"),
      imageLoader("image5", "resources/font.png"),
      imageLoader("image6", "resources/font.png"),
      imageLoader("image7", "resources/font.png"),
      imageLoader("image8", "resources/font.png")
    )
  ) {
    case Left(error) =>
      val p = dom.document.createElement("p")
      p.textContent = s"ERROR loading resources: $error"
      dom.document.body.appendChild(p)

    case Right(resources) => startLoop
  }
}

object Main {
  def main(args: Array[String]): Unit = {
    dom.document.addEventListener(
      "DOMContentLoaded",
      { (e: dom.Event) =>
        new MyGame()
      }
    )
  }
}
