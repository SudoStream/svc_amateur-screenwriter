package io.sudostream.api_antagonist.screenwriter.api

import java.io._

import io.sudostream.api_antagonist.messages.{HttpMethod, SpeculativeScreenplay}
import io.swagger.models.Scheme
import io.swagger.parser.SwaggerParser
import org.apache.avro.file.{DataFileReader, DataFileWriter}
import org.apache.avro.io.{DatumReader, DatumWriter, DecoderFactory, EncoderFactory}
import org.apache.avro.specific.{SpecificDatumReader, SpecificDatumWriter}
import org.scalatest.FunSuite

class SwaggerJsonScreenplayWriterAmateurConverterTest
  extends FunSuite {

  val swaggerJsonHelloApi =
    """{
      |    "swagger": "2.0",
      |    "info": {
      |        "title": "Hello World App",
      |        "description": "Says hello to people",
      |        "version": "1.0.0"
      |    },
      |    "host": "hello.com",
      |    "schemes": [
      |        "https"
      |    ],
      |    "basePath": "/v1",
      |    "produces": [
      |        "application/json"
      |    ],
      |    "paths": {
      |        "/hello": {
      |            "get": {
      |                "summary": "Say Hello",
      |                "description": "Says hello to whoever\n",
      |                "parameters": [
      |                    {
      |                        "name": "nameOfPerson",
      |                        "in": "query",
      |                        "description": "Name of the person to say hello to",
      |                        "required": false,
      |                        "type": "string"
      |                    }
      |                ],
      |                "tags": [
      |                    "sayingHi"
      |                ],
      |                "responses": {
      |                    "200": {
      |                        "description": "A message saying hello to the person",
      |                        "schema": {
      |                            "type": "string"
      |                        }
      |                    }
      |                }
      |            }
      |        }
      |    }
      |}
    """.stripMargin

  test("Simple Hello World Json Spec is parsed correctly by Swagger Parser") {
    val swaggerDeserialized = new SwaggerParser().readWithInfo(swaggerJsonHelloApi)
    assert(swaggerDeserialized.getSwagger !== null)
  }

  test("Simple Hello World Json Spec parsed to give expected meta values") {
    val converter = new SwaggerJsonApiToScreenplayWriterConverter
    val swaggerDeserialized = new SwaggerParser().readWithInfo(swaggerJsonHelloApi)
    val swaggerDefn = swaggerDeserialized.getSwagger
    assert(swaggerDefn.getBasePath === "/v1")
    assert(swaggerDefn.getHost === "hello.com")
    assert(swaggerDefn.getInfo.getDescription === "Says hello to people")
    assert(swaggerDefn.getSchemes.contains(Scheme.HTTPS))
  }

  val expectedShortMetaDescription =
    """
      |API Title : Hello World App
      |Target Host : hello.com
      |Target Port : 443
      |Schemes : https
      | """.stripMargin

  test("Ensure that when the expected number of happy path tests are generated") {
    val speculativeScreenplay: SpeculativeScreenplay = generateTestsFromSwagger
    assert(speculativeScreenplay.theAntagonistInterrogation.size === 4)
  }

  test("Ensure the top level GET /stars test has expected detail") {
    val speculativeScreenplay: SpeculativeScreenplay = generateTestsFromSwagger
    val foundTest = speculativeScreenplay.theAntagonistInterrogation.filter(
      test => HttpMethod.GET.equals(test.method) && test.uriPath == "/stars")

    println(speculativeScreenplay)
    assert(foundTest.size === 1)
  }

  def generateTestsFromSwagger: SpeculativeScreenplay = {
    val swaggerJsonHelloStream: InputStream = getClass.getResourceAsStream("/swagger-starBirth.json")
    val swaggerHelloJson = scala.io.Source.fromInputStream(swaggerJsonHelloStream).getLines() mkString "\n"
    val starBirthScram = new SwaggerJsonApiToScreenplayWriterConverter().passApiDefinitionToScreenplayWriterAmateur(swaggerHelloJson).get
    val happyTests = starBirthScram.generateHappyPathTests
    happyTests
  }

  test("Lets serialise and deserialise with Avro using files as intermediate") {
    val happyTests: SpeculativeScreenplay = generateTestsFromSwagger

    // serialize
    val speculativeScreenplayDatumWriter: DatumWriter[SpeculativeScreenplay] = new SpecificDatumWriter[SpeculativeScreenplay](happyTests.getSchema)
    val dataFileWriter: DataFileWriter[SpeculativeScreenplay] = new DataFileWriter[SpeculativeScreenplay](speculativeScreenplayDatumWriter)
    dataFileWriter.create(happyTests.getSchema(), new File("happy.avro"))
    dataFileWriter.append(happyTests)
    dataFileWriter.close()

    // Deserialize  from disk
    val speculativeScreenplayDatumReader: DatumReader[SpeculativeScreenplay] = new SpecificDatumReader[SpeculativeScreenplay](happyTests.getSchema)
    val dataFileReader: DataFileReader[SpeculativeScreenplay] =
      new DataFileReader[SpeculativeScreenplay](new File("happy.avro"), speculativeScreenplayDatumReader)

    var testScript: SpeculativeScreenplay = null
    while (dataFileReader.hasNext) {
      // Reuse user object by passing it to next(). This saves us from  allocating and
      // garbage collecting many objects for files with many items.
      testScript = dataFileReader.next(testScript)
      println("okay: " + testScript + "; dokies")
    }
  }


  test("Lets serialise and deserialise with Avro using in memory") {
    val happyTests: SpeculativeScreenplay = generateTestsFromSwagger

    // serialize
    val writer: DatumWriter[SpeculativeScreenplay] = new SpecificDatumWriter[SpeculativeScreenplay](happyTests.getSchema)
    val out = new ByteArrayOutputStream()
    val encoder = new EncoderFactory().binaryEncoder(out, null)
    writer.write(happyTests, encoder)
    encoder.flush()
    var someBytes = new Array[Byte](1024)
    println("So encoded is : ###" + out + "###")

    // Deserialize  from disk
    val reader = new SpecificDatumReader[SpeculativeScreenplay](SpeculativeScreenplay.SCHEMA$)
    val in: InputStream = new ByteArrayInputStream(out.toByteArray)
    val decoder: org.apache.avro.io.Decoder = new DecoderFactory().binaryDecoder(in, null)
    val outVersion = new SpeculativeScreenplay()
    reader.read(outVersion, decoder)
    println("OUT: " + outVersion)


    in.close()
    out.close()
  }

}
