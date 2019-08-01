package com.elasticbot.utils

import scala.concurrent.Future

object Service {
  type ServiceM[M[+_], -In, +Out] = In => M[Out]

  type Service[-In, +Out] = ServiceM[Future, In, Out]
}
