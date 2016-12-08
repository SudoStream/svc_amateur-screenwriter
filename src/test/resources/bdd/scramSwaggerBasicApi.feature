Feature: Swagger Interpretations

  Scenario: A small swagger file should be able to be parsed for its description
    Given A simple star birth Swagger JSON API Specification
    When I ask for the short API description
    Then I should see a description from the initial file

  Scenario: A small but expressive Swagger File modelled in SCRAM API should list resources and operations
    Given A swagger API with /stars resource having 2 operations, GET and POST, and /stars/!starId! with 1 GET operation
    When I ask for the number of happy path tests
    Then I should get 3 happy path tests
