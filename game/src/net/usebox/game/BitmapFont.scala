package net.usebox.game

import org.scalajs.dom

class BitmapFont(
    image: dom.html.Image,
    width: Int,
    height: Int,
    fontMap: Option[String] = None
) {
  val map: String = fontMap.getOrElse(
    "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!?()@:/'.,- *"
  )

  def renderText(text: String): Sprite = {
    val canvas =
      dom.document.createElement("canvas").asInstanceOf[dom.html.Canvas]
    canvas.width = text.size * width
    canvas.height = height
    val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

    ctx.clearRect(0, 0, canvas.width, canvas.height)
    text.foldLeft(0)((acc, c) => {
      ctx.drawImage(
        image,
        map.indexOf(c) * width,
        0,
        width,
        height,
        acc * width,
        0,
        width,
        height
      )
      acc + 1
    })

    val outputImage =
      dom.document.createElement("img").asInstanceOf[dom.html.Image]
    outputImage.src = canvas.toDataURL("image/png", null)
    Sprite(outputImage, 0, 0)
  }
}
