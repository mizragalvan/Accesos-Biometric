package mx.pagos.admc.contracts.structures.digitalsignature;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Options {
	
	private RemindersEvi reminders;
	
	@JsonProperty("timeToLive") 
	private Integer timeToLive;
	
	@JsonProperty("signatureRequestInfoText") 
	private String signatureRequestInfoText;
	
	@JsonProperty("language") 
	private String language;
	
	
    public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getSignatureRequestInfoText() {
		return signatureRequestInfoText;
	}

	public void setSignatureRequestInfoText(String signatureRequestInfoText) {
		this.signatureRequestInfoText = signatureRequestInfoText;
	}

	public Integer getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(Integer timeToLive) {
		this.timeToLive = timeToLive;
	}

	public RemindersEvi getReminders() {
		return reminders;
	}

	public void setReminders(RemindersEvi reminders) {
		this.reminders = reminders;
	}

}
