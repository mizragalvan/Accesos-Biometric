package mx.pagos.admc.contracts.structures.digitalsignature;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InterestedParty {

    @JsonProperty("partyId") 
    private String partyId;
    
    @JsonProperty("address") 
    private String address;

    
	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
    
}
