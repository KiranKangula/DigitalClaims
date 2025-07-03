Feature: Document upload Numbered and Unnumbered - API Validations

  @API @Token
  Scenario: Generate the Token
    Given the UNCD Token endpoint is configured with "/b2c_1_udu_signinup/oauth2/v2.0/token"
    When the token request body has set up
    And a UNCD Token POST request is sent
    Then the UNCD response status code should be 200
    Then Capture the token value from response

  @API @UNCD_API
  Scenario: Authenticate user for document upload
    Given the UNCD API endpoint is configured with "/documentupload/api/auth"
    When the UNCD request body is:
    """
    {"claimNumber":"{{ClaimNumber}}","lastName":"{{LastName}}","dateOfBirth":"{{dateOfBirth}}"}
     """
    And a UNCD POST request is sent
    Then the UNCD response status code should be 200

  @API @UNCD_API
  Scenario: Upload File for Document Upload
    Given the UNCD API endpoint is configured with "/documentupload/api/edobupload"
    When a POST request is sent with form data for UNCD
    Then the UNCD response status code should be 202
    Then Extract the file id from the response for UNCD


  @API @UNCD_API
  Scenario: Confirmation for document upload
    Given the UNCD API endpoint is configured with "/documentupload/api/edobconfirm"
    When the UNCD request body is:
    """
    {"fileIds":[{"fileId":"{{FileId}}","formId":"2048","wsibform":true,
    "formNameEn":"Benefit for Loss of Retirement Income Designation of Beneficiary Form (2048A)",
    "formNameFr":"Indemnité pour perte de revenu de retraite Désignation de bénéficiaires (2048B)"}],
    "email":"Kiran_Kangula@wsib.on.ca","sendEmailConfirmation":false,"language":"en","dateOfBirth":"{{dateOfBirth}}",
    "userLoginID":"{{userLoginID}}",
    "claimantLastName":"{{LastName}}","claimNumber":"{{ClaimNumber}}"}
     """
    And a UNCD POST request is sent
    Then the UNCD response status code should be 202

  @API @UNCD_API
  Scenario: Upload File for Document Upload for Unnumbered
    Given the UNCD API endpoint is configured with "/documentupload/api/uncdupload"
    When a POST request is sent with form data for UNCD Unnumbered
    Then the UNCD response status code should be 202
    Then Extract the file id from the response for UNCD

  @API @UNCD_API
  Scenario: Confirmation for document upload for Unnumbered
    Given the UNCD API endpoint is configured with "/documentupload/api/uncdconfirm"
    When the UNCD request body is:
    """
    {"fileIds":[{"fileId":"{{FileId}}","formId":"6","wsibform":true,
    "formNameEn":"Worker's report of injury/disease (Form 6) (0006A)",
    "formNameFr":"Avis de lésion ou de maladie (travailleuse ou travailleur) (formulaire 6) (0006B)"}],
    "email":"Kiran_Kangula@wsib.on.ca","sendEmailConfirmation":true,"language":"en","dateOfBirth":"{{dateOfBirth}}",
    "userLoginID":"{{userLoginID}}",
    "claimantLastName":"Test","claimantFirstName":"Test","subFileInfo":[{
			"fileId": "{{FileId}}",
			"formId": "1824",
			"wsibform": true,
			"formNameEn": "Direction of authorization",
			"formNameFr": "Avis d'autorisation"
		}],"dateOfAccident":""}
     """
    And a UNCD POST request is sent
    Then the UNCD response status code should be 202
