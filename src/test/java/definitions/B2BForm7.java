package definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import org.junit.Assert;
import pages.OSPC.OSPCClaimOverview;
import utils.ConfigReader;
import utils.ExcelUtils;

import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.random.RandomGenerator;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static utils.BaseDriverClass.*;

public class B2BForm7 {


	private OSPCClaimOverview OSPCClaimOverview;
	private String endpoint;
	private String requestBody;
	private Response response;
	private String Filepath = "src/test/resources/config/B2BForm7.properties"; // Path to your properties file

	ConfigReader prop= Hooks.getConfigReader();
	List<Map<String, String>> data = Hooks.data;
	public B2BForm7() throws Exception {

	}

	@Given("the API endpoint is configured with {string}")
	public void theAPIEndpointIsConfiguredWith(String Endpoint) {
		endpoint = prop.getProperty("AuthHandler")+ Endpoint;
	}

	@Given("the API Token endpoint is configured with {string}")
	public void theAPITokenEndpointIsConfiguredWith(String TokenEndpoint) {
		endpoint = prop.getProperty("AuthHandler")+ TokenEndpoint;
	}

	@And("the request body is:")
	public void theRequestBodyIs(String body) {
		String claimNumber = prop.getProperty("ClaimNumber");
		String FileId = prop.getProperty("FileId");
		String LoginId = prop.getProperty("userLoginID");
		String LastName = prop.getProperty("LastName");
		String dateOfBirth = prop.getProperty("dateOfBirth");
		String Month = prop.getProperty("Month");
		String Year = prop.getProperty("Year");
		String InstitutionNumber = prop.getProperty("InstitutionNumber");
		String TransitNumber = prop.getProperty("TransitNumber");
		String BankAccountNumber = prop.getProperty("BankAccountNumber");
		String DD_publicID = prop.getProperty("DD_publicID");
		String RefNumber = prop.getProperty("RefNumber");
		String SessionID= prop.getProperty("SessionId");
		String UploadFileID= prop.getProperty("UploadFileId");
		String Filename = prop.getProperty("Filename");
		// Fetch ClaimNumber from config.properties
		requestBody = body.replace("{{ClaimNumber}}", claimNumber).replace("{{FileID}}", FileId).replace("{{userLoginID}}", LoginId)
				.replace( "{{LastName}}", LastName).replace("{{dateOfBirth}}", dateOfBirth)
				.replace("{{Month}}", Month)
				.replace("{{Year}}", Year)
				.replace("{{InstitutionNumber}}", InstitutionNumber)
				.replace("{{TransitNumber}}", TransitNumber)
				.replace("{{BankAccountNumber}}", BankAccountNumber)
				.replace("{{DD_publicID}}", DD_publicID)
				.replace("{{RefNumber}}", RefNumber)
				.replace("{{Filename}}", Filename)
				.replace("{{SessionId}}", SessionID)
				.replace("{{UploadFileId}}", UploadFileID);

	}

	@When("a POST request is sent")
	public void aPOSTRequestIsSent() {
		// Disable SSL validation
		RestAssured.useRelaxedHTTPSValidation();
		String bearerToken = prop.getProperty("BearerToken");
		response = RestAssured
				.given()
				.log().all() // Logs the request details
				.header("Authorization", "Bearer " + bearerToken)
				.header("Content-Type", "application/json")
				.body(requestBody)
				.post(endpoint);
	}

	@Then("the response status code should be {int}")
	public void theResponseStatusCodeShouldBe(int statusCode) throws Exception {
		// Log the response details for debugging
		//response.then().log().all(); // Logs the response details
		response.prettyPrint(); // Prints the response body
		assertThat(response.getStatusCode(), is(statusCode));
		// Capture the current timestamp

	}

	@When("a POST request is sent with form data")
	public void aPOSTRequestIsSentWithFormData() {
		String bearerToken = prop.getProperty("BearerToken");
		File file = new File(prop.getProperty("Filepath")); // Path to the file
		response = RestAssured
				.given()
				.log().all() // Logs the request details
				.header("Authorization", "Bearer " + bearerToken)
				.header("Content-Type", "multipart/form-data")
				.multiPart("file", file) // Attach the file
				.formParam("claimId", prop.getProperty("ClaimNumber")) // Add claimId as form data
				.post(endpoint);

	}

	@Then("Extract the file id from the response")
	public void extractTheFileIdFromTheResponse() throws Exception {

		String fileId = response.jsonPath().getString("name");
		Assert.assertNotNull("File ID should not be null", fileId);
		prop.setProperty("FileId", fileId, Filepath); // Store the fileId in config.properties
		System.out.println("Extracted File ID: " + fileId);
	}


	@When("a POST request is sent with Claimid as parameters")
	public void aPOSTRequestIsSentWithClaimidAsParameters() {
		String bearerToken = prop.getProperty("BearerToken");
		response = RestAssured
				.given()
				.log().all() // Logs the request details
				.header("Authorization", "Bearer " + bearerToken)
				.queryParam("claimId", 	prop.getProperty("ClaimNumber")) // Add claimId as a query parameter
				.get(endpoint);
	}


	@Then("Capture the Public id from the response")
	public void captureThePublicIdFromTheResponse() throws Exception {
		String PublicId = response.jsonPath().getString("result.publicID");
		Assert.assertNotNull("Public ID should not be null", PublicId);
		prop.setProperty("DD_publicID", PublicId, Filepath); // Store the fileId in config.properties
		System.out.println("Extracted PublicId: " + PublicId);

	}

