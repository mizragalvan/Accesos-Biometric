/**
 * 
 */
package mx.pagos.admc.contracts.structures;

import java.io.Serializable;

import mx.pagos.security.structures.User;

/**
 * @author Mizraim
 */
public class RequisitionComplete implements Serializable {

	/**
	 * Selialization generated
	 */
	private static final long serialVersionUID = -3080159545973782242L;

	private Integer idRequisition;
	private User applicant;
	private RequisitionsPartOneAndTwo requisitionsPartOneAndTwo;
	private RequisitionsPartThree requisitionsPartThree;
	private RequisitionsPartFour requisitionsPartFour;
	private Instrument instrument;
	private Attachment attachment;
	private Clause clause;

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
	 * @return the requisitionsPartOneAndTwo
	 */
	public RequisitionsPartOneAndTwo getRequisitionsPartOneAndTwo() {
		return requisitionsPartOneAndTwo;
	}

	/**
	 * @param requisitionsPartOneAndTwo the requisitionsPartOneAndTwo to set
	 */
	public void setRequisitionsPartOneAndTwo(RequisitionsPartOneAndTwo requisitionsPartOneAndTwo) {
		this.requisitionsPartOneAndTwo = requisitionsPartOneAndTwo;
	}

	/**
	 * @return the requisitionsPartThree
	 */
	public RequisitionsPartThree getRequisitionsPartThree() {
		return requisitionsPartThree;
	}

	/**
	 * @param requisitionsPartThree the requisitionsPartThree to set
	 */
	public void setRequisitionsPartThree(RequisitionsPartThree requisitionsPartThree) {
		this.requisitionsPartThree = requisitionsPartThree;
	}

	/**
	 * @return the requisitionsPartFour
	 */
	public RequisitionsPartFour getRequisitionsPartFour() {
		return requisitionsPartFour;
	}

	/**
	 * @param requisitionsPartFour the requisitionsPartFour to set
	 */
	public void setRequisitionsPartFour(RequisitionsPartFour requisitionsPartFour) {
		this.requisitionsPartFour = requisitionsPartFour;
	}

	/**
	 * @return the instrument
	 */
	public Instrument getInstrument() {
		return instrument;
	}

	/**
	 * @param instrument the instrument to set
	 */
	public void setInstrument(Instrument instrument) {
		this.instrument = instrument;
	}

	/**
	 * @return the attachment
	 */
	public Attachment getAttachment() {
		return attachment;
	}

	/**
	 * @param attachment the attachment to set
	 */
	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}

	/**
	 * @return the clause
	 */
	public Clause getClause() {
		return clause;
	}

	/**
	 * @param clause the clause to set
	 */
	public void setClause(Clause clause) {
		this.clause = clause;
	}
	
	public User getApplicant() {
		return applicant;
	}

	public void setApplicant(User applicant) {
		this.applicant = applicant;
	}

}
