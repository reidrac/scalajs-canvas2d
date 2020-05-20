package net.usebox.game

import scala.scalajs.js
import org.scalajs.dom

class Audio(resources: Map[String, js.Object], limit: Int = 16) {
  val sounds: Map[String, dom.html.Audio] =
    resources
      .filter { case (k, v) => v.isInstanceOf[dom.html.Audio] }
      .map { case (k, v) => (k, v.asInstanceOf[dom.html.Audio]) }
      .toMap

  var playingCount: Int = 0

  def play(
      resourceName: String,
      loop: Boolean = false,
      clone: Boolean = true
  ): Option[dom.html.Audio] = {
    if (playingCount >= limit)
      None
    else {
      val playable =
        if (clone)
          sounds(resourceName).cloneNode(true).asInstanceOf[dom.html.Audio]
        else sounds(resourceName)

      playable.onended = { (event: dom.Event) =>
        playable.onended = null
        playingCount.synchronized {
          playingCount -= 1
        }
      }
      playable.loop = loop
      playable.play
      playingCount.synchronized {
        playingCount += 1
      }
      Some(playable)
    }
  }

}
