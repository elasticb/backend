package setup

import play.api.inject.guice.GuiceApplicationBuilder

trait Injector {
  lazy val injector = new GuiceApplicationBuilder().build().injector
}
