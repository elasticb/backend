package com.elasticbot.services.bots.pbot

import java.util.UUID

import com.elasticbot.services.bots.pbot.GetPBotAnswerService.{GetPBotAnswerRequest, GetPBotAnswerResponse, PBotResponse}
import com.elasticbot.utils.Logging
import com.elasticbot.utils.Service.Service
import com.google.inject.{Inject, Singleton}
import org.apache.http.Consts
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.{BasicResponseHandler, HttpClients}
import org.apache.http.message.BasicNameValuePair
import play.api.libs.json.{Format, JsValue, Json}
import play.api.libs.ws.WSClient

import scala.collection.JavaConversions._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

@Singleton
class GetPBotAnswerService @Inject()(ws: WSClient, urlEncodingService: PBotKeysEncodingService)(implicit val executionContext: ExecutionContext)
  extends Service[GetPBotAnswerRequest, Either[String, GetPBotAnswerResponse]] with Logging {
  private[this] def DEFAULT_PBOT_API_URL = "http://p-bot.ru/api/getAnswer"

  override def apply(request: GetPBotAnswerRequest): Future[Either[String, GetPBotAnswerResponse]] = {
    for {
      httpResponse <- getHTTPResponse(request)
      ccResponse = getBodyCC(httpResponse)
    } yield {
      ccResponse match {
        case Some(response) => Right(GetPBotAnswerResponse(response.answer))
        case None => Left("Can't get response")
      }
    }
  }

  private[this] def getHTTPResponse(request: GetPBotAnswerRequest) =
    ws.url(DEFAULT_PBOT_API_URL).post(getRequestParams(request)).map(response => response.json)

  private[this] def getBodyCC(response: JsValue): Option[PBotResponse] =
    response.validateOpt[PBotResponse].asOpt.flatten

  private[this] def getRequestParams(request: GetPBotAnswerRequest) = {
    val keys = urlEncodingService.getSignatureKeys()

    Map(
      "request" -> request.question,
      "bot_name" -> "ρBot",
      "user_name" -> request.userName,
      "dialog_lang" -> "ru",
      "dialog_id" -> request.dialogId,
      "dialog_greeting" -> "false",
      "a" -> keys.a,
      "b" -> keys.b,
      "c" -> keys.c,
      "d" -> keys.d,
      "e" -> keys.e,
      "t" -> keys.t,
      "x" -> keys.x
    )
  }
}

object GetPBotAnswerService {

  case class GetPBotAnswerRequest(question: String,
                                  dialogId: String = UUID.randomUUID().toString,
                                  userName: String = "Незнакомец")

  case class GetPBotAnswerResponse(answer: String)


  case class PBotResponseSet(commands: Seq[String] = Seq.empty, mood: Int, sentiment: Int)

  case class PBotResponseAnswer(set: PBotResponseSet, variants: Seq[String] = Seq.empty)

  case class PBotResponsePattern(_id: String,
                                 request: String,
                                 context: String,
                                 author: String,
                                 __v: Int,
                                 score: Double,
                                 created: String,
                                 useCount: Int,
                                 rating: Double,
                                 dislikes: Int,
                                 likes: Int,
                                 mood: Int,
                                 sentiment: Int,
                                 mainbase: Boolean,
                                 question: Boolean,
                                 answer: PBotResponseAnswer,
                                 bot: String,
                                 resultAnswer: String)

  case class PBotResponse(status: String, time: String, answer: String, pattern: PBotResponsePattern)

  object PBotResponseSet {
    implicit val fmt: Format[PBotResponseSet] = Json.format
  }

  object PBotResponseAnswer {
    implicit val fmt: Format[PBotResponseAnswer] = Json.format
  }

  object PBotResponsePattern {
    implicit val fmt: Format[PBotResponsePattern] = Json.format
  }

  object PBotResponse {
    implicit val fmt: Format[PBotResponse] = Json.format
  }

}
