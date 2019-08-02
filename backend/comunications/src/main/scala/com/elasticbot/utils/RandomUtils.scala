package com.elasticbot.utils

import scala.util.Random

object RandomUtils {
  private[this] lazy val random = new Random

  def getRandomElement[A](list: Seq[A]): A =
    list(random.nextInt(list.length))
}
