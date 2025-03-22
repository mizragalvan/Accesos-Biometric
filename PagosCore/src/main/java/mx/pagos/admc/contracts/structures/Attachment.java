/**
 * 
 */
package mx.pagos.admc.contracts.structures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mizraim
 *
 */
public class Attachment implements Serializable {

	/**
	 * Default Serialization
	 */
	private static final long serialVersionUID = -7224797614882084934L;

	private Integer idRequisition;
	private String businessReasonMonitoringPlan;
	private String attatchmentOthersName;
	private Boolean attatchmentDeliverables = false;
	private Boolean attchmtServiceLevelsMeasuring = false;
	private Boolean attatchmentPenalty = false;
	private Boolean attatchmentScalingMatrix = false;
	private Boolean attatchmentCompensation = false;
	private Boolean attchmtBusinessMonitoringPlan = false;
	private Boolean attchmtImssInfoDeliveryReqrmts = false;
	private Boolean attatchmentInformationSecurity = false;
    private List<FileUploadInfo> documentsAttachment = new ArrayList<FileUploadInfo>();

	/**
	 * @return the businessReasonMonitoringPlan
	 */
	public String getBusinessReasonMonitoringPlan() {
		return businessReasonMonitoringPlan;
	}

	/**
	 * @param businessReasonMonitoringPlan the businessReasonMonitoringPlan to set
	 */
	public void setBusinessReasonMonitoringPlan(String businessReasonMonitoringPlan) {
		this.businessReasonMonitoringPlan = businessReasonMonitoringPlan;
	}

	/**
	 * @return the attatchmentOthersName
	 */
	public String getAttatchmentOthersName() {
		return attatchmentOthersName;
	}

	/**
	 * @param attatchmentOthersName the attatchmentOthersName to set
	 */
	public void setAttatchmentOthersName(String attatchmentOthersName) {
		this.attatchmentOthersName = attatchmentOthersName;
	}

	/**
	 * @return the attatchmentDeliverables
	 */
	public Boolean getAttatchmentDeliverables() {
		return attatchmentDeliverables;
	}

	/**
	 * @param attatchmentDeliverables the attatchmentDeliverables to set
	 */
	public void setAttatchmentDeliverables(Boolean attatchmentDeliverables) {
		this.attatchmentDeliverables = attatchmentDeliverables;
	}

	/**
	 * @return the attchmtServiceLevelsMeasuring
	 */
	public Boolean getAttchmtServiceLevelsMeasuring() {
		return attchmtServiceLevelsMeasuring;
	}

	/**
	 * @param attchmtServiceLevelsMeasuring the attchmtServiceLevelsMeasuring to set
	 */
	public void setAttchmtServiceLevelsMeasuring(Boolean attchmtServiceLevelsMeasuring) {
		this.attchmtServiceLevelsMeasuring = attchmtServiceLevelsMeasuring;
	}

	/**
	 * @return the attatchmentPenalty
	 */
	public Boolean getAttatchmentPenalty() {
		return attatchmentPenalty;
	}

	/**
	 * @param attatchmentPenalty the attatchmentPenalty to set
	 */
	public void setAttatchmentPenalty(Boolean attatchmentPenalty) {
		this.attatchmentPenalty = attatchmentPenalty;
	}

	/**
	 * @return the attatchmentScalingMatrix
	 */
	public Boolean getAttatchmentScalingMatrix() {
		return attatchmentScalingMatrix;
	}

	/**
	 * @param attatchmentScalingMatrix the attatchmentScalingMatrix to set
	 */
	public void setAttatchmentScalingMatrix(Boolean attatchmentScalingMatrix) {
		this.attatchmentScalingMatrix = attatchmentScalingMatrix;
	}

	/**
	 * @return the attatchmentCompensation
	 */
	public Boolean getAttatchmentCompensation() {
		return attatchmentCompensation;
	}

	/**
	 * @param attatchmentCompensation the attatchmentCompensation to set
	 */
	public void setAttatchmentCompensation(Boolean attatchmentCompensation) {
		this.attatchmentCompensation = attatchmentCompensation;
	}

	/**
	 * @return the attchmtBusinessMonitoringPlan
	 */
	public Boolean getAttchmtBusinessMonitoringPlan() {
		return attchmtBusinessMonitoringPlan;
	}

	/**
	 * @param attchmtBusinessMonitoringPlan the attchmtBusinessMonitoringPlan to set
	 */
	public void setAttchmtBusinessMonitoringPlan(Boolean attchmtBusinessMonitoringPlan) {
		this.attchmtBusinessMonitoringPlan = attchmtBusinessMonitoringPlan;
	}

	/**
	 * @return the attchmtImssInfoDeliveryReqrmts
	 */
	public Boolean getAttchmtImssInfoDeliveryReqrmts() {
		return attchmtImssInfoDeliveryReqrmts;
	}

	/**
	 * @param attchmtImssInfoDeliveryReqrmts the attchmtImssInfoDeliveryReqrmts to
	 *                                       set
	 */
	public void setAttchmtImssInfoDeliveryReqrmts(Boolean attchmtImssInfoDeliveryReqrmts) {
		this.attchmtImssInfoDeliveryReqrmts = attchmtImssInfoDeliveryReqrmts;
	}

	/**
	 * @return the attatchmentInformationSecurity
	 */
	public Boolean getAttatchmentInformationSecurity() {
		return attatchmentInformationSecurity;
	}

	/**
	 * @param attatchmentInformationSecurity the attatchmentInformationSecurity to
	 *                                       set
	 */
	public void setAttatchmentInformationSecurity(Boolean attatchmentInformationSecurity) {
		this.attatchmentInformationSecurity = attatchmentInformationSecurity;
	}

	/**
	 * @return the attatchmentOthers
	 */
	public Boolean getAttatchmentOthers() {
		return attatchmentOthers;
	}

	/**
	 * @param attatchmentOthers the attatchmentOthers to set
	 */
	public void setAttatchmentOthers(Boolean attatchmentOthers) {
		this.attatchmentOthers = attatchmentOthers;
	}

	private Boolean attatchmentOthers = false;

	/**
	 * @return the idRequisition
	 */
	public Integer getIdRequisition() {
		return idRequisition;
	}

	/**
	 * @param idRequisition the idRequisition to set
	 */
	public void setIdRequisition(Integer idRequisition) {
		this.idRequisition = idRequisition;
	}

	/**
	 * @return the documentsAttachment
	 */
	public List<FileUploadInfo> getDocumentsAttachment() {
		return documentsAttachment;
	}

	/**
	 * @param documentsAttachment the documentsAttachment to set
	 */
	public void setDocumentsAttachment(List<FileUploadInfo> documentsAttachment) {
		this.documentsAttachment = documentsAttachment;
	}

}
