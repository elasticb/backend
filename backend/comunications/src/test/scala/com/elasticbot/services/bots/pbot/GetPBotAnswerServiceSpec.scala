package com.elasticbot.services.bots.pbot

import com.elasticbot.services.bots.pbot.GetPBotAnswerService.GetPBotAnswerRequest
import com.elasticbot.utils.Logging
import org.scalatest.AsyncFlatSpec
import play.api.inject.guice.GuiceApplicationBuilder
import setup.Injector

import scala.concurrent.ExecutionContext

class GetPBotAnswerServiceSpec extends AsyncFlatSpec with Logging with Injector {
  behavior of "GetPBotAnswerService"
  val testSubject = injector.instanceOf(classOf[GetPBotAnswerService])
  implicit val ex = ExecutionContext.global

  it should "check if pBot service is available" in {
    for {
      response <- testSubject(GetPBotAnswerRequest("мур"))
    } yield {
      response match {
        case Left(_) => fail("No answer from pBot service")
        case Right(response) =>
          log.debug("pBot response: " + response)
          assert(1 == 1)
      }
    }
  }

}
