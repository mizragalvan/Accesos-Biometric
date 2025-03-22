package mx.pagos.admc.contracts.structures.digitalsignature;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Affidavit {

    @JsonProperty("uniqueId") 
    private String uniqueId;
    
    @JsonProperty("date") 
    private Date date;
    
    @JsonProperty("evidenceUniqueId") 
    private String evidenceUniqueId;
    
    @JsonProperty("partyUniqueId") 
    private String partyUniqueId;
    
    @JsonProperty("bytes") 
    private String bytes;
    
    @JsonProperty("description") 
    private String description;
    
    @JsonProperty("kind") 
    private String kind;
    
    @JsonProperty("additionalData") 
    private Object additionalData;

    
	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getEvidenceUniqueId() {
		return evidenceUniqueId;
	}

	public void setEvidenceUniqueId(String evidenceUniqueId) {
		this.evidenceUniqueId = evidenceUniqueId;
	}

	public String getPartyUniqueId() {
		return partyUniqueId;
	}

	public void setPartyUniqueId(String partyUniqueId) {
		this.partyUniqueId = partyUniqueId;
	}

	public String getBytes() {
		return bytes;
	}

	public void setBytes(String bytes) {
		this.bytes = bytes;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public Object getAdditionalData() {
		return additionalData;
	}

	public void setAdditionalData(Object additionalData) {
		this.additionalData = additionalData;
	}
    
}
