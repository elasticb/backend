package com.elasticbot.services.answer

import com.elasticbot.services.answer.GetAnswerService.{AnswerSuccess, GetAnswerRequest}
import com.elasticbot.services.answer.model.AnswersModel.TextAnswer
import org.scalatest.Matchers
import play.api.libs.json.Json
import setup.{Injector, LocalPostgresSetup}

class GetAnswerServiceSpec extends LocalPostgresSetup with Injector with Matchers {
  behavior of "GetAnswerService"
  val testSubject: GetAnswerService = injector.instanceOf(classOf[GetAnswerService])

  it should "returns answer on question if it's present in database" in {
    val questionValue = "test question"
    val answerValue = "test answer"
    for {
      questionId <- generateQuestion(question = questionValue)
      _ <- generateAnswer(questionFk = questionId, answerValue = Json.obj("answer" -> answerValue))
      serviceRequest = GetAnswerRequest(questionValue)
      serviceResponse <- testSubject(serviceRequest)
    } yield {
      serviceResponse match {
        case AnswerSuccess(answer) => answer shouldBe TextAnswer(answerValue)
        case _ => fail()
      }
    }
  }

  it should "returns answer on question if it's present in database and questions are not the same but similar" in {
    val questionValue = "test similar question"
    val answerValue = "test answer similar answer"
    for {
      questionId <- generateQuestion(question = questionValue)
      _ <- generateAnswer(questionFk = questionId, answerValue = Json.obj("answer" -> answerValue))
      serviceRequest = GetAnswerRequest(questionValue + "123")
      serviceResponse <- testSubject(serviceRequest)
    } yield {
      serviceResponse match {
        case AnswerSuccess(answer) => answer shouldBe TextAnswer(answerValue)
        case _ => fail()
      }
    }
  }
}
