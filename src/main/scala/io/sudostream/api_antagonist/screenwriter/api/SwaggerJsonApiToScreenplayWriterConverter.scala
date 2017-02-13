package io.sudostream.api_antagonist.screenwriter.api

import io.swagger.parser.SwaggerParser

class SwaggerJsonApiToScreenplayWriterConverter extends ApiToScreenplayWriterConverter {

  /**
    * Takes a string version of an API definition ( for example a Swagger json API definition )
    * and converts it into the Scram format for testing use.
    *
    * @param swaggerApiAsJsonString String version of an API specification
    * @return
    */
  override def passApiDefinitionToScreenplayWriterAmateur(swaggerApiAsJsonString: String): Option[ScreenplayWriterAmateur] = {
    val apiSwaggerDeserializationResult = new SwaggerParser().readWithInfo(swaggerApiAsJsonString)
    if (apiSwaggerDeserializationResult.getSwagger == null) {
      None
    }
    else {
      Some(new ScreenplayWriterAmateur(apiSwaggerDeserializationResult.getSwagger))
    }
  }

}
