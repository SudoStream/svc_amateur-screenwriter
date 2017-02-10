package io.sudostream.api_event_horizon.scram.api

import io.swagger.parser.SwaggerParser

class SwaggerJsonScreenplayWriterConverter extends ScreenplayWriterConverter
{

  /**
    * Takes a string version of an API definition ( for example a Swagger json API definition )
    * and converts it into the Scram format for testing use.
    *
    * @param swaggerApiAsJsonString String version of an API specification
    * @return
    */
  override def convertToScreenplayWriterAmateur(swaggerApiAsJsonString: String): Option[ScreenplayWriterAmateur] =
  {
    val apiSwaggerDeserializationResult = new SwaggerParser().readWithInfo(swaggerApiAsJsonString)
    if (apiSwaggerDeserializationResult.getSwagger == null)
    {
      None
    }
    else
    {
     Some(new ScreenplayWriterAmateur(apiSwaggerDeserializationResult.getSwagger))
    }
  }

}
