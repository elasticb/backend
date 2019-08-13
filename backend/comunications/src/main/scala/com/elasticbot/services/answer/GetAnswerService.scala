package com.elasticbot.services.answer

import com.elasticbot.db.DatabaseTables.AnswerType
import com.elasticbot.db.{DatabaseTables, _}
import com.elasticbot.services.answer.GetAnswerService.{AnswerError, AnswerNotFound, AnswerSuccess, GetAnswerRequest, GetAnswerResponse}
import com.elasticbot.services.answer.model.ActionType
import com.elasticbot.services.answer.model.AnswersModel.{Answer, TextAnswer}
import com.elasticbot.services.bots.pbot.GetPBotAnswerService
import com.elasticbot.services.bots.pbot.GetPBotAnswerService.GetPBotAnswerRequest
import com.elasticbot.utils.{Logging, RandomUtils}
import com.elasticbot.utils.Service.Service
import com.google.inject.{Inject, Singleton}
import play.api.libs.json.{Format, Json}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

@Singleton
class GetAnswerService @Inject() (database: ElasticbotDatabase, pBotService: GetPBotAnswerService)
  extends Service[GetAnswerRequest, GetAnswerResponse] with Logging {
  import com.elasticbot.db.ExtendedPostgresDriver.api._
  private[this] implicit val ec: ExecutionContextExecutor = ExecutionContext.global

  override def apply(request: GetAnswerRequest): Future[GetAnswerResponse] = {
    for {
      databaseAnswers <- getDatabaseAnswers(request.question)
      serviceResponse <- getServiceResponse(databaseAnswers, request)
    } yield serviceResponse
  }

  private[this] def getDatabaseAnswers(question: String) = {
    log.debug(s"getDatabaseAnswers, question: $question")
    val query = DatabaseTables.QuestionsTable
      .filter(_.question  % question)
      .sortBy(_.question <-> question)
      .take(1)
      .join(DatabaseTables.AnswersTable)
      .on{case (questions, answers) => answers.questionFk === questions.id}
      .map{case (_, answers) => answers}
      .result

    database.writableDatabase.run(query)
  }

  private[this] def getServiceResponse(answers: Seq[AnswerType], request: GetAnswerRequest) = {
    log.debug(s"getServiceResponse(answers) values: $answers")
    answers.isEmpty match {
      case true =>
        log.info("Empty answers")
        log.info("Try to get answer from pbot")
        getPBotAnswer(request)
      case false =>
        log.info(s"Non empty answers: $answers")
        getServiceAnswer(getRandomAnswer(answers)) match {
          case Some(answer) => Future.successful(AnswerSuccess(answer))
          case None => Future.successful(AnswerError)
        }
    }
  }

  private[this] def getPBotAnswer(request: GetAnswerRequest) = {
    pBotService(GetPBotAnswerRequest(request.question)).map {
      case Left(errors) =>
        log.warn(s"PBotAnswerResponse errors: $errors")
        AnswerNotFound
      case Right(response) =>
        log.info(s"PBotAnswer response: $response")
        AnswerSuccess(TextAnswer(response.answer))
    }
  }

  private[this] def getServiceAnswer(answer: AnswerType) = {
    answer.actionType match {
      case ActionType.TextResponse => getTextAnswer(answer)
    }
  }

  private[this] def getTextAnswer(answer: AnswerType) = {
    answer.answerValue.validate[TextAnswer].asOpt
  }

  private[this] def getRandomAnswer(answers: Seq[AnswerType]): AnswerType = {
    RandomUtils.getRandomElement(answers)
  }
}

object GetAnswerService {
  sealed trait GetAnswerResponse
  case object AnswerError extends GetAnswerResponse
  case object AnswerNotFound extends GetAnswerResponse
  case class AnswerSuccess(answer: Answer) extends GetAnswerResponse

  case class GetAnswerRequest(question: String)

  object AnswerSuccess {
    implicit val fmt: Format[AnswerSuccess] = Json.format
  }

  object GetAnswerRequest {
    implicit val fmt: Format[GetAnswerRequest] = Json.format
  }
}