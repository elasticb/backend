package com.elasticbot.services.deadquestions

import com.elasticbot.services.deadquestions.AddDeadQuestionService.{AddDeadQuestionServiceRequest, OK}
import org.scalatest.Matchers
import setup.{Injector, LocalPostgresSetup}

class AddDeadQuestionServiceSpec extends LocalPostgresSetup with Injector with Matchers {
  behavior of "AddDeadQuestionService"
  val testSubject: AddDeadQuestionService = injector.instanceOf(classOf[AddDeadQuestionService])

  it should "add new question in dead question service" in {
    for {
      uid <- generateServiceUser()
      question = randomString()
      serviceRequest = AddDeadQuestionServiceRequest(uid, question)
      serviceResponse <- testSubject(serviceRequest)
    } yield {
      serviceResponse shouldBe OK
    }
  }
}
