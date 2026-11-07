Feature: Guidewire ClaimCenter

  Background:  Login to GWCC
    Given User is on GWlogin page
    When User submit username and password
    Then User should be able to login successfully into GWCC

  @SheetName_GWCC_Claim @ClaimCreation_NewWorker
Scenario: Create a Claim in GW ClaimCenter with New Worker
  Given User Logged into ClaimCenter
  When User initiates Manual Registration
  Then User selects Source of Information
  And User enters new worker information
  And User searches for Account with bussiness activity
  And User enters Accident Details
    And User Enters Body part details
  And Select Third Party accident
  And Select Accident Location from the dropdown
  And Select Health Care Received
  And Select Claim Type
  And Click on Register Claim button and verify that Claim has been registered Successfully

  @SheetName_GWCC_Claim @ClaimCreation_ExistingWorker
  Scenario: Create a Claim in GW ClaimCenter with New Worker
    Given User Logged into ClaimCenter
    When User initiates Manual Registration
    Then User selects Source of Information
    And User searches for Existing Worker
    And User searches for Account with bussiness activity
    And User enters Accident Details
    And User Enters Body part details
    And Select Third Party accident
    And Select Accident Location from the dropdown
    And Select Health Care Received
    And Select Claim Type
    And Click on Register Claim button and verify that Claim has been registered Successfully


  @SheetName_GWCC_Claim @ClaimNW_withOSPCData
  Scenario: Setup data on Claim after Creation with New Worker
    Given I have created a new claim in GW ClaimCenter with New Worker
    Then Setup Appeals Data on the Claim
    Then Setup RTW information on the Claim
    Then Setup Device Details on the Claim
    Then Setup Independent Living Allowance on the Claim
    Then Setup Personal Care Allowance on the Claim
    Then Setup Clothing Allowance on the Claim
    Then Setup Treatment Details on the Claim
    Then Setup Medication Details on the Claim



  @SheetName_GWCC_Claim @ClaimEW_withOSPCData
  Scenario: Setup data on Claim after Creation with Existing Worker
    Given I have created a new claim in GW ClaimCenter with Existing Worker
    Then Setup Appeals Data on the Claim
    Then Setup RTW information on the Claim
    Then Setup Device Details on the Claim
    Then Setup Independent Living Allowance on the Claim
    Then Setup Personal Care Allowance on the Claim
    Then Setup Payments data on the Claim
    Then Setup Clothing Allowance on the Claim
    Then Setup Treatment Details on the Claim
    Then Setup Medication Details on the Claim


  @TestBatchRun
  Scenario: Test Batch Run
    Given User Logged into ClaimCenter
    And Run the DI batch job in ClaimCenter to process the Auto Registration
