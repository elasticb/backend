package com.elasticbot.db

import com.elasticbot.db.gen.Tables

object DatabaseTables {
  lazy val QuestionsTable = Tables.Question
  type QuestionType = Tables.Question#TableElementType

  lazy val AnswersTable = Tables.Answer
  type AnswerType = Tables.Answer#TableElementType
}
