package net.usebox.game

import scala.scalajs.js
import org.scalajs.dom

class Audio(resources: Map[String, js.Object], limit: Int = 8) {
  val sounds: Map[String, dom.html.Audio] =
    resources.flatMap {
      case (k, v: dom.html.Audio) => Some((k, v))
      case _                      => None
    }

  var playingCount: Int = 0

  def createInstance(resourceName: String): dom.html.Audio =
    sounds(resourceName).cloneNode(true).asInstanceOf[dom.html.Audio]

  def play(
      resourceName: String,
      loop: Boolean = false,
      clone: Boolean = true
  ): Option[dom.html.Audio] =
    if (playingCount >= limit)
      None
    else {
      val playable =
        if (clone)
          createInstance(resourceName)
        else sounds(resourceName)

      playable.onended = { (event: dom.Event) =>
        playable.onended = null
        playingCount.synchronized {
          playingCount -= 1
        }
      }
      playable.loop = loop
      playable.play()
      playingCount.synchronized {
        playingCount += 1
      }
      Some(playable)
    }
}
