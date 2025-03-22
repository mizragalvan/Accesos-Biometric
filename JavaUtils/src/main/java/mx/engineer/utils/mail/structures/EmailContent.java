package mx.engineer.utils.mail.structures;

import java.time.LocalDate;

/**
 * @author Mizraim
 */
public class EmailContent {
	private String subject = "";
	private String fieldOne = "";
	private String fieldOneDescription = "";
	private String fieldTwo = "";
	private String fieldTwoDescription = "";
	private String fieldThree = "";
	private String fieldThreeDescription = "";
	private String fieldFour = "";
	private String fieldFourDescription = "";
	private String sendDateDescription = "";
	private LocalDate sendDate;
	private String content = "";
	private String brand = "";

	public final String getSubject() {
		return this.subject;
	}

	public final void setSubject(final String subjectParameter) {
		this.subject = subjectParameter;
	}

	public final String getFieldOne() {
		return this.fieldOne;
	}

	public final void setFieldOne(final String fieldOneParameter) {
		this.fieldOne = fieldOneParameter;
	}

	public final String getFieldOneDescription() {
		return this.fieldOneDescription;
	}

	public final void setFieldOneDescription(final String fieldOneDescriptionParameter) {
		this.fieldOneDescription = fieldOneDescriptionParameter;
	}

	public final String getFieldTwo() {
		return this.fieldTwo;
	}

	public final void setFieldTwo(final String fieldTwoParameter) {
		this.fieldTwo = fieldTwoParameter;
	}

	public final String getFieldTwoDescription() {
		return this.fieldTwoDescription;
	}

	public final void setFieldTwoDescription(final String fieldTwoDescriptionParameter) {
		this.fieldTwoDescription = fieldTwoDescriptionParameter;
	}

	public final String getFieldThree() {
		return this.fieldThree;
	}

	public final void setFieldThree(final String fieldThreeParameter) {
		this.fieldThree = fieldThreeParameter;
	}

	public final String getFieldThreeDescription() {
		return this.fieldThreeDescription;
	}

	public final void setFieldThreeDescription(final String fieldThreeDescriptionParameter) {
		this.fieldThreeDescription = fieldThreeDescriptionParameter;
	}

	public final String getContent() {
		return this.content;
	}

	public final void setContent(final String contentParameter) {
		this.content = contentParameter;
	}

	public final String getBrand() {
		return this.brand;
	}

	public final void setBrand(final String brandParameter) {
		this.brand = brandParameter;
	}

	/**
	 * @return the fieldFour
	 */
	public String getFieldFour() {
		return fieldFour;
	}

	/**
	 * @param fieldFour the fieldFour to set
	 */
	public void setFieldFour(String fieldFour) {
		this.fieldFour = fieldFour;
	}

	/**
	 * @return the fieldFourDescription
	 */
	public String getFieldFourDescription() {
		return fieldFourDescription;
	}

	/**
	 * @param fieldFourDescription the fieldFourDescription to set
	 */
	public void setFieldFourDescription(String fieldFourDescription) {
		this.fieldFourDescription = fieldFourDescription;
	}

	/**
	 * @return the sendDate
	 */
	public LocalDate getSendDate() {
		return sendDate;
	}

	/**
	 * @param fechaEnvio the sendDate to set
	 */
	public void setSendDate(LocalDate fechaEnvio) {
		this.sendDate = fechaEnvio;
	}

	/**
	 * @return the sendDateDescription
	 */
	public String getSendDateDescription() {
		return sendDateDescription;
	}

	/**
	 * @param sendDateDescription the sendDateDescription to set
	 */
	public void setSendDateDescription(String sendDateDescription) {
		this.sendDateDescription = sendDateDescription;
	}
}