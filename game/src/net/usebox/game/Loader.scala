package net.usebox.game

import scala.concurrent.{Future, Promise}

import scala.scalajs.js
import org.scalajs.dom

trait Loader {
  val renderer: CanvasRenderer
  var loaded: Int = 0

  def imageLoader(
      name: String,
      src: String
  ): Future[(String, dom.html.Image)] = {
    val p = Promise[(String, dom.html.Image)]()
    val image =
      dom.document.createElement("img").asInstanceOf[dom.html.Image]
    image.src = src
    image.addEventListener(
      "error",
      (event: dom.Event) =>
        p.failure(new RuntimeException(s"Failed to load $src"))
    )
    image.onload = { (event: dom.Event) =>
      image.onload = null
      loaded.synchronized {
        loaded += 1
      }
      p.success((name, image))
    }
    p.future
  }

  def load(
      resources: List[Future[(String, js.Object)]]
  )(block: (Either[Throwable, Map[String, js.Object]]) => Unit)(implicit
      executionContext: scala.concurrent.ExecutionContextExecutor
  ): Unit = {
    loadingProgress(resources.size)

    Future.sequence(resources).onComplete { result =>
      block(result.toEither.map { loaded =>
        loaded
          .map { case (name, element) => (name, element) }
          .toMap[String, js.Object]
      })
    }
  }

  private def loadingProgress(size: Int) = {
    def loading(now: Double): Unit = {
      renderer.render {
        renderer.ctx.fillStyle = "rgb(128, 128, 128)"
        renderer.ctx.fillRect(
          Math.floor(renderer.width * 0.2),
          Math.floor(renderer.height / 2) - 4,
          renderer.width - Math.floor(renderer.width * 0.4),
          4
        )
        renderer.ctx.fillStyle = "rgb(255, 255, 255)"
        renderer.ctx.fillRect(
          Math.floor(renderer.width * 0.2),
          Math.floor(renderer.height / 2) - 4,
          (loaded * (renderer.width - Math
            .floor(renderer.width * 0.4))) / size,
          4
        )
      }

      if (loaded != size)
        dom.window.requestAnimationFrame(loading)
    }

    loading(0)
  }

}
