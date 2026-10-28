Feature: Online Service for Business with Claims(OSBC)

  Background: Login to OSBC
    Given User is on login page
    When User submit email and password
    Then User should be able to login successfully to OSBC portal

  @SheetName_eForm7_PI @OSBC @DI7 @eForm7_PI_NoSupportingDocuments
  Scenario: Submit a Report for Physical Injury without Supporting Documents
    Given User is on online services screen
    When User click on Report an injury or illness Form7 GO button
    Then User selects an account and Start Report
    And User should be able to select report type Report Type
    And User should be able to select Injury Category
    And User should be able to select radio PhysicalInjury, Body Part
    And User should be able to Select Reported by
    And User should be able to enter Employer details and get Reference Claim Number
    And User should be able to enter Employee details
    And User should be able to enter Work History details
    And User should be able to enter Injury Illness details
    And User should be able to enter Health care details
    And User should be able to enter Loss Time and Earnings details
    And User should be able to attach Supporting Documents
    And User should be able to review and submit the report
    Then Switch to New tab and Login to ClaimCenter
    And Run the DI batch job in ClaimCenter to process the Auto Registration
    And Verify the Claim is Auto Registered in ClaimCenter

  @SheetName_eForm7_PI @OSBC @DI7 @eForm7_PI_WithSupportingDocuments
  Scenario: Submit a Report for Physical Injury with Supporting Documents
    Given User is on online services screen
    When User click on Report an injury or illness Form7 GO button
    Then User selects an account and Start Report
    And User should be able to select report type Report Type
    And User should be able to select Injury Category
    And User should be able to select radio PhysicalInjury, Body Part
    And User should be able to Select Reported by
    And User should be able to enter Employer details and get Reference Claim Number
    And User should be able to enter Employee details
    And User should be able to enter Work History details
    And User should be able to enter Injury Illness details
    And User should be able to enter Health care details
    And User should be able to enter Loss Time and Earnings details
    And Upload and verify Form 7 supporting documents
    And User should be able to review and submit the report
    Then Switch to New tab and Login to ClaimCenter
    And Run the DI batch job in ClaimCenter to process the Auto Registration
    And Verify the Claim is Auto Registered in ClaimCenter



