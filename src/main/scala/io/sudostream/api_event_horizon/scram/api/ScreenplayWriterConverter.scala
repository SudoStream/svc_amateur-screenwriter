package io.sudostream.api_event_horizon.scram.api

trait ScreenplayWriterConverter
{
  /**
    * Takes a string version of an API definition ( for example a Swagger json API definition )
    * and converts it into the Scram format for testing use.
    *
    * @param inputApiDefinition String version of an API specification
    * @return
    */
  def convertToScreenplayWriterAmateur(inputApiDefinition: String) : Option[ScreenplayWriterAmateur]
}
