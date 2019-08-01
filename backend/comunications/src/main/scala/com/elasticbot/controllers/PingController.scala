package com.elasticbot.controllers

import com.google.inject.Inject
import play.api.mvc.ControllerComponents

class PingController @Inject() (cc: ControllerComponents) extends ElastibotController(cc) {
  def ping = Action { implicit request =>
    jsonOk()
  }
}