package io.sudostream.api_antagonist.screenwriter.api.http

import java.io.{PrintWriter, StringWriter}

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpResponse, Multipart, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import akka.stream.scaladsl.StreamConverters
import akka.util.Timeout
import io.sudostream.api_antagonist.messages.SpeculativeScreenplay
import io.sudostream.api_antagonist.screenwriter.api.SwaggerJsonApiToScreenplayWriterConverter

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContextExecutor, Future}

trait ProcessApiDefinition extends Health
  with io.sudostream.api_antagonist.screenwriter.api.kafka.ProcessApiDefinition {

  implicit def executor: ExecutionContextExecutor

  implicit val system: ActorSystem
  implicit val materializer: Materializer
  implicit val timeout = Timeout(30.seconds)

  val routes: Route = path("amateur-screenwriter" / "api-definition") {
    (post & entity(as[Multipart.FormData])) { fileData =>
      complete {
        processFile(fileData).map { speculativeScreenplay =>
          publishSingleEventToKafka(speculativeScreenplay)
          HttpResponse(StatusCodes.OK, entity = "\n" + speculativeScreenplay + "\n\n")
        }.recover {
          case ex: Exception =>
            val stacktrace = new StringWriter
            ex.printStackTrace(new PrintWriter(stacktrace))
            HttpResponse(StatusCodes.InternalServerError, entity = "Error in file uploading\n"
              + stacktrace.toString + "\n\n")
        }
      }
    }
  } ~ health

  private def processFile(fileData: Multipart.FormData): Future[SpeculativeScreenplay] = {
    val swaggerDefinitionFuture: Future[String] = fileData.parts.mapAsync(1) { bodyPart ⇒
      val inputStream = bodyPart.entity.dataBytes.runWith(StreamConverters.asInputStream())
      val swaggerSpec = scala.io.Source.fromInputStream(inputStream).getLines
      Future {
        swaggerSpec.mkString
      }
    }.runFold("")(_ + _)

    swaggerDefinitionFuture flatMap {
      swaggerDefinition =>
        val amateurScreenplayWriter = new SwaggerJsonApiToScreenplayWriterConverter().passApiDefinitionToScreenplayWriterAmateur(swaggerDefinition)

        val specScreenplay = if (amateurScreenplayWriter.isDefined) {
          Some(amateurScreenplayWriter.get.generateHappyPathTests)
        } else {
          None
        }

        Future {
          specScreenplay.get
        }
    }
  }

}
