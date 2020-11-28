package game

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

  println(resources("map").asInstanceOf[js.Dynamic].hello)

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