	@And("the request body is identified: {string}")
	public void theRequestBodyIsIdentified(String type) {
		// Fetch ClaimNumber from config.properties
		if(type.contentEquals("Injury")) {
			requestBody = """
					{
					                                   "id": "b1385a2c-8477-4580-903c-f7c3c8331aa6_32",
					                                   "jsonrpc": "2.0",
					                                   "method": "createClaim",
					                                   "params": [
					                                       {
					                                           "privacyWC": true,
					                                           "diwizardStep": "DI_EmployerDetails",
					                                           "reportType": "injuryOrIllness",
					                                           "intakeChannel": "OSPC_DI_Web",
					                                           "originalChannel": "OSPC_DI_Web",
					                                           "categoryOfIllness": "physicalInjury",
					                                           "oneTime": "no",
					                                           "injuryDescribedAs": "Faint",
					                                           "lossDetails": {
					                                               "injuryIncident": {
					                                                   "accidentIllnessCat": "GraduallyOccuringOverTime",
					                                                   "natureOfAccident": "Faint"
					                                               }
					                                           },
					                                           "claimant": {
					                                               "firstName": "Kiran",
					                                               "lastName": "Kangula",
					                                               "pronoun_Ext": "he_him",
					                                               "workNumber": "234-234-2342",
					                                               "gender": "M",
					                                               "dateOfBirthLocal": {
					                                                   "year": 2000,
					                                                   "month": 0,
					                                                   "day": 1
					                                               },
					                                               "languageOfService_Ext": "english",
					                                               "show": false,
					                                               "workerSinNoExist_Ext": true,
					                                               "primaryAddress": {
					                                                   "country": "CA",
					                                                   "state": "ON",
					                                                   "addressLine1": "140 Erskine Avenue",
					                                                   "city": "Toronto",
					                                                   "postalCode": "M4P 1Z2"
					                                               }
					                                           },
					                                           "checkAgeUnderSixteen_WC": false,
					                                           "languageOfServiceUi": "en",
					                                           "employer": {
					                                               "primaryAddress": {
					                                                   "country": "CA",
					                                                   "state": "ON",
					                                                   "addressLine1": "140 Erskine Avenue",
					                                                   "city": "Toronto",
					                                                   "postalCode": "M4P 1Z2"
					                                               },
					                                               "contactName": "InjuryTest",
					                                               "workNumber": "000-000-0000"
					                                           }
					                                       }
					                                   ]
					                               }
					 """;
		}
		else if (type.contentEquals("Illness"))
		{
			requestBody = """
					{
						"id": "eb41e556-7a50-4cde-920e-d45d5810baab_2",
						"jsonrpc": "2.0",
						"method": "createClaim",
						"params": [
							{
								"privacyWC": true,
								"diwizardStep": "DI_EmployerDetails",
								"reportType": "injuryOrIllness",
								"intakeChannel": "OSPC_DI_Web",
								"originalChannel": "OSPC_DI_Web",
								"lossDetails": {
									"injuryIncident": {
										"accidentIllnessCat": "OccupationalDisease"
									}
								},
								"exposureApplicable": "false",
								"categoryOfIllness": "illness",
								"claimant": {
									"firstName": "Kiran",
									"lastName": "Kangula",
									"pronoun_Ext": "he_him",
									"workNumber": "647-666-6666",
									"gender": "M",
									"dateOfBirthLocal": {
										"year": 2000,
										"month": 0,
										"day": 1
									},
									"languageOfService_Ext": "english",
									"show": false,
									"workerSinNoExist_Ext": true,
									"primaryAddress": {
										"country": "CA",
										"state": "ON",
										"addressLine1": "140 Erskine Avenue",
										"city": "Toronto",
										"postalCode": "M4P 1Z2"
									}
								},
								"checkAgeUnderSixteen_WC": false,
								"languageOfServiceUi": "en",
								"employer": {
									"primaryAddress": {
										"country": "CA",
										"state": "ON",
										"addressLine1": "140 Erskine Avenue",
										"city": "Toronto",
										"postalCode": "M4P 1Z2"
									},
									"contactName": "IllnessTest",
									"workNumber": "000-000-0000"
								}
							}
						]
					} 
					 """;
		}
		else if (type.contentEquals("Mental Health"))
		{
			requestBody = """
					{
					    "id": "eb41e556-7a50-4cde-920e-d45d5810baab_4",
					    "jsonrpc": "2.0",
					    "method": "createClaim",
					    "params": [
					        {
					            "privacyWC": true,
					            "diwizardStep": "DI_EmployerDetails",
					            "reportType": "injuryOrIllness",
					            "intakeChannel": "OSPC_DI_Web",
					            "originalChannel": "OSPC_DI_Web",
					            "categoryOfIllness": "mentalHealth",
					            "lossDetails": {
					                "injuryIncident": {
					                    "accidentIllnessCat": "PsychologicalCondition",
					                    "natureOfAccident": "CumPsycoEvent"
					                }
					            },
					            "claimant": {
					                "firstName": "Kiran",
					                "lastName": "Kangula",
					                "pronoun_Ext": "he_him",
					                "workNumber": "647-888-8888",
					                "gender": "M",
					                "dateOfBirthLocal": {
					                    "year": 2000,
					                    "month": 0,
					                    "day": 1
					                },
					                "languageOfService_Ext": "english",
					                "show": false,
					                "workerSinNoExist_Ext": true,
					                "primaryAddress": {
					                    "country": "CA",
					                    "state": "ON",
					                    "addressLine1": "140 Erskine Avenue",
					                    "city": "Toronto",
					                    "postalCode": "M4P 1Z2"
					                }
					            },
					            "checkAgeUnderSixteen_WC": false,
					            "languageOfServiceUi": "en",
					            "employer": {
					                "primaryAddress": {
					                    "country": "CA",
					                    "state": "ON",
					                    "addressLine1": "140 Erskine Avenue",
					                    "city": "Toronto",
					                    "postalCode": "M4P 1Z2"
					                },
					                "contactName": "EmployerMental",
					                "workNumber": "000-000-0000"
					            }
					        }
					    ]
					}
					 """;
		}
		else if(type.contentEquals("Construction"))
		{
			requestBody = """
					{
					    "id": "eb41e556-7a50-4cde-920e-d45d5810baab_6",
					    "jsonrpc": "2.0",
					    "method": "createClaim",
					    "params": [
					        {
					            "privacyWC": true,
					            "diwizardStep": "DI_EmployerDetails",
					            "reportType": "exposureIncident",
					            "intakeChannel": "OSPC_DI_Web",
					            "originalChannel": "OSPC_DI_Web",
					            "lossDetails": {
					                "injuryIncident": {
					                    "natureOfAccident": "ExposureReportWithoutInjury",
					                    "accidentIllnessCat": "OccupationalDisease"
					                },
					                "peirceir": {
					                    "workerInConstruction": true
					                }
					            },
					            "exposureApplicable": "false",
					            "claimant": {
					                "firstName": "Kiran",
					                "lastName": "Kangula",
					                "pronoun_Ext": "he_him",
					                "workNumber": "647-888-8888",
					                "languageOfService_Ext": "english",
					                "gender": "M",
					                "workerSinNoExist_Ext": true,
					                "dateOfBirthLocal": {
					                    "year": 2000,
					                    "month": 0,
					                    "day": 1
					                },
					                "primaryAddress": {
					                    "country": "CA",
					                    "state": "ON",
					                    "addressLine1": "140 Erskine Avenue",
					                    "city": "Toronto",
					                    "postalCode": "M4P 1Z2"
					                }
					            },
					            "checkAgeUnderSixteen_WC": false,
					            "employer": {
					                "primaryAddress": {
					                    "country": "CA",
					                    "state": "ON",
					                    "addressLine1": "140 Erskine Avenue",
					                    "city": "Toronto",
					                    "postalCode": "M4P 1Z2"
					                },
					                "contactName": "EmployerConstruction",
					                "workNumber": "647-777-7777"
					            }
					        }
					    ]
					}
					 """;
		}
		else if (type.contentEquals("Other Industry"))
		{
			requestBody = """
					{
					    "id": "eb41e556-7a50-4cde-920e-d45d5810baab_8",
					    "jsonrpc": "2.0",
					    "method": "createClaim",
					    "params": [
					        {
					            "privacyWC": true,
					            "diwizardStep": "DI_EmployerDetails",
					            "reportType": "exposureIncident",
					            "intakeChannel": "OSPC_DI_Web",
					            "originalChannel": "OSPC_DI_Web",
					            "lossDetails": {
					                "injuryIncident": {
					                    "natureOfAccident": "ExposureReportWithoutInjury",
					                    "accidentIllnessCat": "OccupationalDisease"
					                },
					                "peirceir": {
					                    "workerInConstruction": false
					                }
					            },
					            "exposureApplicable": "false",
					            "claimant": {
					                "firstName": "Kiran",
					                "lastName": "Kangula",
					                "pronoun_Ext": "he_him",
					                "workNumber": "647-888-8888",
					                "languageOfService_Ext": "english",
					                "gender": "M",
					                "workerSinNoExist_Ext": true,
					                "dateOfBirthLocal": {
					                    "year": 2000,
					                    "month": 0,
					                    "day": 1
					                },
					                "primaryAddress": {
					                    "country": "CA",
					                    "state": "ON",
					                    "addressLine1": "140 Erskine Avenue",
					                    "city": "Toronto",
					                    "postalCode": "M4P 1Z2"
					                }
					            },
					            "checkAgeUnderSixteen_WC": false,
					            "employer": {
					                "primaryAddress": {
					                    "country": "CA",
					                    "state": "ON",
					                    "addressLine1": "140 Erskine Avenue",
					                    "city": "Toronto",
					                    "postalCode": "M4P 1Z2"
					                },
					                "contactName": "OtherIndustry",
					                "workNumber": "647-099-8888"
					            }
					        }
					    ]
					}
					 """;
		}

	}

