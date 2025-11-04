Feature: Online Service for People with Claims(OSPC)

  Background: Login to OSPC
    Given User is on login page
    When User submit email and password
    Then User should be able to login successfully and new page open

  @SheetName_GWCC @OSPC @OSPCUI
  Scenario: Login and Add a Claim
    Given User is on my Claim screen
    Then Switch to New tab and Login to ClaimCenter
    And Navigate to Portal Authorization screen
    And Remove all the claims from profile
    And Switch back to OSPC and verify if the "<ClaimNumber>" is removed
    When User click on Add a Claim button
    Then User should be able to add claim with "<ClaimNumber>", "<DOB>" and "<PIN>"


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

  @SheetName_GWCC @OSPC @OSPCDocUpload
  Scenario Outline: Upload WSIB form Document and Verify Submitted Document Screen
    Given User is on my Claim screen
    When User clicks on "<claimnumber>"
    Then User should be navigated to My Claims Overview screen
    And User should be navigated to Submit Document screen
    And User should be able to upload a document with "<filename>"
    And User should be able to select WSIB form "<wsibform>"
    And Select confirmation checkbox and Click on Submit button
    And Verify the Document History is updated with "<filename>"
    Then Switch to New tab and Login to ClaimCenter
    And Search for "<claimnumber>"
    And Verify the document is landed in GWCC with "<Author>", "<Description>"
    Examples:
     | wsibform              | filename                                                | Author      |  Description      |
     | Dental report (0278A) | 0717B_202410_LongTermRateEarningsQuestionnaire_ACES.pdf | OSPC        |  Dental report    |

  @SheetName_GWCC @DirectDeposit  @PrerequisiteTest
  Scenario Outline: Add Direct Deposit Information in OSPC
    Given User is on my Claim screen
    When User clicks on Direct Deposit
    Then Click Add direct Deposit information and Select "<Month>" and "<Year>"
    And Enter "<Transit number>", "<Institution number>", "<Account number>" and Confirm details

    Examples:
     | Transit number | Institution number | Account number |
     |  00100         | 010                |  123456789     |

  @SheetName_GWCC @DirectDeposit @DependentTest @DDSanity
  Scenario Outline: Edit Direct Deposit Information in OSPC
    Given User is on my Claim screen
    When User clicks on Direct Deposit
    Then Click Edit direct Deposit information and Select "<Month>" and "<Year>"
    And Enter "<Transit number>", "<Institution number>", "<Account number>" and Confirm details
#    And Switch to New tab and Login to ClaimCenter
#    And Search for Claim number
#    And Remove the Direct Deposit from the Claim
#    And Verify the Direct Deposit is not displayed in OSPC
    Examples:
  | Transit number | Institution number | Account number |
  |  01001         | 010                |  987654321     |

  @SheetName_eForm6_PI @eForm6_PI @eForm6
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
    And Switch to New tab and Login to ClaimCenter
    And Search for Reference Number
    And Verify intake and Worker Information and Validate worker Address
    And User searches for "1169879" with bussiness activity
    And User enters Accident Details as below:
      | Date of Accident  |   17/12/2024 |
      | Date Reported     |   17/12/2024 |
    And User Enters Body part details as below:
      | Area of Body      |   Head     |
      | Area of Injury    |   Head    |
      | Accident Type     |   Serious Injury     |
      | Detailed Accident Type    |  Burn    |
      | Nature of Injury      |   Major Burns      |
    And Select Third Party accident as "No"
    And Select Accident Location from the dropdown
    And Select Health Care Received as "No"
    And Select Claim Type as "Lost Time"
    And Click on Register Claim button and verify that Claim has been registered Successfully
    And Switch back to OSPC and verify if the "<ClaimNumber>" is added
    Then Switch to New tab and Login to ClaimCenter
    And Navigate to Portal Authorization screen
    And Remove the "<ClaimNumber>" from profile
    And Switch back to OSPC and verify if the "<ClaimNumber>" is removed


  @SheetName_eForm6_Illness @eForm6_illness @eForm6
  Scenario: Login and Submit a Report for Illness, disease or medical condition
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
    And User searches for "1169879" with bussiness activity
    And User enters Accident Details as below:
      | Date of Accident  |   17/12/2024 |
      | Date Reported     |   17/12/2024 |
    And User Enters Body part details as below:
      | Area of Body      |   Head     |
      | Area of Injury    |   Head    |
      | Accident Type     |   Serious Injury     |
      | Detailed Accident Type    |  Burn    |
      | Nature of Injury      |   Major Burns      |
    And Select Third Party accident as "No"
    And Select Accident Location from the dropdown
    And Select Health Care Received as "No"
    And Select Claim Type as "Lost Time"
    And Click on Register Claim button and verify that Claim has been registered Successfully
    And Switch back to OSPC and verify if the "<ClaimNumber>" is added
    Then Switch to New tab and Login to ClaimCenter
    And Navigate to Portal Authorization screen
    And Remove the "<ClaimNumber>" from profile
    And Switch back to OSPC and verify if the "<ClaimNumber>" is removed


  @SheetName_eForm6_Mental @eForm6_Mental @eForm6
  Scenario: Login and Submit a Report for mental health
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
    And User searches for "1169879" with bussiness activity
    And User enters Accident Details as below:
      | Date of Accident  |   17/12/2024 |
      | Date Reported     |   17/12/2024 |
    And User Enters Body part details as below:
      | Area of Body      |   Head     |
      | Area of Injury    |   Head    |
      | Accident Type     |   Serious Injury     |
      | Detailed Accident Type    |  Burn    |
      | Nature of Injury      |   Major Burns      |
    And Select Third Party accident as "No"
    And Select Accident Location from the dropdown
    And Select Health Care Received as "No"
    And Select Claim Type as "Lost Time"
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

  @SheetName_eForm6_Construction @eForm6_Construction @eForm6
  Scenario: Login and Submit a Report for Exposure Incident - Construction
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
    And User searches for "1169879" with bussiness activity
    And User enters Accident Details as below:
      | Date of Accident  |   17/12/2024 |
      | Date Reported     |   17/12/2024 |
    And User Enters Body part details as below:
      | Area of Body      |   Head     |
      | Area of Injury    |   Head    |
      | Accident Type     |   Serious Injury     |
      | Detailed Accident Type    |  Burn    |
      | Nature of Injury      |   Major Burns      |
    And Select Third Party accident as "No"
    And Select Accident Location from the dropdown
    And Select Health Care Received as "No"
    And Select Claim Type as "Lost Time"
    And Click on Register Claim button and verify that Claim has been registered Successfully

  @SheetName_eForm6_Other @eForm6_Other @eForm6
  Scenario: Login and Submit a Report for Exposure Incident - Other
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
    And User searches for "1169879" with bussiness activity
    And User enters Accident Details as below:
      | Date of Accident  |   17/12/2024 |
      | Date Reported     |   17/12/2024 |
    And User Enters Body part details as below:
      | Area of Body      |   Head     |
      | Area of Injury    |   Head    |
      | Accident Type     |   Serious Injury     |
      | Detailed Accident Type    |  Burn    |
      | Nature of Injury      |   Major Burns      |
    And Select Third Party accident as "No"
    And Select Accident Location from the dropdown
    And Select Health Care Received as "No"
    And Select Claim Type as "Lost Time"
    And Click on Register Claim button and verify that Claim has been registered Successfully