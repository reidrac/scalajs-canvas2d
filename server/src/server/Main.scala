package server

import scala.io.Source

import scalatags.Text.all._

object DevServer extends cask.MainRoutes {

  @cask.staticFiles("/resources")
  def staticResourceRoutes() = "./game/resources"

  @cask.get("/app/main.js")
  def app() =
    cask.Response(
      Source.fromFile("./out/game/fastOpt/dest/out.js").mkString,
      headers = Seq("Content-Type" -> "text/javascript")
    )

  @cask.get("/")
  def index() =
    doctype("html")(
      html(
        body(
          script(src := "/app/main.js", `type` := "text/javascript"),
          canvas(id := "game")
        )
      )
    )

  initialize()
}
