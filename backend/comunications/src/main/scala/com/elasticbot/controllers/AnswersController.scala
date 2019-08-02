package com.elasticbot.controllers

import com.elasticbot.services.answer.GetAnswerService
import com.elasticbot.services.answer.GetAnswerService.{AnswerError, AnswerNotFound, AnswerSuccess, GetAnswerRequest}
import com.elasticbot.utils.Logging
import com.google.inject.Inject
import play.api.mvc.ControllerComponents

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

class AnswersController @Inject() (cc: ControllerComponents, getAnswerService: GetAnswerService)
  extends ElastibotController(cc) with Logging {
  private[this] implicit val executionContext: ExecutionContextExecutor = ExecutionContext.global

  def getAnswer(serviceRequest: GetAnswerRequest) = Action.async { implicit request =>
    getAnswerService(serviceRequest) map {
      case AnswerError =>
        log.debug("AnswerError => ServerError")
        jsonFail(InternalServerError)
      case AnswerNotFound =>
        log.debug("getAnswer(): Answer not found")
        jsonFail(NotFound)
      case AnswerSuccess(answer) =>
        log.debug(s"getAnswer(): AnswerSuccess: $answer")
        jsonOk(answer)
    }
  }
}
