package com.elasticbot.services.bots.pbot

import java.util.Date

import com.elasticbot.services.bots.pbot.PBotKeysEncodingService.PBotSignatureKeys
import com.google.inject.Singleton
import com.typesafe.config.ConfigFactory
import javax.script.{Compilable, Invocable, ScriptEngine, ScriptEngineManager}

import scala.io.Source

@Singleton
class PBotKeysEncodingService {
  private[this] val engine = new ScriptEngineManager().getEngineByMimeType("text/javascript").asInstanceOf[ScriptEngine with Compilable]
  private[this] val config = ConfigFactory.defaultApplication()
  private[this] val code = Source.fromFile(getClass.getResource(config.getString("pbot.signature_generator_path")).getPath).mkString
  engine.compile(code).eval()

  def getSignatureKeys(time: Long = new Date().getTime) =
    PBotSignatureKeys(
      engine.eval(s"getA($time)").toString,
      engine.eval(s"getB($time)").toString,
      engine.eval(s"getC($time)").toString,
      engine.eval(s"getD($time)").toString,
      engine.eval(s"getE($time)").toString,
      engine.eval(s"getT($time)").toString,
      engine.eval(s"getX()").toString
    )

}

object PBotKeysEncodingService {
  case class PBotSignatureKeys(a: String, b: String, c: String, d: String, e: String, t: String, x: String)
}
