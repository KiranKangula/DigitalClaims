Feature: Clothing Allowance Portal

Background: Launch to ClothingAllowance Portal
  Given User is on landing page

  @SheetName_CAA @CAA
  Scenario: Submit Clothing Allowance application and verify if it is landed in GWCC.
    Given User is on landing page
    When User enters claimant information such as "<claimnumber>", "<lastname>", "<dateofbirth>"
    Then Enter data in Clothing allowance application screen
    And User should be able to review and Submit the application
    Then Switch to New tab and Login to ClaimCenter
    And Search for "<claimnumber>"
    And Verify the document is landed in GWCC with "<Author>", "<Description>"
