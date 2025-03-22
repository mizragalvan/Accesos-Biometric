package mx.pagos.admc.contracts.structures;

public class FinancialEntitieCombination {

	private Integer idCombination;
	private Integer idFinancialEntity;
	private String combinationName;
	
	public final Integer getIdCombination() {
		return this.idCombination;
	}
	
	public final void setIdCombination(final Integer idCombinationParameter) {
		this.idCombination = idCombinationParameter;
	}
	
	public final Integer getIdFinancialEntity() {
		return this.idFinancialEntity;
	}
	
	public final void setIdFinancialEntity(final Integer idFinancialEntityParameter) {
		this.idFinancialEntity = idFinancialEntityParameter;
	}
	
	public final String getCombinationName() {
		return this.combinationName;
	}
	
	public final void setCombinationName(final String combinationNameParameter) {
		this.combinationName = combinationNameParameter;
	}
}
