package com.elasticbot.services.answer.model

import play.api.libs.json.{Format, Json}

object AnswersModel {
  sealed trait Answer
  case class TextAnswer(answer: String) extends Answer

  object TextAnswer {
    implicit val fmt: Format[TextAnswer] = Json.format
  }

  object Answer {
    implicit val fmt: Format[Answer] = Json.format
  }
}
