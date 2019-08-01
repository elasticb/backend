package com.elasticbot.db

import com.google.inject.Singleton
import slick.jdbc.JdbcBackend

@Singleton
class ElasticbotDatabase {
  lazy val writableDatabase = JdbcBackend.Database.forConfig("db")
}
