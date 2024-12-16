Feature: Direct Deposit

  Background: Login to OSPC
    Given User is on login page
    When User submit email and password
    Then User should be able to login sucessfully and new page open

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