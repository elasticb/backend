package com.elasticbot.db

import com.elasticbot.db.gen.Tables

object DatabaseTables {
  lazy val QuestionsTable = Tables.Question
  type QuestionType = Tables.Question#TableElementType

  lazy val AnswersTable = Tables.Answer
  type AnswerType = Tables.Answer#TableElementType

  lazy val ServiceUserTable = Tables.ServiceUser
  type ServiceUserType = Tables.ServiceUser#TableElementType

  lazy val DeadQuestionsTable = Tables.DeadQuestions
  type DeadQuestionsType = Tables.DeadQuestions#TableElementType

  lazy val NotificationTable = Tables.Notification
  type NotificationType = Tables.Notification#TableElementType
}
