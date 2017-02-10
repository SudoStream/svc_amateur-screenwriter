package io.sudostream.api_event_horizon.scram.api

import io.swagger.parser.SwaggerParser

class SwaggerJsonScramConverter extends ScramConverter
{

  /**
    * Takes a string version of an API definition ( for example a Swagger json API definition )
    * and converts it into the Scram format for testing use.
    *
    * @param swaggerApiAsJsonString String version of an API specification
    * @return
    */
  override def convertToScram(swaggerApiAsJsonString: String): Option[ScreenplayWriterAmateur] =
  {
    val apiSwaggerDeserialistaionResult = new SwaggerParser().readWithInfo(swaggerApiAsJsonString)
    if (apiSwaggerDeserialistaionResult.getSwagger == null)
    {
      None
    }
    else
    {
     Some(new ScreenplayWriterAmateur(apiSwaggerDeserialistaionResult.getSwagger))
    }
  }

}
