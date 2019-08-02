package com.elasticbot.utils

import org.slf4j.LoggerFactory

trait Logging {
  protected[this] lazy val log = {
    val className = getClass.getName
    LoggerFactory.getLogger(className)
  }
}
