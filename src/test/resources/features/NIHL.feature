Feature: Noise Induced Hearing Loss Portal


  @SheetName_NIHL @NIHL
  Scenario:Submit NIHL application and verify if it is landed in GWCC.
  Given User is on NIHL landing page
  When User clicks on Gradual over a period of a few years radio button
  Then User should be able to enter the Email address and Date of birth and click on Start Claim button
  And Enter data in My Information Screen
  And Enter data in Health care Screen
  And Enter data in Employment Screen
  And Enter data in Supporting Documents Screen
  And Verify the data in Review screen and Submit the claim
  And Verify the claim is submitted successfully
    Then Switch to New tab and Login to ClaimCenter
    And Search with firstname and lastname & dateofbirth
    And Verify the document is landed in GWCC