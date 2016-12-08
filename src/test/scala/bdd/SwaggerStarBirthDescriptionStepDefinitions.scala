package bdd

import java.io.InputStream

import cucumber.api.scala.{EN, ScalaDsl}
import io.sudostream.api_event_horizon.scram.api.{Scram, ScramConverter, SwaggerJsonScramConverter}
import org.scalatest.ShouldMatchers

class SwaggerStarBirthDescriptionStepDefinitions extends ScalaDsl with EN with ShouldMatchers
{
  var starBirthScram: Option[Scram] = Option.empty

  val scramConverter: ScramConverter = new SwaggerJsonScramConverter
  val swaggerJsonHelloStream: InputStream = getClass.getResourceAsStream("/swagger-starBirth.json")
  val swaggerHelloJson = scala.io.Source.fromInputStream(swaggerJsonHelloStream).getLines() mkString "\n"

  Given("""^A simple star birth Swagger JSON API Specification""")
  { () =>
    println("The Swagger file is swagger-starBirth.json\n\n")
  }

  When("""^I ask for the short API description$""")
  { () =>
    starBirthScram = scramConverter.convertToScram(swaggerHelloJson)
  }
  Then("""^I should see a description from the initial file""")
  { () =>
    if (starBirthScram.isDefined)
    {
      assert(starBirthScram.get.shortApiDescription == "This API allows you to create stars",
        "\n\nShort API Description should be equal to:-\n!" + "This API allows you to create stars" +
          "!\n\n\t... But sadly looked like :-\n!" +
          starBirthScram.get.shortApiDescription + "!\n"
      )
    } else
    {
      assert(false)
    }
  }

}


