package com.elasticbot.controllers.utils

import com.elasticbot.services.answer.GetAnswerService.GetAnswerRequest
import play.api.mvc.QueryStringBindable

import scala.util.{Failure, Success, Try}

object Binders {
  implicit def getAnswerRequest(implicit intBinder: QueryStringBindable[Int]) = new QueryStringBindable[GetAnswerRequest] {
    override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, GetAnswerRequest]] = {
      Try(Right(GetAnswerRequest (
        getByKey(params, "question").get
      ))) match {
        case Success(value) => Option(Right(value.value))
        case Failure(exception) => Option(Left(exception.toString))
      }
    }

    override def unbind(key: String, value: GetAnswerRequest): String = {
      value.toString
    }
  }

  private[this] def getByKey(params: Map[String, Seq[String]], key: String): Option[String] = {
    params.get(key) match {
      case Some(values) => values.headOption
      case None => None
    }
  }

  private[this] def getByKey[A](params: Map[String, Seq[String]], key: String, converter: String => A): Option[A] = {
    getByKey(params, key) match {
      case Some(head) => Option(converter(head))
      case None => None
    }
  }

}

