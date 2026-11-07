package pages.Guidewire;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;

public class GWRecoveryPage extends BaseDriverClass {

    @FindBy(id = "Claim:MenuLinks:Claim_RTWRecMenu_WC")
    private WebElement RecoveryLink;
    @FindBy(id="RTWRecMenu_WC:MenuLinks:RTWRecMenu_WC_RecoveryDetail_WC")
    private WebElement RecoveryDetail_WC;
    @FindBy(id="RecoveryDetail_WC:ClaimWorkplanScreen:Edit")
    private WebElement RecoveryEditLink;
    @FindBy(id = "RecoveryDetail_WC:ClaimWorkplanScreen:Goals_ProposedWCLV:0:Goal")
    private WebElement Proposed_RTW_Goal;
    @FindBy(id = "RecoveryDetail_WC:ClaimWorkplanScreen:Goals_ProposedWCLV:1:Goal")
    private WebElement Proposed_Interim_RTW_Goal;
    @FindBy(id = "RecoveryDetail_WC:ClaimWorkplanScreen:Goals_ProposedWCLV:2:Goal")
    private WebElement Proposed_Recovery_Goal;
    @FindBy(id = "RecoveryDetail_WC:ClaimWorkplanScreen:Goals_ProposedWCLV:0:GoalDate")
    private WebElement ProposedDate_RTW_Goal;
    @FindBy(id = "RecoveryDetail_WC:ClaimWorkplanScreen:Goals_ProposedWCLV:1:GoalDate")
    private WebElement ProposedDate_Interim_RTW_Goal;
    @FindBy(id = "RecoveryDetail_WC:ClaimWorkplanScreen:Goals_ProposedWCLV:2:GoalDate")
    private WebElement ProposedDate_Recovery_Goal;
    @FindBy(id = "RecoveryDetail_WC:ClaimWorkplanScreen:CUrrentStatus:WSIB_ICAPRecStatusLV:0:Status")
    private WebElement Current_RTW_Status;
    @FindBy(id = "RecoveryDetail_WC:ClaimWorkplanScreen:CUrrentStatus:WSIB_ICAPRecStatusLV:1:Status")
    private WebElement Current_Recovery_Status;
    @FindBy(id = "RecoveryDetail_WC:ClaimWorkplanScreen:WRStatus:WSIB_ICAPRecActualOutcomeLV:0:Status")
    private WebElement Actual_RTW_Goal;
    @FindBy(id = "RecoveryDetail_WC:ClaimWorkplanScreen:WRStatus:WSIB_ICAPRecActualOutcomeLV:1:Status")
    private WebElement Actual_Recovery_Goal;
    @FindBy(id = "RecoveryDetail_WC:ClaimWorkplanScreen:WRStatus:WSIB_ICAPRecActualOutcomeLV:0:ActualDate")
    private WebElement Actual_RTW_Date;
    @FindBy(id = "RecoveryDetail_WC:ClaimWorkplanScreen:WRStatus:WSIB_ICAPRecActualOutcomeLV:1:ActualDate")
    private WebElement Actual_Recovery_Date;
    @FindBy(id = "RecoveryDetail_WC:ClaimWorkplanScreen:CompleteMedicationButton")
    private WebElement RecoverLinkUpdateButton;

