package com.elasticbot.services.answer

import com.elasticbot.services.answer.GetAnswerService.{AnswerError, GetAnswerRequest, GetAnswerResponse}
import com.elasticbot.services.answer.model.AnswersModel.Answer
import com.elasticbot.utils.Service.Service
import com.google.inject.{Inject, Singleton}
import play.api.libs.json.{Format, Json}

import scala.concurrent.Future

@Singleton
class GetAnswerService @Inject() () extends Service[GetAnswerRequest, GetAnswerResponse]{
  override def apply(request: GetAnswerRequest): Future[GetAnswerResponse] = {
    Future.successful(AnswerError)
  }
}

object GetAnswerService {
  sealed trait GetAnswerResponse
  case object AnswerError extends GetAnswerResponse
  case class AnswerSuccess(answer: Answer) extends GetAnswerResponse

  case class GetAnswerRequest(question: String)

  object AnswerSuccess {
    implicit val fmt: Format[AnswerSuccess] = Json.format
  }

  object GetAnswerRequest {
    implicit val fmt: Format[GetAnswerRequest] = Json.format
  }
}