Feature: Login and Verify Claim details

Background: : Login Functionality
  Given User is on login page
  When User submit email and password
  Then User should be able to login sucessfully and new page open

  Scenario: Click on Claim and Verify each page is landed
    Given User is on my Claim screen
    When User clicks on ClaimNumber
    Then User should be navigated to My Claims Overview screen