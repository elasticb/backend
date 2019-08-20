package setup

import java.util.UUID

import com.elasticbot.db.DatabaseTables
import com.elasticbot.services.answer.model.ActionType
import com.elasticbot.services.answer.model.ActionType.ActionType
import play.api.libs.json.{JsValue, Json}
import slick.jdbc.JdbcBackend

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

trait DatabaseObjectGenerator {
  import com.elasticbot.db.ExtendedPostgresDriver.api._
  private[this] implicit val ec: ExecutionContextExecutor = ExecutionContext.global
  private[this] lazy val random = scala.util.Random

  protected val db = JdbcBackend.Database.forConfig("db")
  def randomString(): String = UUID.randomUUID().toString.take(12)
  def randomLong(): Long = random.nextLong() + 100000

  def generateServiceUser(
     uid: String = randomString(),
     firstName: String = randomString(),
     lastName: String = randomString()) = {
    val query = DatabaseTables.ServiceUserTable
      .map(user => (user.uid, user.firstName, user.lastName))
      .returning(DatabaseTables.ServiceUserTable.map(_.uid))
      .+= (uid, firstName, lastName)

    db.run(query)
  }

  def generateQuestion(
    id: Long = randomLong(),
    uid: Option[String] = None,
    question: String = randomString()) = {
    val query = DatabaseTables.QuestionsTable
      .map(question => (question.id, question.uid, question.question))
      .returning(DatabaseTables.QuestionsTable.map(_.id))
      .+= ((id, uid, question))

    db.run(query)
  }

  def generateAnswer(
      id: Long = randomLong(),
      actionType: ActionType = ActionType.TextResponse,
      answerValue: JsValue = Json.obj("answer" -> "тестовый ответ"),
      questionFk: Long) = {
    val query = DatabaseTables.AnswersTable
      .map(answer => (answer.id, answer.actionType, answer.answerValue, answer.questionFk))
      .returning(DatabaseTables.AnswersTable.map(_.id))
      .+= ((id, actionType, answerValue, questionFk))

    db.run(query)
  }

  def generateDeadQuestions(id: Long = randomLong(),
                            uid: String,
                            question: String = randomString()) = {
    val query = DatabaseTables.DeadQuestionsTable
      .map(question => (question.id, question.uid, question.question))
      .returning(DatabaseTables.DeadQuestionsTable)
      .+= ((id, uid, question))

    db.run(query)
  }

  def generateNotification(id: Long = randomLong(),
                           notifyDate: Option[java.sql.Date] = None,
                           title: String = randomString(),
                           content: Option[String] = None) = {
    val query = DatabaseTables.NotificationTable
      .map(notification => (notification.id, notification.notifyDate, notification.title, notification.content))
      .returning(DatabaseTables.NotificationTable.map(_.id))
      .+= ((id, notifyDate, title, content))

    db.run(query)
  }
}
