// build.sc
import mill._, scalajslib._, scalalib._, scalafmt._

object game extends ScalaJSModule with ScalafmtModule {
  def scalaVersion = "2.13.1"
  def scalaJSVersion = "1.0.1"

  def ivyDeps = Agg(
    ivy"org.scala-js:scalajs-dom_sjs1_2.13:1.0.0"
  )
}