	@Then("Capture the ReferenceNumber from the response")
	public void captureTheReferenceNumberFromTheResponse() throws Exception {

		String RefNumber = response.jsonPath().getString("result.claimNumber");
		Assert.assertNotNull("Ref Number should not be null", RefNumber);
		prop.setProperty("RefNumber", RefNumber, Filepath);
		System.out.println("Extracted RefNumber: " + RefNumber);
	}

	@And("the request body for submit claim is configured: {string}")
	public void theRequestBodyForSubmitClaimIsConfigured(String type) {
		if(type.contentEquals("Injury")) {
			requestBody = """
					{
					                                	"id": "b1385a2c-8477-4580-903c-f7c3c8331aa6_32",
					                                	"jsonrpc": "2.0",
					                                	"method": "submitClaim",
					                                	"params": [
					                                		{
					                                			"privacyWC": true,
					                                			"diwizardStep": "DI_ReviewAndSubmit",
					                                			"reportType": "injuryOrIllness",
					                                			"intakeChannel": "OSPC_DI_Web",
					                                			"originalChannel": "OSPC_DI_Web",
					                                			"oneTime": "no",
					                                			"injuryDescribedAs": "Faint",
					                                			"categoryOfIllness": "physicalInjury",
					                                			"languageOfServiceUi": "en",
					                                			"createTime": "2025-06-16T19:50:16Z",
					                                       \s
					                                			"claimNumber": "{{RefNumber}}",
					                                			"claimant": {
					                                				"displayName": "Kiran Kangula",
					                                				"subtype": "WSIB_PersonWorker",
					                                				"firstName": "Kiran",
					                                				"lastName": "Kangula",
					                                				"gender": "M",
					                                				"primaryAddress": {
					                                					"state": "ON",
					                                					"country": "CA",
					                                					"displayName": "140 Erskine Avenue,\\nToronto, ON M4P 1Z2",
					                                				
					                                					"city": "Toronto",
					                                					"postalCode": "M4P 1Z2",
					                                					"addressLine1": "140 Erskine Avenue"
					                                				},
					                                				"contactType": "WSIB_PersonWorker",
					                                				"workNumber": "234-234-2342",
					                                				"dateOfBirthLocal": {
					                                					"month": 0,
					                                					"year": 2000,
					                                					"day": 1
					                                				},
					                                				"pronoun_Ext": "he_him",
					                                				"languageOfService_Ext": "english",
					                                				"workerSinNoExist_Ext": true
					                                			},
					                                			"claimState": "draft",
					                                			"claimReportedByType": "worker",
					                                			"checkAgeUnderSixteen_WC": false,
					                                			"employer": {
					                                				"displayName": "InjuryTest",
					                                				"subtype": "Company",
					                                				
					                                				"primaryAddress": {
					                                					"state": "ON",
					                                					"country": "CA",
					                                					"displayName": "140 Erskine Avenue,\\nToronto, ON M4P 1Z2",
					                                					
					                                					"city": "Toronto",
					                                					"postalCode": "M4P 1Z2",
					                                					"addressLine1": "140 Erskine Avenue"
					                                				},
					                                				"contactType": "Company",
					                                				"contactName": "InjuryTest",
					                                				"workNumber": "000-000-0000",
					                                				"primaryPhoneValue_WSIB": "000-000-0000"
					                                			},
					                                			"lossDetails": {
					                                				"injuryIncident": {
					                                				
					                                					"accidentIllnessCat": "GraduallyOccuringOverTime",
					                                					"natureOfAccident": "Faint",
					                                					"firstGradualTimeDesc": "Test",
					                                					"bodyPartDetails": [
					                                						{
					                                						
					                                							"detailedBodyPart": "Head",
					                                							"primaryBodyPart": "head"
					                                						}
					                                					],
					                                					"havsdetails": [],
					                                					"nihldetails": []
					                                				},
					                                				"accidentLocation": {
					                                					"state": "ON",
					                                					"country": "CA",
					                                					"displayName": "140 Erskine Avenue,\\nToronto, ON M4P 1Z2",
					                                					
					                                					"city": "Toronto",
					                                					"postalCode": "M4P 1Z2",
					                                					"addressLine1": "140 Erskine Avenue",
					                                					"areaOfAccident": "WorkOrJobSite"
					                                				},
					                                				"lossTimeDetails": {
					                                				
					                                					"lostTimeDueToInjury": false,
					                                					"rtwisWageLoss": false,
					                                					"typeOfReturnedWork": "Regular"
					                                				},
					                                				"healthCare": {
					                                					
					                                					"hcreceivedWorker": "No"
					                                				},
					                                				"priorSimilarCond": false,
					                                				"accidentWitness": false
					                                			},
					                                			"employmentData": {
					                                			
					                                				"occupation": "Job",
					                                				"hireDate": {
					                                					"month": 0,
					                                					"year": 2023,
					                                					"day": 1
					                                				},
					                                				"wsib_BenEarningsSummaries": [
					                                					{
					                                						
					                                						"benefitEarnings": [
					                                							{
					                                								
					                                							}
					                                						],
					                                						"otherBenefitEarnings": []
					                                					}
					                                				],
					                                				"company20Employee": false,
					                                				"wsib_EmployerRelation": "self_employed",
					                                				"wsib_IsSpouseRelavtive": false,
					                                				"employmentLengthYear": 2,
					                                				"employmentLengthMonth": 1,
					                                				"representation": false
					                                			},
					                                			"immediateSupervisor": {
					                                				"displayName": "Test Super",
					                                				"subtype": "WSIB_PersonOther",
					                                			
					                                				"firstName": "Test",
					                                				"lastName": "Super",
					                                				"contactType": "WSIB_PersonOther",
					                                				"workNumber": "647-666-6666"
					                                			},
					                                			"accidentHistory": "Test",
					                                			"onsetFromDate": {
					                                				"month": 0,
					                                				"year": 2024,
					                                				"day": 1
					                                			},
					                                			"signatAknlgmt": true,
					                                			"isSubmitEnroll": true,
					                                			"allowAutoEnrollment": true
					                                		}
					                                	]
					                                }
					 """.replace("{{RefNumber}}", prop.getProperty("RefNumber")); // Replace placeholder with actual value
		}
		else if(type.contentEquals("Illness"))
		{
			requestBody = """
					{
						"id": "655f5504-0f06-43e5-bd51-6aa9b16fd259_44",
						"jsonrpc": "2.0",
						"method": "submitClaim",
						"params": [
							{
								"privacyWC": true,
								"diwizardStep": "DI_ReviewAndSubmit",
								"reportType": "injuryOrIllness",
								"intakeChannel": "OSPC_DI_Web",
								"originalChannel": "OSPC_DI_Web",
								"categoryOfIllness": "illness",
								"languageOfServiceUi": "en",
								"createTime": "2025-06-18T14:14:53Z",
								"claimNumber": "{{RefNumber}}",
								"claimant": {
									"displayName": "Kiran Kangula",
									"subtype": "WSIB_PersonWorker",
									"firstName": "Kiran",
									"lastName": "Kangula",
									"gender": "M",
									"primaryAddress": {
										"state": "ON",
										"country": "CA",
										"displayName": "140 Erskine Avenue,\\nToronto, ON M4P 1Z2",
										"city": "Toronto",
										"postalCode": "M4P 1Z2",
										"addressLine1": "140 Erskine Avenue"
									},
									"contactType": "WSIB_PersonWorker",
									"workNumber": "647-666-6666",
									"dateOfBirthLocal": {
										"month": 0,
										"year": 2000,
										"day": 1
									},
									"pronoun_Ext": "he_him",
									"languageOfService_Ext": "english",
									"workerSinNoExist_Ext": true
								},
								"claimState": "draft",
								"claimReportedByType": "worker",
								"checkAgeUnderSixteen_WC": false,
								"employer": {
									"displayName": "IllnessTest",
									"subtype": "Company",
									"primaryAddress": {
										"state": "ON",
										"country": "CA",
										"displayName": "140 Erskine Avenue,\\nToronto, ON M4P 1Z2",
										"city": "Toronto",
										"postalCode": "M4P 1Z2",
										"addressLine1": "140 Erskine Avenue"
									},
									"contactType": "Company",
									"contactName": "IllnessTest",
									"workNumber": "000-000-0000",
									"primaryPhoneValue_WSIB": "000-000-0000"
								},
								"lossDetails": {
									"injuryIncident": {
										"accidentCatOptns": "NA",
										"accidentIllnessCat": "OccupationalDisease",
										"od_illnessDetails": "Sfasasdf",
										"od_treatmentDetails": "asdfadfasasdfaqf",
										"bodyPartDetails": [],
										"havsdetails": [],
										"nihldetails": []
									},
									"accidentLocation": {
										"state": "ON",
										"country": "CA",
										"displayName": "140 Erskine Avenue,\\nToronto, ON M4P 1Z2",
										"city": "Toronto",
										"postalCode": "M4P 1Z2",
										"addressLine1": "140 Erskine Avenue",
										"areaOfAccident": "WorkOrJobSite"
									},
									"lossTimeDetails": {
										"lostTimeDueToInjury": false,
										"rtwisWageLoss": false,
										"typeOfReturnedWork": "Regular"
									},
									"healthCare": {
										"hcreceivedWorker": "No"
									},
									"priorSimilarCond": false,
									"accidentWitness": false
								},
								"employmentData": {
									"occupation": "Test",
									"hireDate": {
										"month": 0,
										"year": 2000,
										"day": 1
									},
									"wsib_BenEarningsSummaries": [
										{
											"benefitEarnings": [
												{
												}
											],
											"otherBenefitEarnings": []
										}
									],
									"company20Employee": false,
									"wsib_EmployerRelation": "self_employed",
									"wsib_IsSpouseRelavtive": false,
									"employmentLengthYear": 1,
									"employmentLengthMonth": 1,
									"representation": false
								},
								"immediateSupervisor": {
									"displayName": "Test Super",
									"subtype": "WSIB_PersonOther",
									"firstName": "Test",
									"lastName": "Super",
									"contactType": "WSIB_PersonOther",
									"workNumber": "000-000-0000"
								},
								"accidentHistory": "asdfasdf",
								"onsetFromDate": {
									"month": 0,
									"year": 2023,
									"day": 1
								},
								"additionalIntakeComments_WC": "asasdasdasd",
								"signatAknlgmt": true,
								"isSubmitEnroll": true,
								"allowAutoEnrollment": true
							}
						]
					}
				""".replace("{{RefNumber}}", prop.getProperty("RefNumber")); // Replace placeholder with actual value
		}
		else if(type.contentEquals("Mental Health"))
		{
			requestBody = """
					{
						"id": "655f5504-0f06-43e5-bd51-6aa9b16fd259_24",
						"jsonrpc": "2.0",
						"method": "submitClaim",
						"params": [
							{
								"privacyWC": true,
								"diwizardStep": "DI_ReviewAndSubmit",
								"reportType": "injuryOrIllness",
								"intakeChannel": "OSPC_DI_Web",
								"originalChannel": "OSPC_DI_Web",
								"categoryOfIllness": "mentalHealth",
								"languageOfServiceUi": "en",
								"createTime": "2025-06-18T14:15:19Z",
								"claimNumber": "{{RefNumber}}",
								"claimant": {
									"displayName": "Kiran Kangula",
									"subtype": "WSIB_PersonWorker",
									"firstName": "Kiran",
									"lastName": "Kangula",
									"gender": "M",
									"primaryAddress": {
										"state": "ON",
										"country": "CA",
										"displayName": "140 Erskine Avenue,\\nToronto, ON M4P 1Z2",
										"publicID": "cc:651890446",
										"city": "Toronto",
										"postalCode": "M4P 1Z2",
										"addressLine1": "140 Erskine Avenue"
									},
									"contactType": "WSIB_PersonWorker",
									"workNumber": "647-888-8888",
									"dateOfBirthLocal": {
										"month": 0,
										"year": 2000,
										"day": 1
									},
									"pronoun_Ext": "he_him",
									"languageOfService_Ext": "english",
									"workerSinNoExist_Ext": true
								},
								"claimState": "draft",
								"claimReportedByType": "worker",
								"checkAgeUnderSixteen_WC": false,
								"employer": {
									"displayName": "EmployerMental",
									"subtype": "Company",
									"primaryAddress": {
										"state": "ON",
										"country": "CA",
										"displayName": "140 Erskine Avenue,\\nToronto, ON M4P 1Z2",
										"publicID": "cc:651890447",
										"city": "Toronto",
										"postalCode": "M4P 1Z2",
										"addressLine1": "140 Erskine Avenue"
									},
									"contactType": "Company",
									"contactName": "EmployerMental",
									"workNumber": "000-000-0000",
									"primaryPhoneValue_WSIB": "000-000-0000"
								},
								"lossDetails": {
									"injuryIncident": {
										"accidentCatOptns": "NA",
										"accidentIllnessCat": "PsychologicalCondition",
										"natureOfAccident": "CumPsycoEvent",
										"bodyPartDetails": [],
										"havsdetails": [],
										"nihldetails": []
									},
									"accidentLocation": {
										"state": "ON",
										"country": "CA",
										"displayName": "140 Erskine Avenue,\\nToronto, ON M4P 1Z2",
										"city": "Toronto",
										"postalCode": "M4P 1Z2",
										"addressLine1": "140 Erskine Avenue",
										"areaOfAccident": "WorkOrJobSite"
									},
									"lossTimeDetails": {
										"lostTimeDueToInjury": false,
										"rtwisWageLoss": false,
										"typeOfReturnedWork": "Regular"
									},
									"healthCare": {
										"hcreceivedWorker": "No"
									},
									"priorSimilarCond": false,
									"accidentWitness": false
								},
								"employmentData": {
									"occupation": "Ilness title",
									"hireDate": {
										"month": 0,
										"year": 2000,
										"day": 1
									},
									"wsib_BenEarningsSummaries": [
										{
											"benefitEarnings": [
												{
												}
											],
											"otherBenefitEarnings": []
										}
									],
									"company20Employee": false,
									"wsib_EmployerRelation": "self_employed",
									"wsib_IsSpouseRelavtive": false,
									"employmentLengthYear": 1,
									"employmentLengthMonth": 1,
									"representation": false
								},
								"immediateSupervisor": {
									"displayName": "Test Super",
									"subtype": "WSIB_PersonOther",
									"firstName": "Test",
									"lastName": "Super",
									"contactType": "WSIB_PersonOther",
									"workNumber": "000-000-0000"
								},
								"accidentHistory": "Reqsasd",
								"onsetFromDate": {
									"month": 0,
									"year": 2023,
									"day": 1
								},
								"periodOnSetToDate": {
									"month": 0,
									"year": 2023,
									"day": 1
								},
								"psycholphysicalinjury_wc": false,
								"additionalIntakeComments_WC": "Test",
								"signatAknlgmt": true,
								"isSubmitEnroll": true,
								"allowAutoEnrollment": true
							}
						]
					}
				""".replace("{{RefNumber}}", prop.getProperty("RefNumber")); // Replace placeholder with actual value
		}
		else if(type.contentEquals("Construction"))
		{
			requestBody = """
					{
						"id": "655f5504-0f06-43e5-bd51-6aa9b16fd259_59",
						"jsonrpc": "2.0",
						"method": "submitClaim",
						"params": [
							{
								"privacyWC": true,
								"diwizardStep": "DI_ReviewAndSubmit",
								"reportType": "exposureIncident",
								"intakeChannel": "OSPC_DI_Web",
								"originalChannel": "OSPC_DI_Web",
								"languageOfServiceUi": "en",
								"createTime": "2025-06-18T14:15:43Z",
								"claimNumber": "{{RefNumber}}",
								"claimant": {
									"displayName": "Kiran Kangula",
									"subtype": "WSIB_PersonWorker",
									"firstName": "Kiran",
									"lastName": "Kangula",
									"gender": "M",
									"primaryAddress": {
										"state": "ON",
										"country": "CA",
										"displayName": "140 Erskine Avenue,\\nToronto, ON M4P 1Z2",
										"city": "Toronto",
										"postalCode": "M4P 1Z2",
										"addressLine1": "140 Erskine Avenue"
									},
									"contactType": "WSIB_PersonWorker",
									"workNumber": "647-888-8888",
									"dateOfBirthLocal": {
										"month": 0,
										"year": 2000,
										"day": 1
									},
									"pronoun_Ext": "he_him",
									"languageOfService_Ext": "english",
									"workerSinNoExist_Ext": true
								},
								"claimState": "draft",
								"claimReportedByType": "worker",
								"checkAgeUnderSixteen_WC": false,
								"employer": {
									"displayName": "EmployerConstruction",
									"subtype": "Company",
									"primaryAddress": {
										"state": "ON",
										"country": "CA",
										"displayName": "140 Erskine Avenue,\\nToronto, ON M4P 1Z2",
										"city": "Toronto",
										"postalCode": "M4P 1Z2",
										"addressLine1": "140 Erskine Avenue"
									},
									"contactType": "Company",
									"contactName": "EmployerConstruction",
									"workNumber": "647-777-7777",
									"primaryPhoneValue_WSIB": "647-777-7777"
								},
								"lossDetails": {
									"injuryIncident": {
										
										"accidentCatOptns": "NA",
										"accidentIllnessCat": "OccupationalDisease",
										"natureOfAccident": "ExposureReportWithoutInjury",
										"bodyPartDetails": [],
										"havsdetails": [],
										"nihldetails": []
									},
									"accidentLocation": {
										"state": "ON",
										"country": "CA",
										"displayName": "140 Erskine Avenue,\\nToronto, ON M4P 1Z2",
										"city": "Toronto",
										"postalCode": "M4P 1Z2",
										"addressLine1": "140 Erskine Avenue",
										"areaOfAccident": "WorkOrJobSite"
									},
									"peirceir": {
										"exposureSource": "Souircer",
										"hsrepCheck": false,
										"jhsccheck": false,
										"natureOfBusiness": "Testers",
										"peirCeirExposureCat": "InfectiousSubstance",
										"peirCeirExposureType": "BodyFluidSplash",
										"peirCeirSubstance": "Tuberculosis",
										"unionNameLocal": "Tester",
										"workerInConstruction": true
									}
								},
								"employmentData": {
									"occupation": "Test",
									"hireDate": {
										"month": 0,
										"year": 2000,
										"day": 1
									},
									"wsib_BenEarningsSummaries": [
										{
											"benefitEarnings": [
												{
												}
											],
											"otherBenefitEarnings": []
										}
									],
									"company20Employee": false,
									"wsib_EmployerRelation": "self_employed",
									"wsib_IsSpouseRelavtive": false
								},
								"onsetFromDate": {
									"month": 0,
									"year": 2023,
									"day": 1
								},
								"additionalIntakeComments_WC": "Test tetsdq",
								"signatAknlgmt": true
							}
						]
					}
				""".replace("{{RefNumber}}", prop.getProperty("RefNumber")); // Replace placeholder with actual value
		}
		else if(type.contentEquals("Other Industry"))
		{
			requestBody = """
					{
						"id": "655f5504-0f06-43e5-bd51-6aa9b16fd259_71",
						"jsonrpc": "2.0",
						"method": "submitClaim",
						"params": [
							{
								"privacyWC": true,
								"diwizardStep": "DI_ReviewAndSubmit",
								"reportType": "exposureIncident",
								"intakeChannel": "OSPC_DI_Web",
								"originalChannel": "OSPC_DI_Web",
								"languageOfServiceUi": "en",
								"createTime": "2025-06-18T14:16:05Z",
								"claimNumber": "{{RefNumber}}",
								"claimant": {
									"displayName": "Kiran Kangula",
									"subtype": "WSIB_PersonWorker",
									"firstName": "Kiran",
									"lastName": "Kangula",
									"gender": "M",
									"primaryAddress": {
										"state": "ON",
										"country": "CA",
										"displayName": "140 Erskine Avenue,\\nToronto, ON M4P 1Z2",
										"city": "Toronto",
										"postalCode": "M4P 1Z2",
										"addressLine1": "140 Erskine Avenue"
									},
									"contactType": "WSIB_PersonWorker",
									"workNumber": "647-888-8888",
									"dateOfBirthLocal": {
										"month": 0,
										"year": 2000,
										"day": 1
									},
									"pronoun_Ext": "he_him",
									"languageOfService_Ext": "english",
									"workerSinNoExist_Ext": true
								},
								"claimState": "draft",
								"claimReportedByType": "worker",
								"checkAgeUnderSixteen_WC": false,
								"employer": {
									"displayName": "OtherIndustry",
									"subtype": "Company",
									"primaryAddress": {
										"state": "ON",
										"country": "CA",
										"displayName": "140 Erskine Avenue,\\nToronto, ON M4P 1Z2",
										"city": "Toronto",
										"postalCode": "M4P 1Z2",
										"addressLine1": "140 Erskine Avenue"
									},
									"contactType": "Company",
									"contactName": "OtherIndustry",
									"workNumber": "647-099-8888",
									"primaryPhoneValue_WSIB": "647-099-8888"
								},
								"lossDetails": {
									"injuryIncident": {
										"accidentCatOptns": "NA",
										"accidentIllnessCat": "OccupationalDisease",
										"natureOfAccident": "ExposureReportWithoutInjury",
										"bodyPartDetails": [],
										"havsdetails": [],
										"nihldetails": []
									},
									"accidentLocation": {
										"state": "ON",
										"country": "CA",
										"displayName": "140 Erskine Avenue,\\nToronto, ON M4P 1Z2",
										"city": "Toronto",
										"postalCode": "M4P 1Z2",
										"addressLine1": "140 Erskine Avenue",
										"areaOfAccident": "WorkOrJobSite"
									},
									"peirceir": {
										"locationInAffectedArea": "Testest",
										"natureOfBusiness": "Testers",
										"peirExposureDetail": "Explosion",
										"peirCeirExposureCat": "ChemicalWorkplace",
										"workerInConstruction": false,
										"wornPPE": "teststst"
									}
								},
								"employmentData": {
									"occupation": "Test Ob",
									"hireDate": {
										"month": 0,
										"year": 2023,
										"day": 1
									},
									"wsib_BenEarningsSummaries": [
										{
											"benefitEarnings": [
												{
												}
											],
											"otherBenefitEarnings": []
										}
									],
									"company20Employee": false,
									"wsib_EmployerRelation": "self_employed",
									"wsib_IsSpouseRelavtive": false
								},
								"onsetFromDate": {
									"month": 0,
									"year": 2023,
									"day": 1
								},
								"additionalIntakeComments_WC": "SAasdasdasd",
								"signatAknlgmt": true
							}
						]
					}
				""".replace("{{RefNumber}}", prop.getProperty("RefNumber")); // Replace placeholder with actual value
		}


	}


