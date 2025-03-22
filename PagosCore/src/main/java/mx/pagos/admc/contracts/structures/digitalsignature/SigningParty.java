package mx.pagos.admc.contracts.structures.digitalsignature;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SigningParty {
	
	@JsonProperty("partyId") 
	private String partyId;
	
	@JsonProperty("name") 
	private String name;
	
	@JsonProperty("address") 
	private String address;
	
	@JsonProperty("signingMethod") 
	private String signingMethod;
	
	@JsonProperty("role") 
	private String role;
	
	@JsonProperty("signingOrder") 
	private Integer signingOrder;
	
	@JsonProperty("signatureChallenge") 
	private String signatureChallenge;
	
	@JsonProperty("signatureChallengeResponse") 
	private String signatureChallengeResponse;
	
	
	
	public String getSignatureChallengeResponse() {
		return signatureChallengeResponse;
	}

	public void setSignatureChallengeResponse(String signatureChallengeResponse) {
		this.signatureChallengeResponse = signatureChallengeResponse;
	}

	public String getSignatureChallenge() {
		return signatureChallenge;
	}

	public void setSignatureChallenge(String signatureChallenge) {
		this.signatureChallenge = signatureChallenge;
	}

	public Integer getSigningOrder() {
		return signingOrder;
	}

	public void setSigningOrder(Integer signingOrder) {
		this.signingOrder = signingOrder;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSigningMethod() {
		return signingMethod;
	}

	public void setSigningMethod(String signingMethod) {
		this.signingMethod = signingMethod;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
}