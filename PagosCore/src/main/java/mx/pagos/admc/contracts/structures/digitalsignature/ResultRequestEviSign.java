package mx.pagos.admc.contracts.structures.digitalsignature;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResultRequestEviSign {

	@JsonProperty("results")
	private ArrayList<DocumentsResultEviSign> results;

	@JsonProperty("totalMatches")
	private Integer totalMatches;

	public ArrayList<DocumentsResultEviSign> getResults() {
		return results;
	}

	public void setResults(ArrayList<DocumentsResultEviSign> results) {
		this.results = results;
	}

	public Integer getTotalMatches() {
		return totalMatches;
	}

	public void setTotalMatches(Integer totalMatches) {
		this.totalMatches = totalMatches;
	}

}
