Feature: Online Service for People with Claims(OSPC)

  Background: Login to OSPC
    Given User is on login page
    When User submit email and password
    Then User should be able to login successfully and new page open

  @SheetName_GWCC @OSPC @OSPCUI @OSPCAddClaim
  Scenario: Login and Add a Claim
    Given User is on my Claim screen
    Then Switch to New tab and Login to ClaimCenter
    And Navigate to Portal Authorization screen
    And Remove all the claims from profile
    And Switch back to OSPC and verify if the "<ClaimNumber>" is removed
    When User click on Add a Claim button
    Then User should be able to add claim with "<ClaimNumber>", "<DOB>"


  @SheetName_GWCC @OSPC @OSPCUI
  Scenario: Login and Click on Claim and Verify each page is landed
    Given User is on my Claim screen
    When User clicks on "<claimnumber>"
    Then User should be navigated to My Claims Overview screen
    And User should be navigated to Benefit Payments screen
    And User should be navigated to Healthcare benefit screen
    And User should be navigated to Non Health care benefit screen
    And User should be navigated to Appeals screen

  @SheetName_GWCC @OSPC @Appeals
  Scenario: Create Appeals in GWCC and Verify it is reflected in OSPC
    Given User is on my Claim screen
    When User clicks on "<claimnumber>"
    Then User should be navigated to My Claims Overview screen
    And User should be navigated to Appeals screen and count number of appeals
    Then Switch to New tab and Login to ClaimCenter
    And Search for "<claimnumber>"
    And Verify the Appeal is created in ClaimCenter
    And Switch back to OSPC and verify appeal registered is displayed

  @SheetName_GWCC @OSPC @SendMessage @OSPCUI
  Scenario Outline: Send a message from OSPC
    Given User is on my Claim screen
    When User clicks on "<claimnumber>"
    Then User should be navigated to My Claims Overview screen
    And User clicks on Send us a message link and Lands on Send us a message screen
    And User selects "<subject>" and enter "<message>"
    And User clicks on Send button and Lands on Message sent screen
    And Return to Mail and verify the sent message
    Then Switch to New tab and Login to ClaimCenter
    And Search for "<claimnumber>"
    Then User should be able to see the message displayed from OSPC

    Examples:
    | subject | message   |
    | Payments| <dynamic> |

  @SheetName_GWCC @WSIBFormSingle @OSPCDocUpload
  Scenario: Upload a Single WSIB form Document and Verify history and the document is landed in GWCC
    Given User is on my Claim screen
    When User clicks on "<claimnumber>"
    Then User should be navigated to My Claims Overview screen
    And User should be navigated to Submit Document screen
    And User should be able to upload a document with "<filename>"
    And User should be able to select WSIB form "<wsibform>"
    And Select confirmation checkbox and Click on Submit button
    And Verify the Document History is updated with "WSIB"
    Then Switch to New tab and Login to ClaimCenter
    And Search for "<claimnumber>"
    And Verify the document is landed in GWCC with valid Author and Description

  @SheetName_GWCC @WSIBFormMulti @OSPCDocUpload
  Scenario: Upload Multiple WSIB form Documents and Verify history and the documents are landed in GWCC
    Given User is on my Claim screen
    When User clicks on "<claimnumber>"
    Then User should be navigated to My Claims Overview screen
    And User should be navigated to Submit Document screen
    And User should be able to upload documents with "<filenames>"
    And User should be able to select WSIB forms "<wsibforms>"
    And Select confirmation checkbox and Click on Submit button
    And Verify the Document History are updated with "WSIB"
    Then Switch to New tab and Login to ClaimCenter
    And Search for "<claimnumber>"
    And Verify the document are landed in GWCC with valid Author and Description

  @SheetName_GWCC @NonWSIBSingle @OSPCDocUpload
  Scenario: Upload a Single Non WSIB form Document and Verify history and the document is landed in GWCC
    Given User is on my Claim screen
    When User clicks on "<claimnumber>"
    Then User should be navigated to My Claims Overview screen
    And User should be navigated to Submit Document screen
    And User should be able to upload a document with "<filename>"
    And User should be able to select Non WSIB form "<nonwsibformType>" and "<nonwsibforms>"
    And Select confirmation checkbox and Click on Submit button
    And Verify the Document History is updated with "NonWSIB"
    Then Switch to New tab and Login to ClaimCenter
    And Search for "<claimnumber>"
    And Verify the document is landed in GWCC with valid Author and Description

  @SheetName_GWCC @NonWSIBMultiple @OSPCDocUpload
  Scenario: Upload Multiple Non WSIB form Documents and Verify history and the documents are landed in GWCC
    Given User is on my Claim screen
    When User clicks on "<claimnumber>"
    Then User should be navigated to My Claims Overview screen
    And User should be navigated to Submit Document screen
    And User should be able to upload documents with "<filenames>"
    And User should be able to select Non WSIB forms "<nonwsibformType>" and "<nonwsibforms>"
    And Select confirmation checkbox and Click on Submit button
    And Verify the Document History are updated with "NonWSIB"
    Then Switch to New tab and Login to ClaimCenter
    And Search for "<claimnumber>"
    And Verify the document are landed in GWCC with valid Author and Description


  @SheetName_GWCC @DirectDeposit  @PrerequisiteTest
  Scenario: Add Direct Deposit Information in OSPC
    Given User is on my Claim screen
    When User clicks on Direct Deposit
    Then Verify your identity to set up and manage direct deposit
    And Enter banking information and Confirm details
    And Switch to New tab and Login to ClaimCenter
    And Search for Claim number
    And Verify the Direct Deposit is displayed in ClaimCenter
    And Switch to New tab and Login to Contact Manager
    And Search for the Contact using "<FirstName>" and "<LastName>"
    And Verify the Direct Deposit is displayed in ContactManager

  @SheetName_GWCC @DirectDeposit @DependentTest @DDSanity
  Scenario: Edit Direct Deposit Information in OSPC
    Given User is on my Claim screen
    When User clicks on Direct Deposit
    Then Verify your identity to set up and manage direct deposit
    And Enter banking information and Confirm details
    And Switch to New tab and Login to ClaimCenter
    And Search for Claim number
    And Verify the Direct Deposit is displayed in ClaimCenter
    And Switch to New tab and Login to Contact Manager
    And Search for the Contact using "<FirstName>" and "<LastName>"
    And Verify the Direct Deposit is displayed in ContactManager
    And Remove the Direct Deposit from the Contact



  @SheetName_eForm6_PI @eForm6_PI @eForm6 @eForm6_PI_NoSupportingDocuments
  Scenario: Submit a physical-injury report without supporting documents
    Given User is on my Claim screen
    When User click on Submit Report button
    Then User should be able to provide consent to the privacy statement
    And User should be able to select report type "<ReportType>"
    And User should be able to select Injury Type "<InjuryType>"
    And User should be able to select radio "<PhysicalInjury>", Body Part "<BodyPart>"
    And User should be able to enter "<ExistingClaimNumber>" if applicable
    And User should be able to enter "<FirstName>", "<LastName>", "<PhoneNumber>", "<Pronouns>"
    And Enter Additional personal details
    And Enter Worker Address Details
    And Enter Employers Details
    Then Verify the reference number is generated and displayed
    And Enter the Job Details of the Worker
    And Enter Tell us about your work details
    And Enter the Physical injury or illness details
    And Enter Injury date and time
    And Enter Areas of Injury
    And Enter Date and Time Injury Reported to Employer
    And Enter Witnesses contact information
    And Enter Injury, illness or exposure incident address
    And Enter Similar or related previous injury or illness details
    And Enter Health care for injury or illness details
    And Enter Missed time from work details
    And Enter Modified Work Details
    And Enter Return to work details
    And Enter Additional earnings and benefits information
    And Enter Authorized representative details
    And Enter Submit supporting documents details
    And Enter Additional Comments details
    And Verify Review and submit report and Select Declaration checkbox
    And Select decision to Add claim to your account and Submit Report
    And Switch to New tab and Login to ClaimCenter
    And Search for Reference Number
    And Verify intake and Worker Information and Validate worker Address
    And User searches for Account with bussiness activity
    And User enters Accident Details
    And User Enters Body part details
    And Select Third Party accident
    And Select Accident Location from the dropdown
    And Select Health Care Received
    And Select Claim Type
    And Click on Register Claim button and verify that Claim has been registered Successfully
    And Switch back to OSPC and verify if the "<ClaimNumber>" is added
    Then Switch to New tab and Login to ClaimCenter
    And Navigate to Portal Authorization screen
    And Remove the "<ClaimNumber>" from profile
    And Switch back to OSPC and verify if the "<ClaimNumber>" is removed

  @SheetName_eForm6_PI @eForm6_PI @eForm6 @eForm6_PI_WithSupportingDocuments
  Scenario: Submit a physical-injury report with multiple mixed-format supporting documents
    Given User is on my Claim screen
    When User click on Submit Report button
    Then User should be able to provide consent to the privacy statement
    And User should be able to select report type "<ReportType>"
    And User should be able to select Injury Type "<InjuryType>"
    And User should be able to select radio "<PhysicalInjury>", Body Part "<BodyPart>"
    And User should be able to enter "<ExistingClaimNumber>" if applicable
    And User should be able to enter "<FirstName>", "<LastName>", "<PhoneNumber>", "<Pronouns>"
    And Enter Additional personal details
    And Enter Worker Address Details
    And Enter Employers Details
    Then Verify the reference number is generated and displayed
    And Enter the Job Details of the Worker
    And Enter Tell us about your work details
    And Enter the Physical injury or illness details
    And Enter Injury date and time
    And Enter Areas of Injury
    And Enter Date and Time Injury Reported to Employer
    And Enter Witnesses contact information
    And Enter Injury, illness or exposure incident address
    And Enter Similar or related previous injury or illness details
    And Enter Health care for injury or illness details
    And Enter Missed time from work details
    And Enter Modified Work Details
    And Enter Return to work details
    And Enter Additional earnings and benefits information
    And Enter Authorized representative details
    And Upload and verify Form 6 supporting documents
    And Enter Additional Comments details
    And Verify Review and submit report and Select Declaration checkbox
    And Select decision to Add claim to your account and Submit Report
    And Switch to New tab and Login to ClaimCenter
    And Search for Reference Number
    And Verify intake and Worker Information and Validate worker Address
    And User searches for Account with bussiness activity
    And User enters Accident Details
    And User Enters Body part details
    And Select Third Party accident
    And Select Accident Location from the dropdown
    And Select Health Care Received
    And Select Claim Type
    And Click on Register Claim button and verify that Claim has been registered Successfully
    And Switch back to OSPC and verify if the "<ClaimNumber>" is added
    Then Switch to New tab and Login to ClaimCenter
    And Navigate to Portal Authorization screen
    And Remove the "<ClaimNumber>" from profile
    And Switch back to OSPC and verify if the "<ClaimNumber>" is removed



  @SheetName_eForm6_Illness @eForm6_illness @eForm6 @eForm6_illness_WithoutSupportingDocuments
  Scenario: Submit a Report for Illness, disease or medical condition without Supporting documents
    Given User is on my Claim screen
    When User click on Submit Report button
    Then User should be able to provide consent to the privacy statement
    And User should be able to select report type "<ReportType>"
    And User should be able to select Injury Type "<InjuryType>"
    And User should be able to enter "<ExistingClaimNumber>" if applicable
    And User should be able to enter "<FirstName>", "<LastName>", "<PhoneNumber>", "<Pronouns>"
    And Enter Additional personal details
    And Enter Worker Address Details
    And Enter Employers Details
    Then Verify the reference number is generated and displayed
    And Enter the Job Details of the Worker
    And Enter Tell us about your work details
    And Enter the injury or illness details
    And Enter Illness or medical condition details
    And Enter Witnesses contact information
    And Enter Injury, illness or exposure incident address
    And Enter Similar or related previous injury or illness details
    And Enter Health care for injury or illness details
    And Enter Missed time from work details
    And Enter Modified Work Details
    And Enter Return to work details
    And Enter Additional earnings and benefits information
    And Enter Authorized representative details
    And Enter Submit supporting documents details
    And Enter Additional Comments details
    And Verify Review and submit report and Select Declaration checkbox
    And Select decision to Add claim to your account and Submit Report
    And Switch to New tab and Login to ClaimCenter
    And Search for Reference Number
    And Verify intake and Worker Information and Validate worker Address
    And User searches for Account with bussiness activity
    And User enters Accident Details
    And User Enters Body part details
    And Select Third Party accident
    And Select Accident Location from the dropdown
    And Select Health Care Received
    And Select Claim Type
    And Click on Register Claim button and verify that Claim has been registered Successfully
    And Switch back to OSPC and verify if the "<ClaimNumber>" is added
    Then Switch to New tab and Login to ClaimCenter
    And Navigate to Portal Authorization screen
    And Remove the "<ClaimNumber>" from profile
    And Switch back to OSPC and verify if the "<ClaimNumber>" is removed

  @SheetName_eForm6_Illness @eForm6_illness @eForm6 @eForm6_illness_WithSupportingDocuments
  Scenario: Submit a Report for Illness, disease or medical condition with Supporting documents
    Given User is on my Claim screen
    When User click on Submit Report button
    Then User should be able to provide consent to the privacy statement
    And User should be able to select report type "<ReportType>"
    And User should be able to select Injury Type "<InjuryType>"
    And User should be able to enter "<ExistingClaimNumber>" if applicable
    And User should be able to enter "<FirstName>", "<LastName>", "<PhoneNumber>", "<Pronouns>"
    And Enter Additional personal details
    And Enter Worker Address Details
    And Enter Employers Details
    Then Verify the reference number is generated and displayed
    And Enter the Job Details of the Worker
    And Enter Tell us about your work details
    And Enter the injury or illness details
    And Enter Illness or medical condition details
    And Enter Witnesses contact information
    And Enter Injury, illness or exposure incident address
    And Enter Similar or related previous injury or illness details
    And Enter Health care for injury or illness details
    And Enter Missed time from work details
    And Enter Modified Work Details
    And Enter Return to work details
    And Enter Additional earnings and benefits information
    And Enter Authorized representative details
    And Upload and verify Form 6 supporting documents
    And Enter Additional Comments details
    And Verify Review and submit report and Select Declaration checkbox
    And Select decision to Add claim to your account and Submit Report
    And Switch to New tab and Login to ClaimCenter
    And Search for Reference Number
    And Verify intake and Worker Information and Validate worker Address
    And User searches for Account with bussiness activity
    And User enters Accident Details
    And User Enters Body part details
    And Select Third Party accident
    And Select Accident Location from the dropdown
    And Select Health Care Received
    And Select Claim Type
    And Click on Register Claim button and verify that Claim has been registered Successfully
    And Switch back to OSPC and verify if the "<ClaimNumber>" is added
    Then Switch to New tab and Login to ClaimCenter
    And Navigate to Portal Authorization screen
    And Remove the "<ClaimNumber>" from profile
    And Switch back to OSPC and verify if the "<ClaimNumber>" is removed


  @SheetName_eForm6_Mental @eForm6_Mental @eForm6 @eForm6_Mental_WithoutSupportingDocuments
  Scenario: Submit a Report for mental health without Supporting documents
    Given User is on my Claim screen
    When User click on Submit Report button
    Then User should be able to provide consent to the privacy statement
    And User should be able to select report type "<ReportType>"
    And User should be able to select Injury Type "<InjuryType>"
    And User should be able to Describe your Injury
    And User should be able to enter "<ExistingClaimNumber>" if applicable
    And User should be able to enter "<FirstName>", "<LastName>", "<PhoneNumber>", "<Pronouns>"
    And Enter Additional personal details
    And Enter Worker Address Details
    And Enter Employers Details
    Then Verify the reference number is generated and displayed
    And Enter the Job Details of the Worker
    And Enter Tell us about your work details
    And Enter the Mental stress psychological injury details
    And Enter Mental stress psychological injury date details
    And Enter Areas of Injury for Mental
    And Enter Witnesses contact information
    And Enter Injury, illness or exposure incident address
    And Enter Similar or related previous injury or illness details
    And Enter Health care for injury or illness details
    And Enter Missed time from work details
    And Enter Modified Work Details
    And Enter Return to work details
    And Enter Additional earnings and benefits information
    And Enter Authorized representative details
    And Enter Submit supporting documents details
    And Enter Additional Comments details
    And Verify Review and submit report and Select Declaration checkbox
    And Select decision to Add claim to your account and Submit Report
    And Switch to New tab and Login to ClaimCenter
    And Search for Reference Number
    And Verify intake and Worker Information and Validate worker Address
    And User searches for Account with bussiness activity
    And User enters Accident Details
    And User Enters Body part details
    And Select Third Party accident
    And Select Accident Location from the dropdown
    And Select Health Care Received
    And Select Claim Type
    And Click on Register Claim button and verify that Claim has been registered Successfully
    And Switch back to OSPC and verify if the "<ClaimNumber>" is added
    Then Switch to New tab and Login to ClaimCenter
    And Navigate to Portal Authorization screen
    And Remove the "<ClaimNumber>" from profile
    And Switch back to OSPC and verify if the "<ClaimNumber>" is removed

  @SheetName_eForm6_Mental @eForm6_Mental @eForm6 @eForm6_Mental_WithSupportingDocuments
  Scenario: Submit a Report for mental health with Supporting Documents
    Given User is on my Claim screen
    When User click on Submit Report button
    Then User should be able to provide consent to the privacy statement
    And User should be able to select report type "<ReportType>"
    And User should be able to select Injury Type "<InjuryType>"
    And User should be able to Describe your Injury
    And User should be able to enter "<ExistingClaimNumber>" if applicable
    And User should be able to enter "<FirstName>", "<LastName>", "<PhoneNumber>", "<Pronouns>"
    And Enter Additional personal details
    And Enter Worker Address Details
    And Enter Employers Details
    Then Verify the reference number is generated and displayed
    And Enter the Job Details of the Worker
    And Enter Tell us about your work details
    And Enter the Mental stress psychological injury details
    And Enter Mental stress psychological injury date details
    And Enter Areas of Injury for Mental
    And Enter Witnesses contact information
    And Enter Injury, illness or exposure incident address
    And Enter Similar or related previous injury or illness details
    And Enter Health care for injury or illness details
    And Enter Missed time from work details
    And Enter Modified Work Details
    And Enter Return to work details
    And Enter Additional earnings and benefits information
    And Enter Authorized representative details
    And Upload and verify Form 6 supporting documents
    And Enter Additional Comments details
    And Verify Review and submit report and Select Declaration checkbox
    And Select decision to Add claim to your account and Submit Report
    And Switch to New tab and Login to ClaimCenter
    And Search for Reference Number
    And Verify intake and Worker Information and Validate worker Address
    And User searches for Account with bussiness activity
    And User enters Accident Details
    And User Enters Body part details
    And Select Third Party accident
    And Select Accident Location from the dropdown
    And Select Health Care Received
    And Select Claim Type
    And Click on Register Claim button and verify that Claim has been registered Successfully
    And Switch back to OSPC and verify if the "<ClaimNumber>" is added
    Then Switch to New tab and Login to ClaimCenter
    And Navigate to Portal Authorization screen
    And Remove the "<ClaimNumber>" from profile
    And Switch back to OSPC and verify if the "<ClaimNumber>" is removed

  @eForm6_NIHL @eForm6
  Scenario: Login and Submit a Report for NIHL
    Given User is on my Claim screen
    When User click on Submit Report button
    Then User should be able to provide consent to the privacy statement
    And User should be able to select "Injury or illness" report type
    And User should be able to select "Noise-induced hearing loss" Injury Type
    And User should be able to land on Continue to different online service to make a claim for noise-induced hearing loss screen

  @SheetName_eForm6_Construction @eForm6_Construction @eForm6 @eForm6_Construction_WithoutSupportingDocuments
  Scenario: Submit a Report for Exposure Incident - Construction without Supporting Documents
    Given User is on my Claim screen
    When User click on Submit Report button
    Then User should be able to provide consent to the privacy statement
    And User should be able to select report type "<ReportType>"
    And User selects Submission Requirement
    And User should be able to select Injury Type "<InjuryType>"
    And User should be able to enter "<FirstName>", "<LastName>", "<PhoneNumber>", "<Pronouns>"
    And Enter Additional personal details - Exposure
    And Enter Worker Address Details
    And Enter Employers Details
    Then Verify the reference number is generated and displayed
    And Enter the Job Details of the Worker - Exposure Incident
    And Enter Joint Health and Safety Committee details
    And Enter Tell us about your work details
    And Enter the Exposure Incident details
    And Enter Injury, illness or exposure incident address
    And Enter Submit supporting documents details
    And Enter Additional Comments details
    And Verify Review and submit report and Select Declaration checkbox and Submit Exposure
    And Switch to New tab and Login to ClaimCenter
    And Search for Reference Number
    And Verify intake and Worker Information and Validate worker Address
    And User searches for Account with bussiness activity
    And User enters Accident Details
    And User Enters Body part details
    And Select Third Party accident
    And Select Accident Location from the dropdown
    And Select Health Care Received
    And Select Claim Type
    And Click on Register Claim button and verify that Claim has been registered Successfully

  @SheetName_eForm6_Construction @eForm6_Construction @eForm6 @eForm6_Construction_WithSupportingDocuments
  Scenario: Submit a Report for Exposure Incident - Construction with Supporting Documents
    Given User is on my Claim screen
    When User click on Submit Report button
    Then User should be able to provide consent to the privacy statement
    And User should be able to select report type "<ReportType>"
    And User selects Submission Requirement
    And User should be able to select Injury Type "<InjuryType>"
    And User should be able to enter "<FirstName>", "<LastName>", "<PhoneNumber>", "<Pronouns>"
    And Enter Additional personal details - Exposure
    And Enter Worker Address Details
    And Enter Employers Details
    Then Verify the reference number is generated and displayed
    And Enter the Job Details of the Worker - Exposure Incident
    And Enter Joint Health and Safety Committee details
    And Enter Tell us about your work details
    And Enter the Exposure Incident details
    And Enter Injury, illness or exposure incident address
    And Upload and verify Form 6 supporting documents
    And Enter Additional Comments details
    And Verify Review and submit report and Select Declaration checkbox and Submit Exposure
    And Switch to New tab and Login to ClaimCenter
    And Search for Reference Number
    And Verify intake and Worker Information and Validate worker Address
    And User searches for Account with bussiness activity
    And User enters Accident Details
    And User Enters Body part details
    And Select Third Party accident
    And Select Accident Location from the dropdown
    And Select Health Care Received
    And Select Claim Type
    And Click on Register Claim button and verify that Claim has been registered Successfully

  @SheetName_eForm6_Other @eForm6_Other @eForm6 @eForm6_Other_WithoutSupportingDocuments
  Scenario: Submit a Report for Exposure Incident - Other without Supporting documents
    Given User is on my Claim screen
    When User click on Submit Report button
    Then User should be able to provide consent to the privacy statement
    And User should be able to select report type "<ReportType>"
    And User selects Submission Requirement
    And User should be able to select Injury Type "<InjuryType>"
    And User should be able to enter "<FirstName>", "<LastName>", "<PhoneNumber>", "<Pronouns>"
    And Enter Additional personal details - Exposure
    And Enter Worker Address Details
    And Enter Employers Details
    Then Verify the reference number is generated and displayed
    And Enter the Job Details of the Worker - Exposure Incident
    And Enter Tell us about your work details
    And Enter the Exposure Incident details
    And Enter Injury, illness or exposure incident address
    And Enter Submit supporting documents details
    And Enter Additional Comments details
    And Verify Review and submit report and Select Declaration checkbox and Submit Exposure
    And Switch to New tab and Login to ClaimCenter
    And Search for Reference Number
    And Verify intake and Worker Information and Validate worker Address
    And User searches for Account with bussiness activity
    And User enters Accident Details
    And User Enters Body part details
    And Select Third Party accident
    And Select Accident Location from the dropdown
    And Select Health Care Received
    And Select Claim Type
    And Click on Register Claim button and verify that Claim has been registered Successfully

  @SheetName_eForm6_Other @eForm6_Other @eForm6 @eForm6_Other_WithSupportingDocuments
  Scenario: Submit a Report for Exposure Incident - Other with Supporting documents
    Given User is on my Claim screen
    When User click on Submit Report button
    Then User should be able to provide consent to the privacy statement
    And User should be able to select report type "<ReportType>"
    And User selects Submission Requirement
    And User should be able to select Injury Type "<InjuryType>"
    And User should be able to enter "<FirstName>", "<LastName>", "<PhoneNumber>", "<Pronouns>"
    And Enter Additional personal details - Exposure
    And Enter Worker Address Details
    And Enter Employers Details
    Then Verify the reference number is generated and displayed
    And Enter the Job Details of the Worker - Exposure Incident
    And Enter Tell us about your work details
    And Enter the Exposure Incident details
    And Enter Injury, illness or exposure incident address
    And Upload and verify Form 6 supporting documents
    And Enter Additional Comments details
    And Verify Review and submit report and Select Declaration checkbox and Submit Exposure
    And Switch to New tab and Login to ClaimCenter
    And Search for Reference Number
    And Verify intake and Worker Information and Validate worker Address
    And User searches for Account with bussiness activity
    And User enters Accident Details
    And User Enters Body part details
    And Select Third Party accident
    And Select Accident Location from the dropdown
    And Select Health Care Received
    And Select Claim Type
    And Click on Register Claim button and verify that Claim has been registered Successfully

