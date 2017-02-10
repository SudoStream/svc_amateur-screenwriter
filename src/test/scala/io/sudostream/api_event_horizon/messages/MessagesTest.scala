package io.sudostream.api_event_horizon.messages

import org.scalatest.FunSuite

class MessagesTest extends FunSuite {

  test("Lets play with avro") {

    val aSingleTestDescription = TestDescription("/stars", HttpMethod.GET, List(200))

    val generatedEvent =
      SpeculativeScreenPlay(
        "apiTitle",
        Some("apiDesc"),
        Some("v1"),
        "hostname.com",
        Some("api"),
        List("http"),
        List(80),
        List(aSingleTestDescription)
      )

    println("Hey Ho ... \n" + generatedEvent)

  }

}