package io.sudostream.api_antagonist.screenwriter.api.http

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer

import scala.concurrent.ExecutionContextExecutor

trait Health {
  implicit def executor: ExecutionContextExecutor
  implicit val materializer: Materializer

  val health : Route = path("health") {
    get {
      complete {
        val okMessage = "Amateur screenwriter is all okay!\n"
        HttpResponse(StatusCodes.OK, entity = okMessage)
      }
    }
  }

}
