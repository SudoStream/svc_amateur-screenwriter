package io.sudostream.api_antagonist.screenwriter.api.messages

import io.sudostream.api_antagonist.messages.{HttpMethod, HttpQuestionForTheProtagonistAPI, SpeculativeScreenplay}
import org.scalatest.FunSuite

class MessagesTest extends FunSuite {

  test("Lets play with avro") {

    val aSingleHttpQuestion = HttpQuestionForTheProtagonistAPI("/stars", HttpMethod.GET, List(200))

    val generatedEvent =
      SpeculativeScreenplay(
        "apiTitle",
        Some("apiDesc"),
        Some("v1"),
        "hostname.com",
        Some("api"),
        List("http"),
        List(80),
        List(aSingleHttpQuestion)
      )

    println("Hey Ho ... \n" + generatedEvent)

  }

}