Feature: Online Service for People with Claims(OSPC)

Background: : Login to OSPC
  Given User is on login page
  When User submit email and password
  Then User should be able to login successfully and new page open

  @SheetName_GWCC
  Scenario Outline: Login and Add a Claim
    Given User is on my Claim screen
    When User click on Add a Claim button
    Then User should be able to add claim with "<Claim Number>", "<DOB>" and "<PIN>"
    Then Switch to New tab and Login to ClaimCenter
    And Navigate to Portal Authorization screen
    And Remove the "<Claim Number>" from profile
    And Switch back to OSPC and verify if the "<Claim Number>" is removed
    Examples:
      | Claim Number | DOB           | PIN    |
      | 32649902     |  1950         | 807437 |


  Scenario Outline: Login and Click on Claim and Verify each page is landed
    Given User is on my Claim screen
    When User clicks on "<claimnumber>"
    Then User should be navigated to My Claims Overview screen
    And User should be navigated to Benefit Payments screen
    And User should be navigated to Healthcare benefit screen
    And User should be navigated to Non Health care benefit screen
    And User should be navigated to Appeals screen

    Examples:
      | claimnumber |
      | 32649808    |

  Scenario Outline: Send a message from OSPC
    Given User is on my Claim screen
    When User clicks on "<claimnumber>"
    Then User should be navigated to My Claims Overview screen
    And User clicks on Send us a message link and Lands on Send us a message screen
    And User selects "<subject>" and enter "<message>"
    And User clicks on Send button and Lands on Message sent screen
    And Return to Mail and verify the sent message
    Then Switch to New tab and Login to ClaimCenter
    And Search for "<claimnumber>"
    Then User should be able to see the message displayed from OSPC

    Examples:
      | claimnumber | subject | message   |
      | 32649808    | Payments| <dynamic> |

  Scenario Outline: Upload WSIB form Document and Verify Submitted Document Screen
    Given User is on my Claim screen
    When User clicks on "<claimnumber>"
    Then User should be navigated to My Claims Overview screen
    And User should be navigated to Submit Document screen
    And User should be able to upload a document with "<filename>"
    And User should be able to select WSIB form "<wsibform>"
    And Select confirmation checkbox and Click on Submit button
    And Verify the Document History is updated with "<filename>"
    Then Switch to New tab and Login to ClaimCenter
    And Search for "<claimnumber>"
    And Verify the document is landed in GWCC with "<Author>", "<Description>"
    Examples:
      | claimnumber | wsibform              | filename                                                | Author      |  Description      |
      | 32649808    | Dental report (0278A) | 0717B_202410_LongTermRateEarningsQuestionnaire_ACES.pdf | OSPC        |  Dental report    |

  Scenario Outline: Add Direct Deposit Information in OSPC
    Given User is on my Claim screen
    When User clicks on Direct Deposit
    Then Click Add direct Deposit information and Select "<Month>" and "<Year>"
    And Enter "<Transit number>", "<Institution number>", "<Account number>" and Confirm details

    Examples:
      | Month | Year | Transit number | Institution number | Account number |
      |1      | 2000 |  00100         | 010                |  123456789     |

  Scenario Outline: Edit Direct Deposit Information in OSPC
    Given User is on my Claim screen
    When User clicks on Direct Deposit
    Then Click Edit direct Deposit information and Select "<Month>" and "<Year>"
    And Enter "<Transit number>", "<Institution number>", "<Account number>" and Confirm details
    And Switch to New tab and Login to ClaimCenter
    And Search for Claim number
    And Remove the Direct Deposit from the Claim
    And Verify the Direct Deposit is not displayed in OSPC
    Examples:
      | Month | Year | Transit number | Institution number | Account number |
      |1      | 2000 |  01001         | 010                |  987654321     |
