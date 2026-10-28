Feature: Online Service for People with Claims(OSPC) API Validations

  @Token
  Scenario: Generate the Token
    Given User is on login page
    When User submit email and password
    Then User should be able to login successfully and retrieve the token


  @API
  Scenario: Validate getWorkerClaimDetail_WSIB response
    Given the API endpoint is configured with "/claim/claim"
    And the request body is:
    """
      {
    "id": "ab066e37-9b54-46bd-9c71-7fc10a91643b_2",
    "jsonrpc": "2.0",
    "method": "getWorkerClaimDetail_WSIB",
    "params": [
        "{{ClaimNumber}}"
    ]
      }
     """
    When a POST request is sent
    Then the response status code should be 200

  @API
  Scenario: Validate getClaimBenefitPayments_WSIB response
    Given the API endpoint is configured with "/claim/claim"
    And the request body is:
    """
      {
    "id": "ab066e37-9b54-46bd-9c71-7fc10a91643b_2",
    "jsonrpc": "2.0",
    "method": "getClaimBenefitPayments_WSIB",
    "params": [
        "{{ClaimNumber}}"
    ]
      }
     """
    When a POST request is sent
    Then the response status code should be 200

  @API
  Scenario: Validate getClaimHealthCareBenefits_WSIB response
    Given the API endpoint is configured with "/claim/claim"
    And the request body is:
    """
      {
    "id": "ab066e37-9b54-46bd-9c71-7fc10a91643b_2",
    "jsonrpc": "2.0",
    "method": "getClaimHealthCareBenefits_WSIB",
    "params": [
        "{{ClaimNumber}}"
    ]
      }
     """
    When a POST request is sent
    Then the response status code should be 200

  @API
  Scenario: Validate getClaimNonHealthCareBenefits_WSIB response
    Given the API endpoint is configured with "/claim/claim"
    And the request body is:
    """
      {
    "id": "ab066e37-9b54-46bd-9c71-7fc10a91643b_2",
    "jsonrpc": "2.0",
    "method": "getClaimNonHealthCareBenefits_WSIB",
    "params": [
        "{{ClaimNumber}}"
    ]
      }
     """
    When a POST request is sent
    Then the response status code should be 200

  @API
  Scenario: Validate getClaimAppeals_WSIB response
    Given the API endpoint is configured with "/claim/claim"
    And the request body is:
    """
      {
    "id": "ab066e37-9b54-46bd-9c71-7fc10a91643b_2",
    "jsonrpc": "2.0",
    "method": "getClaimAppeals_WSIB",
    "params": [
        "{{ClaimNumber}}"
    ]
      }
     """
    When a POST request is sent
    Then the response status code should be 200

  @API @getClaimSummaries_WSIB
  Scenario: Validate getClaimSummaries_WSIB response
    Given the API endpoint is configured with "/claim/claim"
    And the request body is:
    """
    {
    "id": "ca34502f-bd3f-4278-b7bb-a9451ab82c27_7",
    "jsonrpc": "2.0",
    "method": "getClaimSummaries_WSIB",
    "params": [
        {
            "claimStates": [
                "open",
                "draft"
            ]
        },
        {
            "offsetStart": null,
            "offsetEnd": null,
            "orderBy": null,
            "orderByDescending": null
        }
    ]
    }
     """
    When a POST request is sent
    Then the response status code should be 200

  @API @SendMessageAPI
  Scenario: Validate getNewMessageCount response
    Given the API endpoint is configured with "/customermessage/customermessage"
    And the request body is:
    """
    {
    "id": "b1385a2c-8477-4580-903c-f7c3c8331aa6_16",
    "jsonrpc": "2.0",
    "method": "getNewMessageCount",
    "params": [
        "{{ClaimNumber}}"
    ]
    }
     """
    When a POST request is sent
    Then the response status code should be 200

  @API @SendMessageAPI
  Scenario: Validate saveMessage response
    Given the API endpoint is configured with "/customermessage/customermessage"
    And the request body is:
    """
    {
    "id": "b1385a2c-8477-4580-903c-f7c3c8331aa6_23",
    "jsonrpc": "2.0",
    "method": "saveMessage",
    "params": [
        {
            "claimNumber": "{{ClaimNumber}}",
            "messageBody": "Test API",
            "messageSubject": "information",
            "messageType": "question",
            "portalUserDisplayName": "Kiran Kangula",
            "messageGroupId": null,
            "portalType": "ospc"
        }
    ]
    }
     """
    When a POST request is sent
    Then the response status code should be 200
    Then Capture the timestamp of the message sent

  @API @SendMessageAPI
  Scenario: Validate getMessagesForCustomer response
    Given the API endpoint is configured with "/customermessage/customermessage"
    And the request body is:
    """
    {
    "id": "b1385a2c-8477-4580-903c-f7c3c8331aa6_16",
    "jsonrpc": "2.0",
    "method": "getMessagesForCustomer",
    "params": [
        "{{ClaimNumber}}"
    ]
    }
     """
    When a POST request is sent
    Then the response status code should be 200
    Then Verify the sent message is displayed in the response



  @API @DocUploadAPI
  Scenario: Validate form data submission
    Given the API endpoint is configured with "/documentupload/api/upload"
    When a POST request is sent with form data
    Then the response status code should be 202
    Then Extract the file id from the response

  @API @DocUploadAPI
  Scenario: Validate confirm upload response
    Given the API endpoint is configured with "/documentupload/api/confirm"
    And the request body is:
    """
    {
    "claimNumber": "{{ClaimNumber}}",
    "fileIds": [
        {
            "fileId": "{{FileID}}",
            "formId": "2048",
            "wsibform": true
        }
    ],
    "userLoginID": "{{userLoginID}}"
    }
     """
    When a POST request is sent
    Then the response status code should be 202
    Then Capture the timestamp of the Document uploaded

  @API @DocUploadAPI
  Scenario: Validate Document history response
    Given the API endpoint is configured with "/documentupload/api/history"
    When a POST request is sent with Claimid as parameters
    Then the response status code should be 200
    Then Verify the uploaded document is displayed in the response

  @API @DirectDepositAPI
  Scenario: Validate getClaimDirectDeposit response
    Given the API endpoint is configured with "/claim/directdeposit"
    And the request body is:
    """
    {"id":"7552e40a-e017-4880-9c2f-e7c7a153330b_2","jsonrpc":"2.0","method":"getClaimDirectDeposit","params":["{{ClaimNumber}}"]}
     """
    When a POST request is sent
    Then the response status code should be 200
    Then Capture the Public id from the response

  @API @DirectDepositAPI
  Scenario: Validate getUserAccountStatus response
    Given the API endpoint is configured with "/claim/directdeposit"
    And the request body is:
    """
    {"id":"7552e40a-e017-4880-9c2f-e7c7a153330b_2","jsonrpc":"2.0","method":"getUserAccountStatus","params":["{{ClaimNumber}}"]}
     """
    When a POST request is sent
    Then the response status code should be 200

  @API @DirectDepositAPI
  Scenario: Validate verifyDob response
    Given the API endpoint is configured with "/claim/directdeposit"
    And the request body is:
    """
    {"id":"7552e40a-e017-4880-9c2f-e7c7a153330b_6","jsonrpc":"2.0","method":"verifyDob","params":["{{ClaimNumber}}","{{Month}}","{{Year}}"]}
     """
    When a POST request is sent
    Then the response status code should be 200

  @API @DirectDepositAPI
  Scenario: Validate getBankInfo response
    Given the API endpoint is configured with "/claim/directdeposit"
    And the request body is:
    """
    {"id":"7552e40a-e017-4880-9c2f-e7c7a153330b_7","jsonrpc":"2.0","method":"getBankInfo","params":["{{InstitutionNumber}}","{{TransitNumber}}"]}
     """
    When a POST request is sent
    Then the response status code should be 200

  @API @DirectDepositAPI
  Scenario: Validate updateDirectDepositBankInfo response
    Given the API endpoint is configured with "/claim/directdeposit"
    And the request body is:
    """
   {"id":"7552e40a-e017-4880-9c2f-e7c7a153330b_10","jsonrpc":"2.0","method":"updateDirectDepositBankInfo",
   "params":[{"bankAccountNumber":"{{BankAccountNumber}}","wsibbankNumber":"{{InstitutionNumber}}","wsibbranchNumber":"{{TransitNumber}}","claimNumber":"{{ClaimNumber}}",
   "publicID":"{{DD_publicID}}"}]}
     """
    When a POST request is sent
    Then the response status code should be 200

 @DI6API @InjuryAPI
  Scenario: Validate createClaim response - Reference Number Generation -  Injury
    Given the API endpoint is configured with "/fnol/fnol"
    And the request body is identified: "Injury"
    When a POST request is sent
    Then the response status code should be 200
    Then Capture the ReferenceNumber from the response
    Then Regenerate the Token for Claim Submission

  @API @DI6API @UploadDocumentDI6
  Scenario: Validate Document is uploaded successfully for DI6
    Given the API endpoint is configured with "/claim/docs"
    When the request body is:
    """
    {
    "id": "2f052d00-cff8-4a05-bf29-1f2c18308e5b_8",
    "jsonrpc": "2.0",
    "method": "generateUploadToken",
    "params": []
    }
     """
    And a POST request is sent
    Then the response status code should be 200
    Then Capture the SessionId from the response

    Given the API endpoint is configured with "/api/docupload/validateAndScan"
    When a POST request is sent with file and Metadata as parameters
    Then the response status code should be 200
    And Capture the UploadFileId from the response
    Given the API endpoint is configured with "/api/docupload/upload"
    When the request body is:
    """
   {
    "docUID": "001",
    "documentType": "fnol",
    "securityType": "unrestricted",
    "status": "final",
    "claimNumber": "{{RefNumber}}",
    "name": "{{Filename}}",
    "mimeType": "application/pdf",
    "sessionID": "{{SessionId}}",
    "uploadFileID": "{{UploadFileId}}",
    "echannelSourceType": "OSPC_DI_Web",
    "docUploadSessionID": "5dbb2c40-515a-4657-8b70-3b07cf0a5b39",
    "formID": "5I"
}
     """
    And a POST request is sent
    Then the response status code should be 200

  @API @DI6API @SubmitInjuryAPI
  Scenario: Submit Injury Claim for DI6 and Verify the Confirmation.
    Given the API endpoint is configured with "/fnol/fnol"
    And the request body for submit claim is configured: "Injury"
    When a POST request is sent
    Then the response status code should be 200

    @DI6API @IllnessAPI
  Scenario: Validate createClaim response - Reference Number Generation - Illness
    Given the API endpoint is configured with "/fnol/fnol"
    And the request body is identified: "Illness"
    When a POST request is sent
    Then the response status code should be 200
    Then Capture the ReferenceNumber from the response
    Then Regenerate the Token for Claim Submission

  @API @DI6API @SubmitIllnessAPI
  Scenario: Submit Illness Claim for DI6 and Verify the Confirmation.
    Given the API endpoint is configured with "/fnol/fnol"
    And the request body for submit claim is configured: "Illness"
    When a POST request is sent
    Then the response status code should be 200

   @DI6API @MentalHealthAPI
  Scenario: Validate createClaim response - Reference Number Generation - Mental Health
    Given the API endpoint is configured with "/fnol/fnol"
    And the request body is identified: "Mental Health"
    When a POST request is sent
    Then the response status code should be 200
    Then Capture the ReferenceNumber from the response
    Then Regenerate the Token for Claim Submission

  @API @DI6API @SubmitMentalHealthAPI
  Scenario: Submit Mental Health Claim for DI6 and Verify the Confirmation.
    Given the API endpoint is configured with "/fnol/fnol"
    And the request body for submit claim is configured: "Mental Health"
    When a POST request is sent
    Then the response status code should be 200

    @DI6API @ConstructionAPI
  Scenario: Validate createClaim response - Reference Number Generation - Construction
    Given the API endpoint is configured with "/fnol/fnol"
    And the request body is identified: "Construction"
    When a POST request is sent
    Then the response status code should be 200
    Then Capture the ReferenceNumber from the response
    #Then Regenerate the Token for Claim Submission

 @NDI6API @SubmitConstructionAPI
  Scenario: Submit Construction Claim for DI6 and Verify the Confirmation.
    Given the API endpoint is configured with "/fnol/fnol"
    And the request body for submit claim is configured: "Construction"
    When a POST request is sent
    Then the response status code should be 200

  @DI6API @OtherIndustryAPI
  Scenario: Validate createClaim response - Reference Number Generation - Other Industry
    Given the API endpoint is configured with "/fnol/fnol"
    And the request body is identified: "Other Industry"
    When a POST request is sent
    Then the response status code should be 200
    Then Capture the ReferenceNumber from the response
    #Then Regenerate the Token for Claim Submission

  @NDI6API @SubmitOtherIndAPI
  Scenario: Submit Other Industry Claim for DI6 and Verify the Confirmation.
    Given the API endpoint is configured with "/fnol/fnol"
    And the request body for submit claim is configured: "Other Industry"
    When a POST request is sent
    Then the response status code should be 200


  @DI6API @deleteClaim
  Scenario: Validate createClaim response - Reference Number Generation For Deleting
    Given the API endpoint is configured with "/fnol/fnol"
    And the request body is identified: "Injury"
    When a POST request is sent
    Then the response status code should be 200
    Then Capture the ReferenceNumber from the response
    Then Regenerate the Token for Claim Submission

  @API @DI6API @deleteClaim
  Scenario: Validate deleteClaim response
    Given the API endpoint is configured with "/fnol/fnol"
    And the request body is:
    """
   {"id":"72254d75-bde5-49d0-b1ea-1cab9c2f95b0_5","jsonrpc":"2.0","method":"deleteClaim","params":["{{RefNumber}}"]}
     """
    When a POST request is sent
    Then the response status code should be 200


