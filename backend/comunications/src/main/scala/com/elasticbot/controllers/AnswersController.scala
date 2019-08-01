package com.elasticbot.controllers

import com.elasticbot.services.answer.GetAnswerService
import com.elasticbot.services.answer.GetAnswerService.{AnswerError, GetAnswerRequest, AnswerSuccess}
import com.google.inject.Inject
import play.api.mvc.ControllerComponents

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

class AnswersController @Inject() (cc: ControllerComponents, getAnswerService: GetAnswerService) extends ElastibotController(cc) {
  private[this] implicit val executionContext: ExecutionContextExecutor = ExecutionContext.global

  def getAnswer(serviceRequest: GetAnswerRequest) = Action.async { implicit request =>
    getAnswerService(serviceRequest) map {
      case AnswerError => jsonFail(InternalServerError)
      case AnswerSuccess(answer) => jsonOk(answer)
    }
  }
}
