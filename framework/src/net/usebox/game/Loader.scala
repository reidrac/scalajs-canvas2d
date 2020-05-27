package net.usebox.game

import scala.concurrent.{Future, Promise}

import scala.scalajs.js
import org.scalajs.dom

trait Loader {
  def run: Unit
  def renderer: CanvasRenderer

  var loaded: Int = 0

  def imageLoader(
      name: String,
      src: String
  ): Future[(String, dom.html.Image)] = {
    val p = Promise[(String, dom.html.Image)]()
    val image =
      dom.document.createElement("img").asInstanceOf[dom.html.Image]
    image.src = src

    val onerror: js.Function1[dom.Event, Unit] = { (event: dom.Event) =>
      p.failure(new RuntimeException(s"Failed to load $src"))
    }
    image.addEventListener("error", onerror)

    image.onload = { (event: dom.Event) =>
      event.currentTarget.removeEventListener("error", onerror)
      image.onload = null
      loaded.synchronized {
        loaded += 1
      }
      p.success((name, image))
    }
    p.future
  }

  def audioLoader(
      name: String,
      src: String
  ): Future[(String, dom.html.Audio)] = {
    val p = Promise[(String, dom.html.Audio)]()
    val audio =
      dom.document.createElement("audio").asInstanceOf[dom.html.Audio]
    audio.src = src
    audio.autoplay = false

    val onerror: js.Function1[dom.Event, Unit] = { (event: dom.Event) =>
      p.failure(new RuntimeException(s"Failed to load $src"))
    }
    audio.addEventListener("error", onerror)

    audio.oncanplaythrough = { (event: dom.Event) =>
      audio.oncanplaythrough = null
      audio.removeEventListener("error", onerror)
      loaded.synchronized {
        loaded += 1
      }
      p.success((name, audio))
    }
    p.future
  }

  def load(
      sources: List[Future[(String, js.Object)]]
  )(block: (Either[Throwable, Map[String, js.Object]]) => Unit)(implicit
      executionContext: scala.concurrent.ExecutionContextExecutor
  ): Unit = {
    loadingProgress(sources.size)

    Future.sequence(sources).onComplete { result =>
      block(result.toEither.map { loaded =>
        loaded
          .map { case (name, element) => (name, element) }
          .toMap[String, js.Object]
      })
    }
  }

  private def loadingProgress(size: Int) = {
    def loading(now: Double): Unit = {
      renderer.ctx.save
      renderer.ctx.fillStyle = "rgb(255, 255, 255)"
      renderer.ctx.font = "caption"
      renderer.ctx.fillText(
        "Loading",
        Math.floor(renderer.width * 0.2 * renderer.scale),
        Math.floor((renderer.height / 2 - 6) * renderer.scale)
      )
      renderer.ctx.restore

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