    @FindBy(xpath = "//*[contains(@id, 'DevicePanelRef:WSIB_LatestDeviceDetailsLV_tb:ToolbarButton')]")
    private WebElement ViewDeviceDetailsButton;
    @FindBy(xpath = "//*[contains(@id, 'WSIB_DeviceDetailsListDV:WSIB_LatestDeviceDetailsLV_tb:Add')]")
    private WebElement DeviceAddButton;
    @FindBy(xpath = "//*[contains(@id, 'WSIB_DeviceDetailsCV:WSIB_DeviceEntitlmentDetailInputSet:DeviceECategory')]")
    private WebElement DeviceECategory;
    @FindBy(xpath = "//*[contains(@id, 'WSIB_DeviceDetailsCV:WSIB_DeviceEntitlmentDetailInputSet:SubValuesDevicesID')]")
    private WebElement DeviceType;
    @FindBy(xpath = "//*[contains(@id, 'WSIB_DeviceDetailsCV:WSIB_DeviceEntitlmentDetailInputSet:DeviceStatusID')]")
    private WebElement DeviceStatus;
    @FindBy(xpath = "//*[contains(@id, 'WSIB_DeviceDetailsCV:WSIB_DeviceEntitlmentDetailInputSet:DeviceServiceTypeID')]")
    private WebElement DeviceServiceType;
    @FindBy(xpath = "//*[contains(@id, 'WSIB_DeviceEntitlmentDetailInputSet:StartDateID')]")
    private WebElement StartDate;
    @FindBy(xpath = "//*[contains(@id, 'WSIB_DeviceEntitlmentDetailInputSet:EndDateID')]")
    private WebElement EndDate;
    @FindBy(xpath = "//*[contains(@id, 'WSIB_DeviceEntitlmentDetailInputSet:DecisionDateID')]")
    private WebElement DecisionDate;
    @FindBy(id= "WSIB_DeviceDetailsPopup:ToolbarButton")
    private WebElement DeviceUpdateButton;
    @FindBy(xpath = "//*[contains(@id, 'WSIB_DeviceNotePopup:NoteDetailDV:PurposeOfReviewID')]")
    private WebElement PurposeOfReview;
    @FindBy(xpath = "//*[contains(@id, 'WSIB_DeviceNotePopup:NoteDetailDV:Body')]")
    private WebElement ReviewBody;
    @FindBy(id= "WSIB_DeviceNotePopup:UpdateButton")
    private WebElement DeviceFinishButton;

    //**********Independent Living Allowance
    @FindBy(id= "RecoveryDetail_WC:ClaimWorkplanScreen:CardsCV:ILA:WSIB_ILADetailsLV_tb:Add")
    private WebElement ILAAddButton;
    @FindBy(xpath = "//*[contains(@id, 'WSIB_ILADetailsLV:0:ILAStatus')]")
    private WebElement ILAStatus;

    @FindBy(xpath = "//*[contains(@id, 'ClaimWorkplanScreen:CardsCV:ILA:WSIB_ILADetailsLV:0:PCAFromDate')]")
    private WebElement ILAFromDate;
    @FindBy(xpath = "//*[contains(@id, 'ClaimWorkplanScreen:CardsCV:ILA:WSIB_ILADetailsLV:0:PCAToDate')]")
    private WebElement ILAToDate;

    //******** Personal Care Allowance

    @FindBy(id= "RecoveryDetail_WC:ClaimWorkplanScreen:CardsCV:PCA:WSIB_PCADetailsLV_tb:Add")
    private WebElement PCAAddButton;
    @FindBy(xpath = "//*[contains(@id, 'PCA:WSIB_PCADetailsLV:0:PCAAssesmentType')]")
    private WebElement PCAAssesmentType;
    @FindBy(xpath = "//*[contains(@id, 'PCA:WSIB_PCADetailsLV:0:AssessmentOutcome')]")
    private WebElement PCAAssessmentOutcome;
    @FindBy(xpath = "//*[contains(@id, 'ClaimWorkplanScreen:CardsCV:PCA:WSIB_PCADetailsLV:0:PCAFromDate')]")
    private WebElement PCAFromDate;
    @FindBy(xpath = "//*[contains(@id, 'ClaimWorkplanScreen:CardsCV:PCA:WSIB_PCADetailsLV:0:PCAToDate')]")
    private WebElement PCAToDate;
    @FindBy(xpath = "//*[contains(@id, 'ClaimWorkplanScreen:CardsCV:PCA:WSIB_PCADetailsLV:0:PCAReassesRequired')]")
    private WebElement PCAReassesRequired;
    @FindBy(xpath = "//*[contains(@id, 'ClaimWorkplanScreen:CardsCV:PCA:WSIB_PCADetailsLV:0:PCAReassessmentDate')]")
    private WebElement PCAReassessmentDate;


