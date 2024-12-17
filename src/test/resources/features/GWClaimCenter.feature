Feature: Guidewire ClaimCenter

  Background:  Login to GWCC
    Given User is on GWlogin page
    When User submit username and password
    Then User should be able to login successfully into GWCC

Scenario Outline: Verify Document Landed Test
Given User Logged into ClaimCenter
When Search for "<claimnumber>"
And Verify the document is landed in GWCC with "<Author>", "<Description>"
Examples:
| claimnumber | Author      |  Description      |
| 32649808    | OSPC        |  Dental report    |