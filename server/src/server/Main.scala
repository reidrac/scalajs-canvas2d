package server

import io.undertow.server.handlers.BlockingHandler
import io.undertow.server.handlers.accesslog.{
  AccessLogHandler,
  AccessLogReceiver
}
import scalatags.Text.all._
import cask.util.Logger

class ConsoleAccessLogReceiver(logger: Logger) extends AccessLogReceiver {
  override def logMessage(message: String): Unit = logger.debug(message)
}

object DevServer extends cask.MainRoutes {

  @cask.staticFiles("/resources")
  def staticResourceRoutes() = "./game/resources"

  @cask.staticFiles("/main.js")
  def appRoute() = "./out/game/fastOpt/dest/out.js"

  @cask.get("/")
  def index() =
    doctype("html")(
      html(
        body(
          script(src := "/main.js", `type` := "text/javascript"),
          canvas(id := "game")
        )
      )
    )

  override def defaultHandler =
    new BlockingHandler(
      new AccessLogHandler(
        super.defaultHandler,
        new ConsoleAccessLogReceiver(log),
        "common",
        Console.getClass.getClassLoader
      )
    )

  initialize()
}