### Tombstone Related Tests

  @SheetName_GWCC @OSPC @Tombstone @Mailingaddress
  Scenario: View the Tombstone information and Update the Mailing Address
    Given User is on my Claim screen
    When User clicks on contact information link on my claim screen
    Then User should be view Personal details, Mailing address and Contact details
    When User clicks on Edit button to update Mailing address
    Then User should be able to update the Mailing address details and Save
    And Switch to New tab and Login to ClaimCenter
    And Search for "<claimnumber>"
    And Verify the Mailing address is updated in GWCC
    And Verify that history events are generated for Mailing address update in GWCC
    And Switch to New tab and Login to Contact Manager
    And Search for the Contact using "<FirstName>" and "<LastName>"
    And Verify the Mailing address is updated along with history events in Contact Manager

  @SheetName_GWCC @OSPC @Tombstone @Phone
  Scenario: View the Tombstone information and Update the Phone Number
    Given User is on my Claim screen
    When User clicks on contact information link on my claim screen
    Then User should be view Personal details, Mailing address and Contact details
    When User clicks on Edit button to update Phone Number
    Then User should be able to update the Phone Number and Save
    And Switch to New tab and Login to ClaimCenter
    And Search for "<claimnumber>"
    And Verify the Phone Number is updated in GWCC
    And Verify that history events are generated for Phone Number update in GWCC
    And Switch to New tab and Login to Contact Manager
    And Search for the Contact using "<FirstName>" and "<LastName>"
    And Verify the Phone Number is updated along with history events in Contact Manager


    ######### Sanity Test Cases ##############

  @SheetName_GWCC @OSPCSanity
  Scenario: Login and Click on Claim and Verify each page is landed
    Given User is on my Claim screen
    When User clicks on "<claimnumber>"
    Then User should be navigated to My Claims Overview screen
    And User should be navigated to Benefit Payments screen
    And User should be navigated to Healthcare benefit screen
    And User should be navigated to Non Health care benefit screen
    And User should be navigated to Appeals screen

  @SheetName_GWCC @OSPCSanity
  Scenario Outline: Send a message from OSPC
    Given User is on my Claim screen
    When User clicks on "<claimnumber>"
    Then User should be navigated to My Claims Overview screen
    And User clicks on Send us a message link and Lands on Send us a message screen
    And User selects "<subject>" and enter "<message>"
    And User clicks on Send button and Lands on Message sent screen
    And Return to Mail and verify the sent message

    Examples:
      | subject | message   |
      | Payments| <dynamic> |

  @SheetName_GWCC @OSPCSanity
  Scenario: Upload a Single WSIB form Document and Verify history and the document is landed in GWCC
    Given User is on my Claim screen
    When User clicks on "<claimnumber>"
    Then User should be navigated to My Claims Overview screen
    And User should be navigated to Submit Document screen
    And User should be able to upload a document with "<filename>"
    And User should be able to select WSIB form "<wsibform>"
    And Select confirmation checkbox and Click on Submit button
    And Verify the Document History is updated with "<filenames>"

  @SheetName_GWCC @OSPCSanity @DependentTest
  Scenario: Edit Direct Deposit Information in OSPC
    Given User is on my Claim screen
    When User clicks on Direct Deposit
    Then Click Edit direct Deposit information and Select "<Month>" and "<Year>"
    And Enter banking information and Confirm details


  @SheetName_eForm6_PI @OSPCSanity
  Scenario: Login and Submit a Report for Physical Injury
    Given User is on my Claim screen
    When User click on Submit Report button
    Then User should be able to provide consent to the privacy statement
    And User should be able to select report type "<ReportType>"
    And User should be able to select Injury Type "<InjuryType>"
    And User should be able to select radio "<PhysicalInjury>", Body Part "<BodyPart>"
    And User should be able to enter "<ExistingClaimNumber>" if applicable
    And User should be able to enter "<FirstName>", "<LastName>", "<PhoneNumber>", "<Pronouns>"
    And Enter Additional personal details
    And Enter Worker Address Details
    And Enter Employers Details
    Then Verify the reference number is generated and displayed
    And Enter the Job Details of the Worker
    And Enter Tell us about your work details
    And Enter the Physical injury or illness details
    And Enter Injury date and time
    And Enter Areas of Injury
    And Enter Witnesses contact information
    And Enter Injury, illness or exposure incident address
    And Enter Similar or related previous injury or illness details
    And Enter Health care for injury or illness details
    And Enter Missed time from work details
    And Enter Modified Work Details
    And Enter Return to work details
    And Enter Additional earnings and benefits information
    And Enter Authorized representative details
    And Enter Submit supporting documents details
    And Enter Additional Comments details
    And Verify Review and submit report and Select Declaration checkbox
    And Select decision to Add claim to your account and Submit Report