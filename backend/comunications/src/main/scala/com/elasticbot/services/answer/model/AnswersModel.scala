package com.elasticbot.services.answer.model

import play.api.libs.json.{Format, JsValue, Json, Reads, Writes}

object ActionType extends Enumeration {
  type ActionType = Value
  val TextResponse = Value("text_response")

  implicit val reads = Reads.enumNameReads(ActionType)
  implicit val writes = Writes.enumNameWrites
}

object AnswersModel {
  sealed trait Answer
  case class TextAnswer(answer: String) extends Answer
  case class JsonAnswer(answer: JsValue) extends Answer

  object TextAnswer {
    implicit val fmt: Format[TextAnswer] = Json.format
  }

  object JsonAnswer {
    implicit val fmt: Format[JsonAnswer] = Json.format
  }

  object Answer {
    implicit val fmt: Format[Answer] = Json.format
  }
}
