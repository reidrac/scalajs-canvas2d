// build.sc
import $ivy.`com.lihaoyi::mill-contrib-bloop:0.9.5`
import mill._, scalajslib._, scalalib._, scalafmt._

trait MyScalaJSModule extends ScalaJSModule with ScalafmtModule {
  def scalaVersion = "2.13.6"
  def scalaJSVersion = "1.7.0"

  def scalacOptions =
    Seq(
      // features
      "-encoding",
      "utf-8",
      "-explaintypes",
      // warnings
      "-deprecation",
      "-Xlint:unused",
      "-unchecked"
    )

  def ivyDeps =
    Agg(
      ivy"org.scala-js:scalajs-dom_sjs1_2.13:1.1.0"
    )
}

object framework extends MyScalaJSModule

object game extends MyScalaJSModule {
  def moduleDeps = Seq(framework)
}

object server extends ScalaModule with ScalafmtModule {
  def scalaVersion = "2.13.6"
  def ivyDeps =
    Agg(
      ivy"com.lihaoyi::scalatags:0.9.3",
      ivy"com.lihaoyi::cask:0.7.8"
    )
}
