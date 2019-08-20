package com.elasticbot.services.deadquestions

import com.elasticbot.db.{DatabaseTables, ElasticbotDatabase}
import com.elasticbot.services.deadquestions.AddDeadQuestionService.{AddDeadQuestionServiceRequest, AddDeadQuestionServiceResponse, CantAddQuestionError, OK}
import com.elasticbot.utils.Service.Service
import com.google.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

@Singleton
class AddDeadQuestionService @Inject() (database: ElasticbotDatabase)
  extends Service[AddDeadQuestionServiceRequest, AddDeadQuestionServiceResponse]{
  import com.elasticbot.db.ExtendedPostgresDriver.api._
  private[this] implicit val ec: ExecutionContextExecutor = ExecutionContext.global

  override def apply(request: AddDeadQuestionServiceRequest): Future[AddDeadQuestionServiceResponse] = {
    addDeadQuestion(request) map {
      case Success(_) => OK
      case Failure(errors) => CantAddQuestionError(errors.toString)
    }
  }

  private[this] def addDeadQuestion(request: AddDeadQuestionServiceRequest) = {
    val query = DatabaseTables.DeadQuestionsTable
      .map(question => (question.question, question.uid)) +=
      ((request.question, request.uid))

    database.writableDatabase.run(query.asTry)
  }
}

object AddDeadQuestionService {
  sealed trait AddDeadQuestionServiceResponse
  case object OK extends AddDeadQuestionServiceResponse
  case class CantAddQuestionError(errors: String) extends AddDeadQuestionServiceResponse

  case class AddDeadQuestionServiceRequest(uid: String, question: String)
}
