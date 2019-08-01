package com.elasticbot.controllers

import com.google.inject.Inject
import play.api.libs.json.{JsError, JsPath, JsValue, Json, JsonValidationError, Reads, Writes}
import play.api.mvc.{AbstractController, Action, ActionBuilder, AnyContent, BodyParser, ControllerComponents, Request, Result, Results}

import scala.concurrent.{ExecutionContext, Future}

class ElastibotController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {
  def jsonOk() = jsonOkWithStatus(Ok)
  def jsonOk[R: Writes](r: R) = Ok(Json.obj("status" -> "OK", "result" -> Json.toJson(r)))
  def jsonOkWithStatus(status: Results#Status) = status(Json.obj("status" -> "OK"))
  def jsonOkWithNonconformingResponse[R: Writes](key: String, r: R) =
    Ok(Json.obj("status" -> "OK", key -> Json.toJson(r)))
  def jsonFail(status: Results#Status) = status(Json.obj("status" -> "KO"))
  def jsonFail(status: Results#Status, message: String) = status(Json.obj("status" -> "KO", "message" -> message))
  def jsonFail[R: Writes](status: Results#Status, r: R) = status(Json.obj("status" -> "KO", "message" -> Json.toJson(r)))

  private[this] val trackMetrics = new TrackMetrics()

  def jsonAsyncPost[BODY] = new {
    def apply(
               body: (Request[_], BODY) => Future[Result])
             (implicit reads: Reads[BODY]) : Action[JsValue] = {
      trackMetrics.async(controllerComponents.parsers.tolerantJson) { implicit request =>
        request.body.validate[BODY].fold(
          handleErrors(request, _),
          handleParseResults(body, request)
        )
      }
    }
  }

  private[this] def handleErrors(request: Request[JsValue], errors: Seq[(JsPath, Seq[JsonValidationError])]) = {
    Future.successful(jsonFail(BadRequest, JsError.toJson(errors)))
  }

  private[this] def handleParseResults[BODY](bodyParser: (Request[_], BODY) => Future[Result], request: Request[JsValue]) = {
    bodyParser(request, _ : BODY)
  }

  private class TrackMetrics extends ActionBuilder[Request, AnyContent] {
    override def parser: BodyParser[AnyContent] = controllerComponents.parsers.defaultBodyParser

    override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] = {
      block(request)
    }

    override protected def executionContext: ExecutionContext = ExecutionContext.global
  }
}