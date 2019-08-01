package com.elasticbot.db

import com.github.tminglei.slickpg._


trait ExtendedPostgresDriver extends ExPostgresProfile
  with PgArraySupport
  with PgEnumSupport
  with PgPostGISSupport {

  trait CustomTypeImplicits {

  }

  override val api = new API with ArrayImplicits
    with PostGISImplicits
    with CustomTypeImplicits {}
}

object ExtendedPostgresDriver extends ExtendedPostgresDriver