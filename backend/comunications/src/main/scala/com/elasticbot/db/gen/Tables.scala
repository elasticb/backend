package com.elasticbot.db.gen
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = com.elasticbot.db.ExtendedPostgresDriver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Answer.schema ++ Question.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Answer
   *  @param id Database column id SqlType(bigserial), AutoInc, PrimaryKey
   *  @param answer Database column answer SqlType(jsonb), Length(2147483647,false)
   *  @param questionFk Database column question_fk SqlType(int8) */
  case class AnswerRow(id: Long, answer: String, questionFk: Long)
  /** GetResult implicit for fetching AnswerRow objects using plain SQL queries */
  implicit def GetResultAnswerRow(implicit e0: GR[Long], e1: GR[String]): GR[AnswerRow] = GR{
    prs => import prs._
    AnswerRow.tupled((<<[Long], <<[String], <<[Long]))
  }
  /** Table description of table answer. Objects of this class serve as prototypes for rows in queries. */
  class Answer(_tableTag: Tag) extends profile.api.Table[AnswerRow](_tableTag, "answer") {
    def * = (id, answer, questionFk) <> (AnswerRow.tupled, AnswerRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(answer), Rep.Some(questionFk))).shaped.<>({r=>import r._; _1.map(_=> AnswerRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(bigserial), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column answer SqlType(jsonb), Length(2147483647,false) */
    val answer: Rep[String] = column[String]("answer", O.Length(2147483647,varying=false))
    /** Database column question_fk SqlType(int8) */
    val questionFk: Rep[Long] = column[Long]("question_fk")

    /** Foreign key referencing Question (database name answer_question_fk_fkey) */
    lazy val questionFkX = foreignKey("answer_question_fk_fkey", questionFk, Question)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Answer */
  lazy val Answer = new TableQuery(tag => new Answer(tag))

  /** Entity class storing rows of table Question
   *  @param id Database column id SqlType(bigserial), AutoInc, PrimaryKey
   *  @param uid Database column uid SqlType(varchar), Length(200,true), Default(unassigned)
   *  @param question Database column question SqlType(text) */
  case class QuestionRow(id: Long, uid: String = "unassigned", question: String)
  /** GetResult implicit for fetching QuestionRow objects using plain SQL queries */
  implicit def GetResultQuestionRow(implicit e0: GR[Long], e1: GR[String]): GR[QuestionRow] = GR{
    prs => import prs._
    QuestionRow.tupled((<<[Long], <<[String], <<[String]))
  }
  /** Table description of table question. Objects of this class serve as prototypes for rows in queries. */
  class Question(_tableTag: Tag) extends profile.api.Table[QuestionRow](_tableTag, "question") {
    def * = (id, uid, question) <> (QuestionRow.tupled, QuestionRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(uid), Rep.Some(question))).shaped.<>({r=>import r._; _1.map(_=> QuestionRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(bigserial), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column uid SqlType(varchar), Length(200,true), Default(unassigned) */
    val uid: Rep[String] = column[String]("uid", O.Length(200,varying=true), O.Default("unassigned"))
    /** Database column question SqlType(text) */
    val question: Rep[String] = column[String]("question")
  }
  /** Collection-like TableQuery object for table Question */
  lazy val Question = new TableQuery(tag => new Question(tag))
}
