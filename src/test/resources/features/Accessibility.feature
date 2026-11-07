Feature: WCAG Accessibility Testing

  # ─────────────────────────────────────────────
  # Scans each page of a flow for WCAG violations
  # ─────────────────────────────────────────────
  Background: Login to OSPC
    Given User is on login page
    When User submit email and password
    Then User should be able to login successfully and new page open

  @Accessibility @WCAG_Login
  Scenario: WCAG Accessibility scan on Login Page
    When User runs accessibility scan on "Login Page"
    Then the page should have no critical WCAG violations
    And User logs all WCAG results without failing

  @Accessibility @WCAG_HomePage
  Scenario: WCAG Accessibility scan on Home Page
    Given User is on my Claim screen
    When User runs accessibility scan on "Home Page"
    Then the page should have no critical WCAG violations
    And User logs all WCAG results without failing

  @Accessibility @WCAG_ClaimOverview
  Scenario: WCAG Accessibility scan on Claim Overview Page
    Given User is on my Claim screen
    When User clicks on "<claimnumber>"
    Then User should be navigated to My Claims Overview screen
    When User runs accessibility scan on "Claim Overview Page"
    Then the page should have no critical WCAG violations
    And User logs all WCAG results without failing

  @Accessibility @WCAG_TombstoneEmail
  Scenario: WCAG Accessibility scan on Tombstone Page
    Given User is on my Claim screen
    When User runs accessibility scan on "My Claims Screen"
    Then the page should have no critical WCAG violations
    And User logs all WCAG results without failing
    When User clicks on contact information link on my claim screen
    Then User should be view Personal details, Mailing address and Contact details
    When User runs accessibility scan on "Personal Details"
    Then the page should have no critical WCAG violations
    And User logs all WCAG results without failing
    When User clicks on Edit button to update Mailing address
    When User runs accessibility scan on "Update Mailing Address"
    Then the page should have no critical WCAG violations
    And User logs all WCAG results without failing
    Then User should be able to update the Mailing address details and Save
    When User runs accessibility scan on "Address Save"
    Then the page should have no critical WCAG violations
    And User logs all WCAG results without failing