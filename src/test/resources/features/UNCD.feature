Feature: Document Upload Portal

Background: Login to Document Upload Portal
  Given User is on login page
  When User submit email and password
  Then User is on Step 1: Get Started screen

  @UNCD @WSIB
  Scenario Outline: Upload Numbered WSIB form Document and Verify Document the confirmation screen
    Given User is on Step 1: Get Started screen
    When User clicks on Submitting a document for an existing claim
    Then User enters claim information such as "<claimnumber>", "<lastname>", "<dateofbirth>"
    And User should be able to upload a UNCD document with "<filename>"
    And User should be able to select UNCD WSIB form "<wsibform>"
    And Select delaration and Email consent checkbox and Click on Submit button
    And Verify the confirmation screen is displayed
    Then Switch to New tab and Login to ClaimCenter
    And Search for UNCD "<claimnumber>"
    And Verify the EDOB document is landed in GWCC with "<Author>", "<Description>"
    Examples:
      | claimnumber |   lastname   |  dateofbirth  |  wsibform             | filename                                                | Author      |  Description      |
      | 32710725    |  PAULNMVFQ   |  01/01/2000  | Dental report (0278A) | 0717B_202410_LongTermRateEarningsQuestionnaire_ACES.pdf | UNCD        |  Dental report    |

  @UNCD @NonWSIB
  Scenario Outline: Upload Numbered Non-WSIB form Document and Verify Document the confirmation screen
    Given User is on Step 1: Get Started screen
    When User clicks on Submitting a document for an existing claim
    Then User enters claim information such as "<claimnumber>", "<lastname>", "<dateofbirth>"
    And User should be able to upload a UNCD document with "<filename>"
    And User should be able to select UNCD Non WSIB form "<nonwsibformcategory>", "<subform>"
    And Select delaration and Email consent checkbox and Click on Submit button
    And Verify the confirmation screen is displayed
    Then Switch to New tab and Login to ClaimCenter
    And Search for UNCD "<claimnumber>"
    And Verify the EDOB document is landed in GWCC with "<Author>", "<Description>"
    Examples:
      | claimnumber |   lastname   |  dateofbirth  |  nonwsibformcategory | subform                  |  filename                                              | Author      |  Description      |
      | 32710725    |  PAULNMVFQ   |  01/01/2000  | Expense receipt       | General expense receipt  |0717B_202410_LongTermRateEarningsQuestionnaire_ACES.pdf | UNCD        |  GENERAL WORKER EXPENSE FORM    |

  @UNCD @Unnumbered
  Scenario Outline: Upload UnNumbered Document and Verify Document the confirmation screen
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
    Examples:
      | firstname |   lastname   |  dateofbirth  |  dateofinjury|  formcategory                                       |  filename                                             | Author     | Description        |
      | <dynamicFN>    |  <dynamicLN>   |  01/01/2000  |     01/01/2024 | Worker's report of injury/disease (Form 6) (0006A)  |0717B_202410_LongTermRateEarningsQuestionnaire_ACES.pdf | UNCD      |  Worker's Report of Injury|