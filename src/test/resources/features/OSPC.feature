Feature: Login and Verify Claim details

Background: : Login to OSPC
  Given User is on login page
  When User submit email and password
  Then User should be able to login sucessfully and new page open

  Scenario Outline: Login and Add a Claim
    Given User is on my Claim screen
    When User click on Add a Claim button
    Then User should be able to add claim with "<Claim number>", "<Year of Birth>" and "<PIN>"
    Then Switch to New tab and Login to ClaimCenter
    And Navigate to Portal Authorization screen
    And Remove the "<Claim number>" from profile
    Examples:
      | Claim number | Year of Birth | PIN    |
      | 32649902     |  1950         | 807437 |
#
#
#
#
#  Scenario Outline: Login and Click on Claim and Verify each page is landed
#    Given User is on my Claim screen
#    When User clicks on "<claimnumber>"
#    Then User should be navigated to My Claims Overview screen
#    And User should be navigated to Benefit Payments screen
#    And User should be navigated to Healthcare benefit screen
#    And User should be navigated to Non Health care benefit screen
#    And User should be navigated to Appeals screen
#
#    Examples:
#      | claimnumber |
#      | 32649808    |

#  Scenario Outline: Upload WSIB form Document and Verify Submitted Document Screen
#    Given User is on my Claim screen
#    When User clicks on "<claimnumber>"
#    Then User should be navigated to My Claims Overview screen
#    And User should be navigated to Submit Document screen
#    And User should be able to upload a document with "<filename>"
#    And User should be able to select WSIB form "<wsibform>"
#    And Select confirmation checkbox and Click on Submit button
#    And Verify the Document History is updated with "<filename>"
#    Examples:
#      | claimnumber | wsibform              | filename                                                |
#      | 32649808    | Dental report (0278A) | 0717B_202410_LongTermRateEarningsQuestionnaire_ACES.pdf |

#  Scenario Outline: Send a message from OSPC
#    Given User is on my Claim screen
#    When User clicks on "<claimnumber>"
#    Then User should be navigated to My Claims Overview screen
#    And User clicks on Send us a message link and Lands on Send us a message screen
#    And User selects "<subject>" and enter "<message>"
#    And User clicks on Send button and Lands on Message sent screen
#    And Return to Mail and verify the sent message
#    Then Switch to New tab and Login to ClaimCenter
#    And Search for "<claimnumber>"
#    Then User should be able to see the message displayed from OSPC
#
#    Examples:
#      | claimnumber | subject | message   |
#      | 32649808    | Payments| <dynamic> |
