package io.sudostream.api_antagonist.screenwriter.api

trait ApiToScreenplayWriterConverter
{
  /**
    * Takes a string version of an API definition ( for example a Swagger json API definition )
    * and converts it into the Scram format for testing use.
    *
    * @param inputApiDefinition String version of an API specification
    * @return
    */
  def passApiDefinitionToScreenplayWriterAmateur(inputApiDefinition: String) : Option[ScreenplayWriterAmateur]
}
