package io.sudostream.api_antagonist.screenwriter.api

import java.io.InputStream

import io.swagger.parser.SwaggerParser
import org.scalatest.FunSuite

class SwaggerApiPlaying extends FunSuite
{
  val swaggerJsonHelloStream: InputStream = getClass.getResourceAsStream("/swagger-starBirth.json")
  val swaggerHelloJson = scala.io.Source.fromInputStream(swaggerJsonHelloStream).getLines() mkString "\n"

  test("Lets play with swagger definition")
  {
    val converter = new SwaggerJsonApiToScreenplayWriterConverter
    val swaggerDefn = new SwaggerParser().readWithInfo(swaggerHelloJson).getSwagger

    import scala.collection.JavaConverters._
    val thePaths = swaggerDefn.getPaths.asScala

    for ((resourceAsString, resourceObject) <- thePaths)
    {
      println("\nResource: " + resourceAsString)
      for ((httpMethod, operation) <- resourceObject.getOperationMap.asScala)
      {
        print("\t" + httpMethod + ", " + operation.getDescription)
      }
    }
  }

}
