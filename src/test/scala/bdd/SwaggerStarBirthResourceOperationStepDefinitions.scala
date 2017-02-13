package bdd

import java.io.InputStream

import cucumber.api.scala.{EN, ScalaDsl}
import io.sudostream.api_antagonist.screenwriter.api.{ApiToScreenplayWriterConverter, ScreenplayWriterAmateur, SwaggerJsonApiToScreenplayWriterConverter}
import io.sudostream.api_antagonist.screenwriter.api.{ApiToScreenplayWriterConverter, SwaggerJsonApiToScreenplayWriterConverter}
import org.scalatest.ShouldMatchers

class SwaggerStarBirthResourceOperationStepDefinitions extends ScalaDsl with EN with ShouldMatchers {
  var starBirthScram: Option[ScreenplayWriterAmateur] = Option.empty

  val scramConverter: ApiToScreenplayWriterConverter = new SwaggerJsonApiToScreenplayWriterConverter
  val swaggerJsonHelloStream: InputStream = getClass.getResourceAsStream("/swagger-starBirth.json")
  val swaggerHelloJson = scala.io.Source.fromInputStream(swaggerJsonHelloStream).getLines() mkString "\n"

  Given("""^A swagger API with /stars resource having 2 operations, GET and POST, and /stars/!starId! with 1 GET operation$""") { () =>
    println("The Swagger file is swagger-starBirth.json\n\n")
  }

  When("""^I ask for the number of happy path tests$""") { () =>
    starBirthScram = scramConverter.passApiDefinitionToScreenplayWriterAmateur(swaggerHelloJson)
  }
  Then("""^I should get 3 happy path tests""") { () =>
    if (starBirthScram.nonEmpty) {
      val scram = starBirthScram.get
      val speculativeScreenplay = scram.generateHappyPathTests
      println("looks like:-\n\n" + speculativeScreenplay.toString)
      assert(speculativeScreenplay.apiDescription.isDefined, "API Description should be defined")
    } else {
      assert(false, "Scram not created correctly")
    }
  }

}


