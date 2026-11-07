Feature: B2B Form7 Submission

@Token @API
  Scenario: Generate the Token
    Given the API Token endpoint is configured with "/b2bf7/Authentication"
    When the token request body has set up
    And a Token POST request is sent
    Then the response status code should be 200
    Then Capture the token value from response


  @SheetName_B2BForm7 @API @B2BForm7
  Scenario: Validate B2B Form7 Claim Submission
    Given the API endpoint is configured with "/b2bf7/SubmitForm7"
    And the request body is configured
    When a POST request is sent with XML body
    Then the response status code and confirmation number should be validated

  @SheetName_B2BForm7 @B2BForm7
  Scenario: GWCC Validation for B2B Form7 Claim Submission
    Given User is on GWlogin page
    When User submit username and password
    Then User should be able to login successfully into GWCC
    And Search with firstname and lastname & dateofbirth