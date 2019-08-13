package com.elasticbot.services.bots.pbot

import org.scalatest.AsyncFlatSpec
import play.api.inject.guice.GuiceApplicationBuilder
import setup.Injector

import scala.concurrent.ExecutionContext

class PBotKeysEncodingServiceSpec extends AsyncFlatSpec with Injector {
  val testSubject = injector.instanceOf(classOf[PBotKeysEncodingService])
  implicit val ex = ExecutionContext.global

  behavior of "PBotKeysEncodingService"

  it should "returns signature keys" in {
    val keys = testSubject.getSignatureKeys()
    assert(!keys.a.isEmpty)
    assert(!keys.b.isEmpty)
    assert(!keys.c.isEmpty)
    assert(!keys.d.isEmpty)
    assert(!keys.e.isEmpty)
    assert(!keys.t.isEmpty)
    assert(!keys.x.isEmpty)
  }
}