    //******** Treatment Details
    @FindBy(xpath = "//*[contains(@id, 'TreatmentPanelRef:WSIB_TreatmentDetailsLV_tb:ToolbarButton')]")
    private WebElement TreatmentDetailsButton;
    @FindBy(xpath = "//*[contains(@id, 'TreatmentDetailsListDV:WSIB_TreatmentDetailsTopLV_tb:Add')]")
    private WebElement TreatmentAdd;
    @FindBy(xpath = "//*[contains(@id, 'WSIB_TreatmentDetailsCV:WSIB_TreatmentEntitlmentDetailInputSet:TreatmentType')]")
    private WebElement TreatmentType;
    @FindBy(xpath = "//*[contains(@id, 'WSIB_TreatmentEntitlmentDetailInputSet:TreatmentStatus')]")
    private WebElement TreatmentStatus;
    @FindBy(xpath = "//*[contains(@id, 'WSIB_TreatmentEntitlmentDetailInputSet:TreatmentFromDate')]")
    private WebElement TreatmentFromDate;
    @FindBy(xpath = "//*[contains(@id, 'WSIB_TreatmentEntitlmentDetailInputSet:TreatmentToDate')]")
    private WebElement TreatmentToDate;
    @FindBy(xpath = "//*[contains(@id, 'WSIB_TreatmentEntitlmentDetailInputSet:FreqNumID')]")
    private WebElement FreqNumID;
    @FindBy(xpath = "//*[contains(@id, 'WSIB_TreatmentEntitlmentDetailInputSet:EntitlementDUnit')]")
    private WebElement EntitlementDUnit;
    @FindBy(xpath = "//*[contains(@id, 'WSIB_TreatmentDetailsPopup:ToolbarButton')]")
    private WebElement TreatmentDetailsUpdate;
    @FindBy(xpath = "//*[contains(@id, 'WSIB_TreatmentNotePopup:NoteDetailDV:PurposeOfReviewID')]")
    private WebElement PurposeOfReviewTreatment;
    @FindBy(xpath = "//*[contains(@id, 'WSIB_TreatmentNotePopup:NoteDetailDV:Body')]")
    private WebElement BodyTreatment;
    @FindBy(xpath = "//*[contains(@id, 'WSIB_TreatmentNotePopup:UpdateButton')]")
    private WebElement FinishTreatment;

    //******** Medication Details
    @FindBy(xpath = "//*[contains(@id, 'RecoveryDetail_WC:ClaimWorkplanScreen:CardsCV:MedicationTab')]")
    private WebElement MedicationTab;
    @FindBy(xpath = "//*[contains(@id, 'WSIB_ExceptionMedicationListDV:WSIB_ExceptionMedicationLV_tb:Add')]")
    private WebElement MedicationAdd;
    @FindBy(xpath = "//*[contains(@id, 'WSIB_ExceptionMedicationCV:MedicationTypeID')]")
    private WebElement MedicationType;
    @FindBy(xpath = "//*[contains(@id, 'WSIB_ExceptionMedicationCV:MedicationStatusID')]")
    private WebElement MedicationStatus;
    @FindBy(xpath = "//*[contains(@id, 'WSIB_ExceptionMedicationCV:SearchMedicinesID')]")
    private WebElement SearchMedicines;
    @FindBy(xpath = "//*[contains(@id, 'EntitlementDecisionDetailPopup:EntitlementDecisionDV:MedicinesListID')]")
    private WebElement MedicinesList;
    @FindBy(xpath = "//*[contains(@id, 'WSIB_ExceptionMedicationListDV:WSIB_ExceptionMedicationCV:StartDate_ID')]")
    private WebElement MedicinesStartDate;
    @FindBy(xpath = "//*[contains(@id, 'WSIB_ExceptionMedicationListDV:WSIB_ExceptionMedicationCV:EndDate_ID')]")
    private WebElement MedicationEndDate;
    @FindBy(xpath = "//*[contains(@id, 'MedicationNotePopup:NoteDetailDV:PurposeOfReview')]")
    private WebElement MedicationPurposeOfReview;
    @FindBy(xpath = "//*[contains(@id, 'MedicationNotePopup:NoteDetailDV:Body')]")
    private WebElement MedicationBody;
    @FindBy(xpath = "//*[contains(@id, 'MedicationNotePopup:UpdateButton')]")
    private WebElement MedicationFinish;

