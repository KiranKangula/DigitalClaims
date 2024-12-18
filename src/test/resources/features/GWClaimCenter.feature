Feature: Guidewire ClaimCenter

  Background:  Login to GWCC
    Given User is on GWlogin page
    When User submit username and password
    Then User should be able to login successfully into GWCC

Scenario Outline: Create a Claim in GW ClaimCenter
  Given User Logged into ClaimCenter
  When User initiates Manual Registration
  Then User selects Source of Information as "Form 6"
  And User enters new worker information as below:
    | First Name    | <firstname> |
    | Last Name     | <lastname>  |
    | Primary Phone | <phonenumber> |
    | Postal Code   | <postalcode>  |
    | Gender        | <gender>      |
    | Date of Birth | <dateofbirth> |
  And User searches for "<Account#>" with bussiness activity
  And User enters Accident Details as below:
    | Date of Accident  |   <dateofaccident> |
    | Date Reported     |   <datereported>   |
  And User Enters Body part details as below:
    | Area of Body      |   <areaofbody>      |
    | Area of Injury    |   <areaofinjury>    |
    | Accident Type     |   <accidentType>     |
    | Detailed Accident Type    |   <detailedaccidentType>    |
    | Nature of Injury      |   <natureofinjury>      |
  And Select Third Party accident as "No"
  And Select Accident Location from the dropdown
  And Select Health Care Received as "No"
  And Select Claim Type as "Lost Time"
  And Click on Register Claim button and verify that Claim has been registered Successfully
  Examples:
    | firstname | lastname | phonenumber  | postalcode | gender | dateofbirth | Account# | dateofaccident | datereported | areaofbody | areaofinjury | accidentType | detailedaccidentType | natureofinjury |
    | Kiran     | Kangulaa | 647-800-0000 | M4P 1Z2    | Male   | 01/01/2000  | 1169879  | 17/12/2024     | 17/12/2024   | Head       | Head         | Serious Injury | Burn               | Major Burns    |

Scenario Outline: Verify Document Landed Test
    Given User Logged into ClaimCenter
    When Search for "<claimnumber>"
    And Verify the document is landed in GWCC with "<Author>", "<Description>"
    Examples:
              | claimnumber | Author      |  Description      |
              |  32649808    | OSPC        |  Dental report    |