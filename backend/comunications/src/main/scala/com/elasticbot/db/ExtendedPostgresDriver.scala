package com.elasticbot.db

import com.elasticbot.services.answer.model.ActionType
import com.github.tminglei.slickpg._
import com.github.tminglei.slickpg.trgm.PgTrgmSupport


trait ExtendedPostgresDriver extends ExPostgresProfile
  with PgArraySupport
  with PgEnumSupport
  with PgTrgmSupport
  with PgPlayJsonSupport {
  def pgjson = "jsonb"

  trait CustomTypeImplicits {
    implicit val answerActionTypeMapping = createEnumJdbcType("answer_type_enum", ActionType)
    implicit val answerActionTypeMappingList = createEnumListJdbcType("answer_type_enum", ActionType)
    implicit val answerActionTypeMappingColumn = createEnumColumnExtensionMethodsBuilder(ActionType)
  }

  override val api = new API
    with JsonImplicits
    with ArrayImplicits
    with PgTrgmImplicits
    with CustomTypeImplicits {}
}

object ExtendedPostgresDriver extends ExtendedPostgresDriver