    //********* NEL Data Set up and Payments
    @FindBy(xpath = "//*[contains(@id, 'Claim:MenuLinks:Claim_Inc2EntitiesGroup')]")
    private WebElement Claim_Inc2EntitiesGroup;
    @FindBy(xpath = "//*[contains(@id, 'Inc2EntitiesGroup:MenuLinks:Inc2EntitiesGroup_Inc2Sequence')]")
    private WebElement Inc2EntitiesGroup_Inc2Sequence;
    @FindBy(xpath = "//*[contains(@id, 'Inc2Sequence:1:exposure')]")
    private WebElement exposureLOE;
    @FindBy(xpath = "//*[contains(@id, 'Inc2Sequence:Edit')]")
    private WebElement Inc2SequenceEDIT;
    @FindBy(xpath = "//*[contains(@id, 'Inc2Sequence:Add')]")
    private WebElement Inc2SequenceAdd;
    @FindBy(xpath = "//*[contains(@id, 'Inc2Sequence:inc2SequencesRowIterator:0:SequenceSourceID')]")
    private WebElement SequenceSourceID;
    @FindBy(xpath = "//*[contains(@id, 'Inc2Sequence:inc2SequencesRowIterator:0:BenefitType')]")
    private WebElement BenefitType;
    @FindBy(xpath = "//*[contains(@id, 'Inc2Sequence:inc2SequencesRowIterator:0:SequenceStatus')]")
    private WebElement SequenceStatus;
    @FindBy(xpath = "//*[contains(@id, 'Inc2Sequence:inc2SequencesRowIterator:0:SequenceFromDate')]")
    private WebElement SequenceFromDate;
    @FindBy(xpath = "//*[contains(@id, 'Inc2Sequence:inc2SequencesRowIterator:0:EntitlementPercent')]")
    private WebElement EntitlementPercent;
    @FindBy(xpath = "//*[contains(@id, 'Inc2Sequence:Update')]")
    private WebElement Inc2SequenceUpdate;
    // Check Payments
    @FindBy(xpath = "//*[contains(@id, 'Inc2EntitiesGroup:MenuLinks:Inc2EntitiesGroup_Inc2Cheque')]")
    private WebElement Inc2EntitiesGroup_Inc2Cheque;
    @FindBy(xpath = "//*[contains(@id, 'Inc2Cheque:Edit')]")
    private WebElement Inc2ChequeEdit;
    @FindBy(xpath = "//*[contains(@id, 'Inc2Cheque:Add')]")
    private WebElement Inc2ChequeAdd;
    @FindBy(xpath = "//*[contains(@id, 'Inc2Cheque:0:ChequeSourceID')]")
    private WebElement ChequeSourceID;
    @FindBy(xpath = "//*[contains(@id, 'Inc2Cheque:0:Payee')]")
    private WebElement Inc2ChequePayee;
    @FindBy(xpath = "//*[contains(@id, 'Inc2Cheque:0:PayTo')]")
    private WebElement Inc2ChequePayTo;
    @FindBy(xpath = "//*[contains(@id, 'Inc2Cheque:0:Status')]")
    private WebElement Inc2ChequeStatus;
    @FindBy(xpath = "//*[contains(@id, 'Inc2Cheque:0:Amount')]")
    private WebElement Inc2ChequeAmount;
    @FindBy(xpath = "//*[contains(@id, 'Inc2Cheque:0:IssuedDate')]")
    private WebElement Inc2ChequeIssuedDate;
    @FindBy(xpath = "//*[contains(@id, 'Inc2Cheque:Update')]")
    private WebElement Inc2ChequeUpdate;
    /// Inc2ChequePayments

    @FindBy(xpath = "//*[contains(@id, 'Inc2EntitiesGroup:MenuLinks:Inc2EntitiesGroup_Inc2ChequePayment')]")
    private WebElement Inc2ChequePayment;
    @FindBy(xpath = "//*[contains(@id, 'Inc2ChequePayment:Edit')]")
    private WebElement Inc2ChequePaymentEdit;
    @FindBy(xpath = "//*[contains(@id, 'Inc2ChequePayment:Add')]")
    private WebElement Inc2ChequePaymentAdd;
    @FindBy(xpath = "//*[contains(@id, 'Inc2ChequePayment:inc2PaymentsRowIterator:0:CostCategory')]")

    private WebElement Inc2ChequePaymentCostCategory;
    @FindBy(xpath = "//*[contains(@id, 'Inc2ChequePayment:inc2PaymentsRowIterator:0:CostType')]")
    private WebElement Inc2ChequePaymentCostType;
    @FindBy(xpath = "//*[contains(@id, 'Inc2ChequePayment:inc2PaymentsRowIterator:0:Amount')]")
    private WebElement Inc2ChequePaymentAmount;
    @FindBy(xpath = "//*[contains(@id, 'Inc2ChequePayment:inc2PaymentsRowIterator:0:PayFromDate')]")
    private WebElement Inc2ChequePaymentPayFromDate;
    @FindBy(xpath = "//*[contains(@id, 'Inc2ChequePayment:inc2PaymentsRowIterator:0:PayToDate')]")
    private WebElement Inc2ChequePaymentPayToDate;
    @FindBy(xpath = "//*[contains(@id, 'Inc2ChequePayment:Update')]")
    private WebElement Inc2ChequePaymentUpdate;

