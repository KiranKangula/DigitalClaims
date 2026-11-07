Feature: Document Upload Portal

Background: Login to Document Upload Portal
  Given User is on login page
  When User submit email and password
  Then User is on Step 1: Get Started screen

  @SheetName_UNCD_WSIB @UNCD @WSIBForm
  Scenario: Upload a Single Numbered WSIB form Document and Verify Document is landed in GWCC
    Given User is on Step 1: Get Started screen
    When User clicks on Submitting a document for an existing claim
    Then User enters claim information such as "<claimnumber>", "<lastname>", "<dateofbirth>"
    And User should be able to upload a UNCD document with "<filename>"
    And User should be able to select UNCD WSIB form "<wsibform>"
    And Select delaration and Email consent checkbox and Click on Submit button
    And Verify the confirmation screen is displayed
    Then Switch to New tab and Login to ClaimCenter
    And Search for UNCD "<claimnumber>"
    And Verify the document is landed in GWCC with valid Author and Description

  @SheetName_UNCD_WSIB @UNCD @WSIBForm
  Scenario: Upload Multiple Numbered WSIB form Document and Verify Document is landed in GWCC
    Given User is on Step 1: Get Started screen
    When User clicks on Submitting a document for an existing claim
    Then User enters claim information such as "<claimnumber>", "<lastname>", "<dateofbirth>"
    And User should be able to upload a UNCD documents with "<filenames>"
    And User should be able to select UNCD WSIB forms "<wsibforms>"
    And Select delaration and Email consent checkbox and Click on Submit button
    And Verify the confirmation screen is displayed
    Then Switch to New tab and Login to ClaimCenter
    And Search for UNCD "<claimnumber>"
    And Verify the document are landed in GWCC with valid Author and Description

  @SheetName_UNCD_NonWSIB @UNCD @NonWSIB
  Scenario: Upload a Single Numbered Non-WSIB form Document and Verify Document is landed in GWCC
    Given User is on Step 1: Get Started screen
    When User clicks on Submitting a document for an existing claim
    Then User enters claim information such as "<claimnumber>", "<lastname>", "<dateofbirth>"
    And User should be able to upload a UNCD document with "<filename>"
    And User should be able to select UNCD Non WSIB form "<nonwsibformcategory>", "<subform>"
    And Select delaration and Email consent checkbox and Click on Submit button
    And Verify the confirmation screen is displayed
    Then Switch to New tab and Login to ClaimCenter
    And Search for UNCD "<claimnumber>"
    And Verify the document is landed in GWCC with valid Author and Description

  @SheetName_UNCD_NonWSIB @UNCD @NonWSIB
  Scenario: Upload Multiple Numbered Non-WSIB form Document and Verify Document is landed in GWCC
    Given User is on Step 1: Get Started screen
    When User clicks on Submitting a document for an existing claim
    Then User enters claim information such as "<claimnumber>", "<lastname>", "<dateofbirth>"
    And User should be able to upload a UNCD documents with "<filenames>"
    And User should be able to select UNCD Non WSIB forms "<nonwsibformcategory>", "<subform>"
    And Select delaration and Email consent checkbox and Click on Submit button
    And Verify the confirmation screen is displayed
    Then Switch to New tab and Login to ClaimCenter
    And Search for UNCD "<claimnumber>"
    And Verify the document are landed in GWCC with valid Author and Description

  @SheetName_UNCD_UnNumbered @UNCD @UNCDUnnumbered
  Scenario: Upload UnNumbered Document and Verify Document the confirmation screen
    Given User is on Step 1: Get Started screen
    When User clicks on Reporting a new injury or exposure
    Then User enters claim information such as "<firstname>", "<lastname>", "<dateofbirth>", "<dateofinjury>"
    And User should be able to upload a UNCD document with "<filename>"
    And User should be able to select UNCD form "<formcategory>"
    And User should be able to select Supporting Documents as "No"
    And Select delaration and Email consent checkbox and Click on Submit button
    And Verify the confirmation screen is displayed
    Then Switch to New tab and Login to ClaimCenter
    And Search with firstname and lastname & dateofbirth "<dateofbirth>"
    And Verify the UNCD document is landed in GWCC with "<Author>", "<Description>"