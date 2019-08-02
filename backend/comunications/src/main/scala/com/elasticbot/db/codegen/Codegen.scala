package com.elasticbot.db.codegen

import com.elasticbot.db.ExtendedPostgresDriver
import slick.codegen.SourceCodeGenerator

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, ExecutionContextExecutor}

object Codegen extends App {
  implicit val eс: ExecutionContextExecutor = ExecutionContext.global
  import com.elasticbot.db.ExtendedPostgresDriver.api._

  val db = Database.forConfig("db")
  val requiredTables = Seq("question", "answer")

  println("TABLES")
  println(ExtendedPostgresDriver.defaultTables.map(_.map(_.name.name).foreach(println)))
  val modelAction = ExtendedPostgresDriver.defaultTables
    .map(_.filter(p => requiredTables.contains(p.name.name.toLowerCase)))
      .flatMap(ExtendedPostgresDriver.createModelBuilder(_, false).buildModel)
  val modelFuture = db.run(modelAction)

  val codegen = modelFuture.map(model => new SourceCodeGenerator(model) {
    override def Table = new Table(_) {
      override def Column = new Column(_){
        override def rawType =
          model.name match {
            case "answer_value" => "play.api.libs.json.JsValue"
            case "action_type" => "com.elasticbot.services.answer.model.ActionType.ActionType"
            case _ => super.rawType
          }
      }
    }
  })

  Await.result(codegen.map(cg =>
    cg.writeToFile(
      "com.elasticbot.db.ExtendedPostgresDriver",
      "src/main/scala/",
      "com.elasticbot.db.gen",
      "Tables")), Duration.Inf)
}
