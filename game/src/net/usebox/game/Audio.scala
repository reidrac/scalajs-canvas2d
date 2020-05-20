package net.usebox.game

import scala.scalajs.js
import org.scalajs.dom

class Audio(resources: Map[String, js.Object]) {
  val sounds: Map[String, dom.html.Audio] =
    resources
      .filter { case (k, v) => v.isInstanceOf[dom.html.Audio] }
      .map { case (k, v) => (k, v.asInstanceOf[dom.html.Audio]) }
      .toMap

  def play(
      resourceName: String,
      loop: Boolean = false,
      clone: Boolean = true
  ): dom.html.Audio = {
    val playable =
      if (clone)
        sounds(resourceName).cloneNode(true).asInstanceOf[dom.html.Audio]
      else sounds(resourceName)
    playable.loop = loop
    playable.play
    playable
  }

}
