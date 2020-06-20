package game

import org.scalajs.dom

import net.usebox.game._

class MyLoader(canvasElementId: String)(implicit
    executionContext: scala.concurrent.ExecutionContextExecutor
) extends Loader {

  val renderer = new CanvasRenderer(canvasElementId)

  def run: Unit =
    load(
      List(
        imageLoader("font", "resources/font.png"),
        audioLoader("effect", "resources/test.ogg")
      )
    ) {
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