	@Then("Capture the SessionId from the response")
	public void captureTheSessionIdFromTheResponse() throws Exception {
		String SessionId = response.jsonPath().getString("result");
		Assert.assertNotNull("SessionId should not be null", SessionId);
		prop.setProperty("SessionId", SessionId, Filepath);
		System.out.println("Extracted SessionId: " + SessionId);

	}

	@When("a POST request is sent with file and Metadata as parameters")
	public void aPOSTRequestIsSentWithFileAndMetadataAsParameters() {
		String bearerToken = prop.getProperty("BearerToken");
		File file = new File(prop.getProperty("Filepath")); // Path to the file
		response = RestAssured
				.given()
				.log().all() // Logs the request details
				.header("Authorization", "Bearer " + bearerToken)
				.header("Content-Type", "multipart/form-data")
				.multiPart("file", file) // Attach the file
				.formParam("claimId", prop.getProperty("ClaimNumber"))
				.formParam("docMetadata", "{\"docUID\":\"001\",\"documentType\":\"letter_received\",\"securityType\":\"unrestricted\","
						+ "\"status\":\"final\",\"claimNumber\":\"" + prop.getProperty("RefNumber") + "\","
						+ "\"name\":\"" + prop.getProperty("Filename") + "\",\"mimeType\":\"application/pdf\","
						+ "\"sessionID\":\"" + prop.getProperty("SessionId") + "\"}")
				.post(endpoint);

	}

