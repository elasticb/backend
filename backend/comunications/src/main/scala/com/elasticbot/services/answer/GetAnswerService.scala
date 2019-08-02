package com.elasticbot.services.answer

import com.elasticbot.db.DatabaseTables.AnswerType
import com.elasticbot.db.{DatabaseTables, _}
import com.elasticbot.services.answer.GetAnswerService.{AnswerError, AnswerNotFound, AnswerSuccess, GetAnswerRequest, GetAnswerResponse}
import com.elasticbot.services.answer.model.ActionType
import com.elasticbot.services.answer.model.AnswersModel.{Answer, JsonAnswer, TextAnswer}
import com.elasticbot.utils.{Logging, RandomUtils}
import com.elasticbot.utils.Service.Service
import com.google.inject.{Inject, Singleton}
import play.api.libs.json.{Format, Json}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

@Singleton
class GetAnswerService @Inject() (database: ElasticbotDatabase)
  extends Service[GetAnswerRequest, GetAnswerResponse] with Logging {
  import com.elasticbot.db.ExtendedPostgresDriver.api._
  private[this] implicit val ec: ExecutionContextExecutor = ExecutionContext.global

  override def apply(request: GetAnswerRequest): Future[GetAnswerResponse] = {
    for {
      databaseAnswers <- getDatabaseAnswers(request.question)
    } yield getServiceResponse(databaseAnswers)
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

  private[this] def getServiceResponse(answers: Seq[AnswerType]) = {
    log.debug(s"getServiceResponse(answers) values: $answers")
    answers.isEmpty match {
      case true =>
        log.info("Empty answers")
        AnswerNotFound
      case false =>
        log.info(s"Non empty answers: $answers")
        getServiceAnswer(getRandomAnswer(answers)) match {
          case Some(answer) => AnswerSuccess(answer)
          case None => AnswerError
        }
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