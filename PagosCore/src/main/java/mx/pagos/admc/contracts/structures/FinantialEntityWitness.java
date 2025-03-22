package mx.pagos.admc.contracts.structures;

public class FinantialEntityWitness {

	private Integer idRequisitonFinantialEntityWitness;
	private Integer idRequisition;
	private String name;
	
	public final Integer getIdRequisitonFinEntityWitness() {
		return this.idRequisitonFinantialEntityWitness;
	}
	
	public final void setIdRequisitonFinEntityWitness(
			final Integer idRequisitonFinantialEntityWitnessParameter) {
		this.idRequisitonFinantialEntityWitness = idRequisitonFinantialEntityWitnessParameter;
	}
	
	public final Integer getIdRequisition() {
		return this.idRequisition;
	}
	
	public final void setIdRequisition(final Integer idRequisitionParameter) {
		this.idRequisition = idRequisitionParameter;
	}
	
	public final String getName() {
		return this.name;
	}
	
	public final void setName(final String nameParameter) {
		this.name = nameParameter;
	}
}
