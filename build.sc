// build.sc
import mill._, scalajslib._, scalalib._, scalafmt._

trait MyScalaJSModule extends ScalaJSModule with ScalafmtModule {
  def scalaVersion = "2.13.4"
  def scalaJSVersion = "1.4.0"

  def scalacOptions = Seq("-deprecation")

  def ivyDeps = Agg(
    ivy"org.scala-js:scalajs-dom_sjs1_2.13:1.1.0"
  )

  override def compile = T {
    reformat().apply()
    super.compile()
  }
}

object framework extends MyScalaJSModule

object game extends MyScalaJSModule {
  def moduleDeps = Seq(framework)
}

object server extends ScalaModule with ScalafmtModule {
  def scalaVersion = "2.13.4"
  def ivyDeps = Agg(
    ivy"com.lihaoyi::scalatags:0.9.1",
    ivy"com.lihaoyi::cask:0.6.7"
  )
}
