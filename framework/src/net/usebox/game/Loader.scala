package net.usebox.game

import scala.concurrent.{Future, Promise}

import scala.scalajs.js
import org.scalajs.dom

trait Loader {

  /** Renderer to show the loading progress. */
  def renderer: CanvasRenderer

  /** Resources to load.
    *
   *  Expected to be (name, object).
    */
  def sources: List[Future[(String, js.Object)]]

  /** Method to be called when the loading progress finishes. */
  def onload(resources: Either[Throwable, Map[String, js.Object]]): Unit

  def objectLoader(
      name: String,
      src: String
  ): Future[(String, js.Object)] = {
    val p = Promise[(String, js.Object)]()
    val onError = onerror(src, p)
    val xhr = new dom.XMLHttpRequest
    xhr.open("GET", src)
    xhr.responseType = "json"
    xhr.addEventListener("error", onError)

    xhr.onload = { (event: dom.Event) =>
      event.currentTarget.removeEventListener("error", onError)
      xhr.onload = null
      if (xhr.status == 200) {
        loadCount.synchronized {
          loadCount += 1
        }
        p.success((name, xhr.response.asInstanceOf[js.Object]))
      } else
        p.failure(new RuntimeException(s"Failed to load $src"))
    }
    xhr.send()
    p.future
  }

  def imageLoader(
      name: String,
      src: String
  ): Future[(String, dom.html.Image)] = {
    val p = Promise[(String, dom.html.Image)]()
    val onError = onerror(src, p)
    val image =
      dom.document.createElement("img").asInstanceOf[dom.html.Image]
    image.src = src
    image.addEventListener("error", onError)

    image.onload = { (event: dom.Event) =>
      event.currentTarget.removeEventListener("error", onError)
      image.onload = null
      loadCount.synchronized {
        loadCount += 1
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
    val onError = onerror(src, p)
    val audio =
      dom.document.createElement("audio").asInstanceOf[dom.html.Audio]
    audio.src = src
    audio.autoplay = false
    audio.addEventListener("error", onError)

    audio.oncanplaythrough = { (event: dom.Event) =>
      audio.oncanplaythrough = null
      audio.removeEventListener("error", onError)
      loadCount.synchronized {
        loadCount += 1
      }
      p.success((name, audio))
    }
    p.future
  }

  def load(implicit
      executionContext: scala.concurrent.ExecutionContextExecutor
  ): Unit = {
    loadingProgress(sources.size)

    Future.sequence(sources).onComplete { result =>
      onload(result.toEither.map { loaded =>
        loaded
          .map { case (name, element) => (name, element) }
          .toMap[String, js.Object]
      })
    }
  }

  private var loadCount: Int = 0

  private def onerror(
      src: String,
      p: Promise[_]
  ): js.Function1[dom.Event, Unit] = { (event: dom.Event) =>
    p.failure(new RuntimeException(s"Failed to load $src"))
  }

  private def loadingProgress(size: Int) = {
    def loading(now: Double): Unit = {
      renderer.ctx.save()
      renderer.ctx.fillStyle = "rgb(255, 255, 255)"
      renderer.ctx.font = "caption"
      renderer.ctx.fillText(
        "Loading",
        Math.floor(renderer.width * 0.2 * renderer.scale),
        Math.floor((renderer.height / 2 - 6) * renderer.scale)
      )
      renderer.ctx.restore()

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
          (loadCount * (renderer.width - Math
            .floor(renderer.width * 0.4))) / size,
          4
        )
      }

      if (loadCount != size)
        dom.window.requestAnimationFrame(loading)
    }

    loading(0)
  }
}
