package mx.pagos.admc.contracts.structures.digitalsignature;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DocumentEviSign {
	
    private List<String> uniqueIds;
    
    private String party;
    
    private String subject;
    
    private String document;
    
    private List<SigningParty> signingParties;
    
    private Options options;
    


	public Options getOptions() {
		return options;
	}

	public void setOptions(Options options) {
		this.options = options;
	}

	private List<SigningParty> interestedParties;
    
    private String comments;

    @JsonProperty("uniqueId")
    private String uniqueId;
    
    private List<RemindersEvi> reminders;
    
    

	public List<String> getUniqueIds() {
		return uniqueIds;
	}

	public void setUniqueIds(List<String> uniqueIds) {
		this.uniqueIds = uniqueIds;
	}

	public String getParty() {
		return party;
	}

	public void setParty(String party) {
		this.party = party;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public List<SigningParty> getSigningParties() {
		return signingParties;
	}

	public void setSigningParties(List<SigningParty> signingParties) {
		this.signingParties = signingParties;
	}


	public List<SigningParty> getInterestedParties() {
		return interestedParties;
	}

	public void setInterestedParties(List<SigningParty> interestedParties) {
		this.interestedParties = interestedParties;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public List<RemindersEvi> getReminders() {
		return reminders;
	}

	public void setReminders(List<RemindersEvi> reminders) {
		this.reminders = reminders;
	}
    
}
