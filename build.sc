// build.sc
import mill._, scalajslib._, scalalib._, scalafmt._

trait MyScalaJSModule extends ScalaJSModule with ScalafmtModule {
  def scalaVersion = "2.13.3"
  def scalaJSVersion = "1.1.1"
  def ivyDeps = Agg(
    ivy"org.scala-js:scalajs-dom_sjs1_2.13:1.0.0"
  )
}

object framework extends MyScalaJSModule

object game extends MyScalaJSModule {
  def moduleDeps = Seq(framework)
}

object server extends ScalaModule with ScalafmtModule {
  def scalaVersion = "2.13.3"
  def ivyDeps = Agg(
    ivy"com.lihaoyi::scalatags:0.9.1",
    ivy"com.lihaoyi::cask:0.6.7"
  )
}
