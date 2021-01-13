package game

import org.scalajs.dom

object Main {
  implicit def executionContext =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  def main(args: Array[String]): Unit =
    dom.document.addEventListener(
      "DOMContentLoaded",
      { (e: dom.Event) => new MyLoader("game").run }
    )
}
