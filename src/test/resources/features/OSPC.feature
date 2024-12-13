Feature: Login and Verify Claim details

Background: : Login to OSPC
  Given User is on login page
  When User submit email and password
  Then User should be able to login sucessfully and new page open

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
#      | 32649417    |
#      | 32649808    |

  Scenario: Upload Document and Verify Submitted Document Screen
    Given User is on my Claim screen
    When User clicks on "32649808"
    Then User should be navigated to My Claims Overview screen
    And User should be navigated to Submit Document screen
    And User should be able to upload a document
    And User should be able to select WSIB form "Benefit for Loss of Retirement Income Designation of Beneficiary Form (2048A)"
    And Select confirmation checkbox and Click on Submit button
    And Verify the Document History is updated