	@And("Capture the UploadFileId from the response")
	public void captureTheUploadFileIdFromTheResponse() throws Exception {
		String UploadFileId = response.jsonPath().getString("uploadFileID");
		Assert.assertNotNull("UploadFileId should not be null", UploadFileId);
		prop.setProperty("UploadFileId", UploadFileId, Filepath);
		System.out.println("Extracted UploadFileId: " + UploadFileId);

	}

	@Then("Capture the timestamp of the Document uploaded")
	public void captureTheTimestampOfTheDocumentUploaded() throws Exception {
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));

		// Format the timestamp in the required format
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		String timeStamp = now.format(formatter);
		prop.setProperty("timeStamp", timeStamp, Filepath); // Store the timestamp in config.properties
		// Print the corrected timestamp
		System.out.println("\"timeStamp\": \"" + timeStamp + "\"");
		Thread.sleep(10000);

	}

	@Then("Verify the uploaded document is displayed in the response")
	public void verifyTheUploadedDocumentIsDisplayedInTheResponse() {

		String expectedFileName = prop.getProperty("Filename");
		String expectedTimeStamp = prop.getProperty("timeStamp");

		// Parse JSON response
		JsonPath jsonPath = response.jsonPath();
		List<Map<String, String>> submittedDocuments = jsonPath.getList("submittedDocuments");

		// Verify if any fileName and timeStamp match the property file values
		boolean isMatchFound = false;
		for (Map<String, String> document : submittedDocuments) {
			String fileName = document.get("fileName");
			String timeStamp = document.get("timeStamp");


// Parse the original timestamp
			ZonedDateTime zonedDateTime = ZonedDateTime.parse(timeStamp);

// Format the timestamp to the desired format
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
			String formattedTimeStamp = zonedDateTime.format(formatter);

			if (fileName.equals(expectedFileName) && formattedTimeStamp.equals(expectedTimeStamp)) {
				isMatchFound = true;
				System.out.println("Match found: fileName = " + fileName + ", timeStamp = " + formattedTimeStamp);
				break;
			}
		}

		if (!isMatchFound) {
			System.out.println("No matching fileName and timeStamp found in the response.");
			Assert.fail("No matching fileName and timeStamp found in the response.");
		}
	}

	@Then("Capture the timestamp of the message sent")
	public void captureTheTimestampOfTheMessageSent() throws Exception {
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/New_York")); // EDT timezone

// Format the timestamp in the required format
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm z");
		String timeStamp = now.format(formatter);
		prop.setProperty("MtimeStamp", timeStamp, Filepath); // Store the timestamp in config.properties

// Print the formatted timestamp
		System.out.println("\"MtimeStamp\": \"" + timeStamp + "\"");
		Thread.sleep(10000);

	}

	@Then("Verify the sent message is displayed in the response")
	public void verifyTheSentMessageIsDisplayedInTheResponse() {
		String expectedBody = "Test API";
		String expectedTimeStamp = prop.getProperty("MtimeStamp");


		// Parse JSON response
		JsonPath jsonPath = response.jsonPath();
		List<Map<String, Object>> customerMessageDetail = jsonPath.getList("result.customerMessages.customerMessageDetail");

		// Verify if any body and sendDate match the property file values
		boolean isMatchFound = false;
		for (Map<String, Object> message : customerMessageDetail) {
			String body = (String) message.get("body");
			String sendDate = (String) message.get("sendDate");

			if (body.equals(expectedBody) && sendDate.equals(expectedTimeStamp)) {
				isMatchFound = true;
				System.out.println("Match found: body = " + body + ", sendDate = " + sendDate);
				break;
			}
		}

		if (!isMatchFound) {
			System.out.println("No matching body and sendDate found in the response.");
			Assert.fail("No matching body and sendDate found in the response.");
		}

	}


	@When("the token request body has set up")
	public void theTokenRequestBodyHasSetUp() {
		requestBody = "UserId=" + prop.getProperty("UserId") + "&Password=" + prop.getProperty("Password");
	}

	@And("a Token POST request is sent")
	public void aTokenPOSTRequestIsSent() {
		response = RestAssured
				.given()
				.log().all() // Logs the request details
				.header("Content-Type", "application/x-www-form-urlencoded")
				.header("User-Agent", "Jakarta Commons-HttpClient/3.1")
				.header("Accept", "*/*")
				.header("Accept-Encoding", "gzip, deflate, br")
				.header("Connection", "keep-alive")
				.body(requestBody)
				.post(endpoint);
	}

	@Then("Capture the token value from response")
	public void captureTheTokenValueFromResponse() throws Exception {
		// Parse the XML response
		XmlPath xmlPath = new XmlPath(response.asString());
		// Extract the token from <wsibf:authHandler>
		String token = xmlPath.getString("Response.ACK.result.authentication.authHandler");
		// Assert that the token is not null
		Assert.assertNotNull("Token should not be null", token);
		// Store the token in the properties file
		prop.setProperty("authToken", token, Filepath);
		// Print the extracted token
		System.out.println("Extracted Token: " + token);
	}

	@And("the request body is configured")
	public void theRequestBodyIsConfigured() {
		requestBody = """
				<?xml version="1.0" encoding="UTF-8"?>
				<wsibf7:Form7 xmlns:wsib="http://www.wsib.on.ca" xmlns:wsibf7="http://www.wsib.on.ca/form7" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.wsib.on.ca/form7 ../Form7.xsd http://www.wsib.on.ca BasicComplexTypes.xsd "> \s
				  <wsibf7:version>1.04</wsibf7:version>
				  <wsibf7:workerInfo>
				  <!-- <wsibf7:claimNo>32010446</wsibf7:claimNo> -->
				    <wsibf7:jobTitle>SE</wsibf7:jobTitle>
				    <wsibf7:SIN>354555556</wsibf7:SIN>
				    <wsibf7:timeInPosition>12 weeks</wsibf7:timeInPosition>
				    <wsibf7:additionalWorkerAttribute>
				      <wsibf7:executive>N</wsibf7:executive>
				      <wsibf7:electedOfficial>N</wsibf7:electedOfficial>
				      <wsibf7:owner>N</wsibf7:owner>
				      <wsibf7:spouseOfOwner>N</wsibf7:spouseOfOwner>
				    </wsibf7:additionalWorkerAttribute>
				    <wsibf7:lastname>{{lastname}}</wsibf7:lastname>
				    <wsibf7:firstname>{{firstname}}</wsibf7:firstname>
				    <wsibf7:address>
				      <wsib:street>27 Greybeaver Trial</wsib:street>
				      <wsib:city>Scarbrough</wsib:city>
				      <wsib:province>ON</wsib:province>
				      <wsib:postalCode>M1c4p1</wsib:postalCode>
				    </wsibf7:address>
				    <wsibf7:collectiveAgreementCoverage>Y</wsibf7:collectiveAgreementCoverage>
				    <wsibf7:referenceNumber>123456789012345</wsibf7:referenceNumber>
				    <wsibf7:preferredLanguage>
				      <wsib:priLanguage>E</wsib:priLanguage>
				    </wsibf7:preferredLanguage>
				    <wsibf7:dateOfBirth>{{dateOfBirth}}</wsibf7:dateOfBirth>
				    <wsibf7:dateOfHire>01012000</wsibf7:dateOfHire>
				    <wsibf7:gender>M</wsibf7:gender>
				    <wsibf7:phoneNumber>(416)123-1230</wsibf7:phoneNumber>
				  </wsibf7:workerInfo>
				  <wsibf7:employerInfo>
				    <wsibf7:tradeLegalName>BYBUUUU</wsibf7:tradeLegalName>
				    <wsibf7:firmAccountNumber>
				      <wsibf7:accountNumber>{{accountNumber}}</wsibf7:accountNumber>
				    </wsibf7:firmAccountNumber>
				    <wsibf7:mailingAddress>
				      <wsib:street>wsib:street</wsib:street>
				      <wsib:city>wsib:city</wsib:city>
				      <wsib:province>ON</wsib:province>
				      <wsib:postalCode>12345</wsib:postalCode>
				    </wsibf7:mailingAddress>
				    <wsibf7:telephoneNumber>(416)123-1230</wsibf7:telephoneNumber>
				   \s
					<wsibf7:classCode>A</wsibf7:classCode>
					<wsibf7:NAICSCode>111110</wsibf7:NAICSCode>
				<!-- 	<wsibf7:rateGroupNumber>123</wsibf7:rateGroupNumber> -->
				<!--     <wsibf7:classificationUnitNumber>1234-123</wsibf7:classificationUnitNumber> -->
				    <wsibf7:businessActivityDescription>Soy Bean Farming</wsibf7:businessActivityDescription>
				    <wsibf7:over20Employees>N</wsibf7:over20Employees>
				    <wsibf7:faxNumber>(416)123-1230</wsibf7:faxNumber>
				    <wsibf7:branchAddress>
				      <wsib:street>wsib:street</wsib:street>
				      <wsib:city>wsib:city</wsib:city>
				      <wsib:province>ON</wsib:province>
				      <wsib:postalCode>12345</wsib:postalCode>
				    </wsibf7:branchAddress>
				    <wsibf7:alternateTelephone>(416)123-1230</wsibf7:alternateTelephone>
				  </wsibf7:employerInfo>
				  <wsibf7:accidentIllnessDetails>
				    <wsibf7:incidentDate>
				      <wsibf7:incidentDate>01012003</wsibf7:incidentDate>
				      <wsibf7:incidentTime>
				        <wsib:time>12:23</wsib:time>
				        <wsib:AMorPM>AM</wsib:AMorPM>
				      </wsibf7:incidentTime>
				      <wsibf7:reportedDate>01012003</wsibf7:reportedDate>
				      <wsibf7:reportedTime>
				        <wsib:time>12:23</wsib:time>
				        <wsib:AMorPM>AM</wsib:AMorPM>
				      </wsibf7:reportedTime>
				    </wsibf7:incidentDate>
				    <wsibf7:incidentReport>
				      <wsibf7:reportedPersonAndPosition>
				        <wsibf7:lineOne>wsibf7:lineOne</wsibf7:lineOne>
				        <wsibf7:lineTwo>wsibf7:lineT</wsibf7:lineTwo>
				      </wsibf7:reportedPersonAndPosition>
				      <wsibf7:phoneNumber>
				        <wsib:phoneNumber>(416)123-1230</wsib:phoneNumber>
				        <wsib:extension>123</wsib:extension>
				      </wsibf7:phoneNumber>
				    </wsibf7:incidentReport>
				    <wsibf7:accidentOccurrence>S</wsibf7:accidentOccurrence>
				    <wsibf7:accidentType>
				      <wsibf7:accidentTypes>
				        <wsibf7:standardAccidentType>STRUCK</wsibf7:standardAccidentType>
				        <wsibf7:otherAccidentType>wsibf7:otherAccidentType</wsibf7:otherAccidentType>
				      </wsibf7:accidentTypes>
				    </wsibf7:accidentType>
				    <wsibf7:injuryArea>
				      <wsibf7:injuryAreas>
				        <wsibf7:standardInjuryArea>HEAD</wsibf7:standardInjuryArea>
				        <wsibf7:otherInjuryArea>wsibf7:otherInjuryArea</wsibf7:otherInjuryArea>
				      </wsibf7:injuryAreas>
				    </wsibf7:injuryArea>
				    <wsibf7:accidentDescription>
						<wsibf7:line1>This is line1</wsibf7:line1>
						<wsibf7:line2>This is line2</wsibf7:line2>
						<wsibf7:line3>This is line3</wsibf7:line3>
						<wsibf7:line4>This is line4</wsibf7:line4>
						<wsibf7:line5>This is line5</wsibf7:line5>
						<wsibf7:line6>12345678901234567890123456789012345678901234567890123456789012345678901234567890123456</wsibf7:line6>
						<wsibf7:line7>123456789012345678901234567890123456789012345678901234567890123456789012345678901234</wsibf7:line7>
					</wsibf7:accidentDescription>
				    <wsibf7:accidentLocation>
				      <wsibf7:happenedOnEmployersPermises>Y</wsibf7:happenedOnEmployersPermises>
				      <wsibf7:Location>wsibf7:Location</wsibf7:Location>
				    </wsibf7:accidentLocation>
				    <wsibf7:outOfProvinceAccident>
				      <wsibf7:inProvinceAccident>Y</wsibf7:inProvinceAccident>
				    </wsibf7:outOfProvinceAccident>
				    <wsibf7:witnesses>
				      <wsibf7:noWitness>Y</wsibf7:noWitness>
				    </wsibf7:witnesses>
				    <wsibf7:responsibleNonEmployee>
				      <wsibf7:noOneResponsible>Y</wsibf7:noOneResponsible>
				    </wsibf7:responsibleNonEmployee>
				    <wsibf7:relatedProblem>
				      <wsibf7:notAware>Y</wsibf7:notAware>
				    </wsibf7:relatedProblem>
				    <wsibf7:submissionAttached>Y</wsibf7:submissionAttached>
				  </wsibf7:accidentIllnessDetails>
				  <wsibf7:healthcare>
				    <wsibf7:receivingHealthCare>
				      <wsibf7:notReceived>Y</wsibf7:notReceived>
				    </wsibf7:receivingHealthCare>
				    <wsibf7:workerTreatmentLocation>
				      <wsibf7:locations>
				        <wsibf7:standardLocation>ONSITE</wsibf7:standardLocation>
				        <wsibf7:otherLocation>wsibf7:otherLocation</wsibf7:otherLocation>
				      </wsibf7:locations>
				      <wsibf7:treatmentProfessionalOrFacility>
						<wsibf7:line1>12345678901234567890123456789012345678901234567890</wsibf7:line1>
						<wsibf7:line2>1234567890123456789012345678901234567890123456789012345678901234567890</wsibf7:line2>
						<wsibf7:line3>1234567890123456789012345678901234567890123456789012345678901234567890</wsibf7:line3>
					</wsibf7:treatmentProfessionalOrFacility>
				    </wsibf7:workerTreatmentLocation>
				  </wsibf7:healthcare>
				  <wsibf7:lostOrNoLostTime>
				    <wsibf7:returnToRegularJob>
				      <wsibf7:baseWageAndEmploymentInformation>
				        <wsibf7:employmentType>
				          <wsibf7:employmentTypes>
				            <wsibf7:standardEmploymentType>PERM_FULL</wsibf7:standardEmploymentType>
				            <wsibf7:otherEmploymentType>wsibf7:otherEmploymentType</wsibf7:otherEmploymentType>
				          </wsibf7:employmentTypes>
				        </wsibf7:employmentType>
				        <wsibf7:regularPayRate>
				          <wsibf7:payRate>0.0</wsibf7:payRate>
				          <wsibf7:payFrequency>
				            <wsibf7:standardPayFrequency>H</wsibf7:standardPayFrequency>
				          </wsibf7:payFrequency>
				        </wsibf7:regularPayRate>
				      </wsibf7:baseWageAndEmploymentInformation>
				      <wsibf7:declaration>
				        <wsibf7:name>wsibfx:name</wsibf7:name>
				        <wsibf7:officialTitle>wsibfx:officialTitle</wsibf7:officialTitle>
				        <wsibf7:phoneNumber>
				          <wsib:phoneNumber>(416)123-1230</wsib:phoneNumber>
				          <wsib:extension>123</wsib:extension>
				        </wsibf7:phoneNumber>
				        <wsibf7:declarationDate>01012003</wsibf7:declarationDate>
				      </wsibf7:declaration>
				    </wsibf7:returnToRegularJob>
				    <wsibf7:confirmedBy>
				      <wsibf7:byMyself>Y</wsibf7:byMyself>
				    </wsibf7:confirmedBy>
				  </wsibf7:lostOrNoLostTime>
				  <wsibf7:additionalInformation>
					<wsibf7:info>Info line 1</wsibf7:info>
					<wsibf7:info>Info line 2</wsibf7:info>
					<wsibf7:info>Info line 3</wsibf7:info>
					<wsibf7:info>Info line 4</wsibf7:info>
					<wsibf7:info>Info line 5</wsibf7:info>
					<wsibf7:info>Info line 1a</wsibf7:info>
					<wsibf7:info>Info line 2a</wsibf7:info>
					<wsibf7:info>Info line 3a</wsibf7:info>
					<wsibf7:info>Info line 4a</wsibf7:info>
					<wsibf7:info>Info line 5a</wsibf7:info>
					<wsibf7:info>Info line 1</wsibf7:info>
					<wsibf7:info>Info line 2</wsibf7:info>
					<wsibf7:info>Info line 3</wsibf7:info>
					<wsibf7:info>Info line 4</wsibf7:info>
					<wsibf7:info>Info line 5</wsibf7:info>
					<wsibf7:info>Info line 1a</wsibf7:info>
					<wsibf7:info>Info line 2a</wsibf7:info>
					<wsibf7:info>Info line 3a</wsibf7:info>
					<wsibf7:info>Info line 4a</wsibf7:info>
					<wsibf7:info>Info line 5a</wsibf7:info>
					<wsibf7:info>Info line 1</wsibf7:info>
					<wsibf7:info>Info line 2</wsibf7:info>
					<wsibf7:info>Info line 3</wsibf7:info>
					<wsibf7:info>Info line 4</wsibf7:info>
					<wsibf7:info>Info line 5</wsibf7:info>
					<wsibf7:info>Info line 1a</wsibf7:info>
					<wsibf7:info>Info line 2a</wsibf7:info>
					<wsibf7:info>Info line 3a</wsibf7:info>
					<wsibf7:info>Info line 4a</wsibf7:info>
					<wsibf7:info>Info line 5a</wsibf7:info>
					<wsibf7:info>Info line 1</wsibf7:info>
					<wsibf7:info>Info line 2</wsibf7:info>
					<wsibf7:info>Info line 3</wsibf7:info>
					<wsibf7:info>Info line 4</wsibf7:info>
					<wsibf7:info>Info line 5</wsibf7:info>
					<wsibf7:info>Info line 1a</wsibf7:info>
					<wsibf7:info>Info line 2a</wsibf7:info>
					<wsibf7:info>Info line 3a</wsibf7:info>
					<wsibf7:info>Info line 4a</wsibf7:info>
					<wsibf7:info>Info line 5a</wsibf7:info>
					<wsibf7:info>Info line 5a1</wsibf7:info>
					<wsibf7:info>Info line 5a2</wsibf7:info>
					<wsibf7:info>Info line 5a3</wsibf7:info>
					<wsibf7:info>Info line 5a4</wsibf7:info>
				  </wsibf7:additionalInformation>
				  <wsibf7:wsibReserved>
				    <wsib:reservedField1>wsib:reservedField1</wsib:reservedField1>
				    <wsib:reservedField2>wsib:reservedField2</wsib:reservedField2>
				  </wsibf7:wsibReserved>
				</wsibf7:Form7>
				     
					 """;
	}

	@When("a POST request is sent with XML body")
	public void aPOSTRequestIsSentWithXMLBody() throws IOException {
		RestAssured.useRelaxedHTTPSValidation();
		String accountNumber = Hooks.currentRow.get().get("accountNumber");
		String firstname = "First" + generateRandomText(5);
		String lastname = "Last" + generateRandomText(5);
		String dateOfBirth = Hooks.currentRow.get().get("DateofBirth").replace("/", "");
		// Update the current row data with the new values
		Map<String, String> currentRowData = Hooks.currentRow.get();
		currentRowData.put("First Name", firstname);
		currentRowData.put("Last Name", lastname);
		String sheetName = Hooks.sheetName;
		ExcelUtils.updateExcelData(sheetName, currentRowData);
		Hooks.currentRow.set(currentRowData);

		requestBody = requestBody.replace("{{accountNumber}}", accountNumber)
				.replace("{{firstname}}", firstname)
				.replace("{{lastname}}", lastname)
				.replace("{{dateOfBirth}}", dateOfBirth);
		response = RestAssured
				.given()
				.queryParam("authHandler", prop.getProperty("authToken"))
				.log().all() // Logs the request details
				.header("Content-Type", "text/plain; charset=ISO-8859-1")
				.header("Cookie", "$Version=0; JSESSIONID=0000" + prop.getProperty("authToken")+ ":-1; $Path=/")
				.header("User-Agent", "Jakarta Commons-HttpClient/3.1")
				.body(requestBody)
				.post(endpoint);

	}

	@Then("the response status code and confirmation number should be validated")
	public void theResponseStatusCodeAndConfirmationNumberShouldBeValidated() {
		int statusCode = response.getStatusCode();
		assertThat(statusCode, is(200));
		String confirmationNumber = response.xmlPath().getString("Response.ACK.result.submission.confirmationNumber");
		assertThat("Confirmation number should not be null", confirmationNumber, notNullValue());
		System.out.println("Extracted Confirmation Number: " + confirmationNumber);
	}
}