   // *************Clothing Allowance
   @FindBy(xpath = "//*[contains(@id, 'ClothingAllowances:WSIB_ClothingAllowanceLV_tb:Add')]")
   private WebElement ClothingAllowanceAdd;
    @FindBy(xpath = "//*[contains(@id, 'WSIB_ClothingAllowanceLV:0:ClothingAllowance_Approved_true')]")
    private WebElement ClothingAllowanceApproved_True;
    @FindBy(xpath = "//*[contains(@id, 'WSIB_ClothingAllowanceLV:0:ClothingAllowance_DecisionDate')]")
    private WebElement ClothingAllowance_DecisionDate;



    public GWRecoveryPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        PageFactory.initElements(driver, this);
    }

    public boolean setupRTWData(String proposedRtwGoal, String proposedInterimRtwGoal, String proposedRecoveryGoal, String proposedDateRtwGoal,
                                String proposedDateInterimRtwGoal, String proposedDateRecoveryGoal, String currentRtwStatus,
                                String currentRecoveryStatus, String actualRtwGoal, String actualRecoveryGoal, String actualRtwDate,
                                String actualRecoveryDate) {
        Click(RecoveryLink);
        Click(RecoveryDetail_WC);
        Click(RecoveryEditLink);
        GWSelect(Proposed_RTW_Goal, proposedRtwGoal);
        GWSelect(Proposed_Interim_RTW_Goal, proposedInterimRtwGoal);
        GWSelect(Proposed_Recovery_Goal, proposedRecoveryGoal);
        Enter(ProposedDate_RTW_Goal, proposedDateRtwGoal);
        Enter(ProposedDate_Interim_RTW_Goal, proposedDateInterimRtwGoal);
        Enter(ProposedDate_Recovery_Goal, proposedDateRecoveryGoal);
        GWSelect(Current_RTW_Status, currentRtwStatus);
        GWSelect(Current_Recovery_Status, currentRecoveryStatus);
        GWSelect(Actual_RTW_Goal, actualRtwGoal);
        GWSelect(Actual_Recovery_Goal, actualRecoveryGoal);
        Enter(Actual_RTW_Date, actualRtwDate);
        Enter(Actual_Recovery_Date, actualRecoveryDate);
        Click(RecoverLinkUpdateButton);
        return Proposed_RTW_Goal.getText().equalsIgnoreCase(proposedRtwGoal);
    }

    public boolean setupDeviceData(String deviceCategory, String deviceType, String deviceStatus, String deviceServiceType, String deviceStartDate,
                                   String deviceEndDate, String deviceDecisionDate, String deviceReview,
                                   String rationale) {
        Click(RecoveryLink);
        Click(RecoveryDetail_WC);
        Click(RecoveryEditLink);
        Click(ViewDeviceDetailsButton);
        Click(DeviceAddButton);
        GWSelect(DeviceECategory, deviceCategory);
        GWSelect(DeviceType, deviceType);
        GWSelect(DeviceStatus, deviceStatus);
        GWSelect(DeviceServiceType, deviceServiceType);
        Enter(StartDate, deviceStartDate);
        Enter(EndDate, deviceEndDate);
        Enter(DecisionDate, deviceDecisionDate);
        Click(DeviceUpdateButton);
        GWSelect(PurposeOfReview, deviceReview);
        Enter(ReviewBody, rationale);
        Click(DeviceFinishButton);
        return true;
    }

    public boolean setupILAData(String ilaStatus, String pcaFromDate, String pcaToDate) {
        Click(RecoveryLink);
        Click(RecoveryDetail_WC);
        Click(RecoveryEditLink);
        Click(ILAAddButton);
        GWSelect(ILAStatus, ilaStatus);
        Enter(ILAFromDate, pcaFromDate);
        Enter(ILAToDate, pcaToDate);
        Click(RecoverLinkUpdateButton);
        return true;
    }


    public boolean setupPCAData(String pcaAssesmentType, String pcaAssessmentOutcome, String pcaFromDate, String pcaToDate, String pcaReassesRequired, String pcaReassessmentDate) {
        Click(RecoveryLink);
        Click(RecoveryDetail_WC);
        Click(RecoveryEditLink);
        Click(PCAAddButton);
        GWSelect(PCAAssesmentType, pcaAssesmentType);
        GWSelect(PCAAssessmentOutcome, pcaAssessmentOutcome);
        Enter(PCAFromDate, pcaFromDate);
        Enter(PCAToDate, pcaToDate);
        GWSelect(PCAReassesRequired, pcaReassesRequired);
        if(pcaReassesRequired.equalsIgnoreCase("Yes"))
        {
        Enter(PCAReassessmentDate, pcaReassessmentDate);
        }
        Click(RecoverLinkUpdateButton);
        return true;


    }

    public boolean setupTreatmentData(String treatmentType, String treatmentStatus, String treatmentStartDate, String treatmentEndDate, String treatmentFreqNum, String treatmentEntitlementDUnit, String deviceReview, String rationale) {
        Click(RecoveryLink);
        Click(RecoveryDetail_WC);
        Click(RecoveryEditLink);
        Click(TreatmentDetailsButton);
        Click(TreatmentAdd);
        GWSelect(TreatmentType, treatmentType);
        GWSelect(TreatmentStatus, treatmentStatus);
        Enter(TreatmentFromDate, treatmentStartDate);
        Enter(TreatmentToDate, treatmentEndDate);
        Enter(FreqNumID, treatmentFreqNum);
        GWSelect(EntitlementDUnit, treatmentEntitlementDUnit);
        Click(TreatmentDetailsUpdate);
        GWSelect(PurposeOfReviewTreatment, deviceReview);
        Enter(BodyTreatment, rationale);
        Click(FinishTreatment);
        return true;
    }

    public boolean setupMedicationData(String medicationType, String medicationStatus, String formulary, String medicationStartDate,
                                       String medicationEndDate, String deviceReview, String rationale) {
        Click(RecoveryLink);
        Click(RecoveryDetail_WC);
        Click(RecoveryEditLink);
        Click(MedicationTab);
        Click(MedicationAdd);
        GWSelect(MedicationType, medicationType);
        GWSelect(MedicationStatus, medicationStatus);
        Click(SearchMedicines);
        GWSelect(MedicinesList, formulary);
        Enter(MedicinesStartDate, medicationStartDate);
        Enter(MedicationEndDate, medicationEndDate);
        Click(RecoverLinkUpdateButton);
        GWSelect(MedicationPurposeOfReview, deviceReview);
        Enter(MedicationBody, rationale);
        Click(MedicationFinish);
        return true;
    }

    public boolean setupPaymentsData(String checkSourceId, String payee, String payTo, String status, String tAmount,
                                     String issuedDate, String costCategory, String costType, String amount,
                                     String payFromDate, String payToDate) {
        Click(Claim_Inc2EntitiesGroup);
        Click(Inc2EntitiesGroup_Inc2Cheque);
        Click(Inc2ChequeEdit);
        Click(Inc2ChequeAdd);
        Enter(ChequeSourceID, checkSourceId);
        GWSelect(Inc2ChequePayee, payee);
        Enter(Inc2ChequePayTo, payTo);
        GWSelect(Inc2ChequeStatus, status);
        Enter(Inc2ChequeAmount, tAmount);
        Enter(Inc2ChequeIssuedDate, issuedDate);
        Click(Inc2ChequeUpdate);

        Click(Inc2ChequePayment);
        Click(Inc2ChequePaymentEdit);
        Click(Inc2ChequePaymentAdd);
        GWSelect(Inc2ChequePaymentCostCategory, costCategory);
        GWSelect(Inc2ChequePaymentCostType, costType);
        Enter(Inc2ChequePaymentAmount, amount);
        Enter(Inc2ChequePaymentPayFromDate, payFromDate);
        Enter(Inc2ChequePaymentPayToDate, payToDate);
        Click(Inc2ChequePaymentUpdate);
        return true;

    }

    public boolean setupClothingAllowanceData(String clothingAllowanceApproved, String clothingAllowanceDecisionDate) {

        Click(Claim_Inc2EntitiesGroup);
        Click(Inc2EntitiesGroup_Inc2Sequence);
        Click(exposureLOE);
        Click(Inc2SequenceEDIT);
        Click(Inc2SequenceAdd);
        Enter(SequenceSourceID, "67687688");
        GWSelect(BenefitType, "Pension");
        GWSelect(SequenceStatus, "Active");
        Enter(SequenceFromDate, "01/01/2026");
        Enter(EntitlementPercent, "10");
        Click(Inc2SequenceUpdate);

        Click(RecoveryLink);
        Click(RecoveryDetail_WC);
        Click(RecoveryEditLink);
        Click(ClothingAllowanceAdd);
        Click(ClothingAllowanceApproved_True);
        Enter(ClothingAllowance_DecisionDate, clothingAllowanceDecisionDate);
        Click(RecoverLinkUpdateButton);
        return true;

    }
}
