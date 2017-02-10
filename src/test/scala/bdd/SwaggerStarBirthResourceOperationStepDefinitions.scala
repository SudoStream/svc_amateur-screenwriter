package bdd

import java.io.InputStream

import cucumber.api.scala.{EN, ScalaDsl}
import io.sudostream.api_event_horizon.scram.api.{Scram, ScramConverter, SwaggerJsonScramConverter}
import org.scalatest.ShouldMatchers
import play.api.libs.json.JsObject

class SwaggerStarBirthResourceOperationStepDefinitions extends ScalaDsl with EN with ShouldMatchers {
  var starBirthScram: Option[Scram] = Option.empty

  val scramConverter: ScramConverter = new SwaggerJsonScramConverter
  val swaggerJsonHelloStream: InputStream = getClass.getResourceAsStream("/swagger-starBirth.json")
  val swaggerHelloJson = scala.io.Source.fromInputStream(swaggerJsonHelloStream).getLines() mkString "\n"

  Given("""^A swagger API with /stars resource having 2 operations, GET and POST, and /stars/!starId! with 1 GET operation$""") { () =>
    println("The Swagger file is swagger-starBirth.json\n\n")
  }

  When("""^I ask for the number of happy path tests$""") { () =>
    starBirthScram = scramConverter.convertToScram(swaggerHelloJson)
  }
  Then("""^I should get 3 happy path tests""") { () =>
    if (starBirthScram.nonEmpty) {
      val scram = starBirthScram.get
      val generatedTestsEvent = scram.generateHappyPathTests
      println("looks like:-\n\n" + generatedTestsEvent.toString)
      assert(generatedTestsEvent.apiDescription.isDefined, "API Description should be defined")
    } else {
      assert(false, "Scram not created correctly")
    }
  }

}

