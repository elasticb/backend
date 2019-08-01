package com.elasticbot.db

import com.elasticbot.db.gen.Tables

class DatabaseTables {
  lazy val QuestionsTable = Tables.Question
  lazy val AnswersTable = Tables.Answer
}
