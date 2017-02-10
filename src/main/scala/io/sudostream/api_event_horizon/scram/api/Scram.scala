package io.sudostream.api_event_horizon.scram.api

import io.sudostream.api_event_horizon.messages.{HttpMethod, SpeculativeScreenplay, TestDescription}
import io.swagger.models.{Operation, Path, Swagger}

import scala.collection.JavaConverters._

class Scram(swaggerModel: Swagger) {
  private val swagger = swaggerModel

  def shortApiDescription: String = swagger.getInfo.getDescription

  def generateHappyPathTests: SpeculativeScreenplay = {

    val apiTitle = swagger.getInfo.getTitle
    val apiDescription = swagger.getInfo.getDescription
    val apiVersion = swagger.getInfo.getVersion
    val hostname = swagger.getHost
    val basePath = swagger.getBasePath
    val schemes = for (scheme <- swagger.getSchemes.asScala.toList) yield scheme.toValue
    val ports = List(80)

    val happyPathTests = extractTheHappyTests

    SpeculativeScreenplay(
      apiTitle, Some(apiDescription), Some(apiVersion), hostname, Some(basePath), schemes, ports, happyPathTests)
  }

  def extractTheHappyTests: List[TestDescription] = {
    val happyPathTests = collection.mutable.Set[TestDescription]()

    val paths = swagger.getPaths.asScala
    for ((resourceAsString, resourceObject) <- paths) {
      for (operation <- Array("get", "post", "put", "delete")) {

        val methodOperation: Option[(HttpMethod, Operation)] = extractHttpMethod(resourceObject, operation)
        if (methodOperation.isDefined) {
          val methodOperationPair = methodOperation.get
          val httpMethod = methodOperationPair._1
          val httpReturnCodes = methodOperationPair._2.getResponses.asScala.toSeq.map(
            entry => entry._1.toInt
          ).toList

          val singleTest = TestDescription(resourceAsString, httpMethod, httpReturnCodes)
          happyPathTests.add(singleTest)
        }
      }
    }

    happyPathTests.toList
  }

  def extractHttpMethod(resourceObject: Path, operation: String): Option[(HttpMethod, Operation)] = {
    val method: Option[(HttpMethod, Operation)] = operation match {
      case "get" =>
        if (resourceObject.getGet == null) {
          Option.empty
        } else {
          Some(HttpMethod.GET, resourceObject.getGet)
        }
      case "post" =>
        if (resourceObject.getPost == null) {
          Option.empty
        } else {
          Some(HttpMethod.POST, resourceObject.getPost)
        }
      case "put" =>
        if (resourceObject.getPut == null) {
          Option.empty
        } else {
          Some(HttpMethod.PUT, resourceObject.getPut)
        }
      case "delete" =>
        if (resourceObject.getDelete == null) {
          Option.empty
        } else {
          Some(HttpMethod.DELETE, resourceObject.getDelete)
        }
    }
    method
  }

}
