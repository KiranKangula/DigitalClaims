Feature: Noise Induced Hearing Loss Portal


  @SheetName_NIHL @NIHL
  Scenario Outline: NIHL claim with supporting documents - <UploadCase>
    Given User is on NIHL landing page
    When User clicks on Gradual over a period of a few years radio button
    Then User should be able to enter the Email address and Date of birth and click on Start Claim button
    And Enter data in My Information Screen
    And Enter data in Health care Screen
    And Enter data in Employment Screen
    And Enter data in Supporting Documents Screen with health care files "<HealthCareFiles>" and employment files "<EmploymentFiles>"
    And Verify the data in Review screen and Submit the claim
    And Verify the claim is submitted successfully
    Then Switch to New tab and Login to ClaimCenter
    And Search for the NIHL claimant in GWCC
    And Verify the NIHL submission documents landed in GWCC
      | SupportingDescriptions | <GWCCSupportingDescriptions> |
      | SupportingAuthors      | <GWCCSupportingAuthors>      |
    Examples:
      | UploadCase                     | HealthCareFiles                                     | EmploymentFiles               | GWCCSupportingDescriptions                       | GWCCSupportingAuthors |
      | No documents                   |                                                     |                                |                                                  |                       |
      | One document in each section   | DOB_Letter.docx                                     | DOB_Letter.jpeg                | Audiogram\|CR FROM:                              | TOSPWSIB\svc_kfxppd   |
      | Multiple health care documents | DOB_Letter.docx\|DOB_Letter.jpeg\|DOB_Letter-Copy.jpg | Sample_DocUpload_GDDE.tif    | Audiogram\|Audiogram\|Audiogram\|CR FROM:      | TOSPWSIB\svc_kfxppd   |