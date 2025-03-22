package mx.pagos.admc.contracts.structures;

/**
 * Bean para retorno de datos de folios
 * por plantilla.
 * @author Mizraim
 *
 */
public class FolioByPlantilla {
	/**
	 * Variable que almacena el IdRequisition.
	 */
	private int IdRequisition;
	/**
	 * Variable que almacena Status.
	 */
	private String Status;
	/**
	 * Variable que almacena el nombre.
	 */
	private String Name;
	/**
	 * Variable que almacena PersonalityEnum.
	 */
	private String PersonalityEnum;
	/**
	 * Variable que almacena nombre de contrato.
	 */
	private String NameContrato;
	/**
	 * Variable que almacena bandeja.
	 */
	private String Bandeja;
	/**
	 * Obtiene el IdRequisition.
	 * @return IdRequisition.
	 */
	public int getIdRequisition() {
		return IdRequisition;
	}
	/**
	 * Establece el IdRequisition.
	 * @param idRequisition.
	 */
	public void setIdRequisition(int idRequisition) {
		IdRequisition = idRequisition;
	}
	/**
	 * Obtiene el Status.
	 * @return Status.
	 */
	public String getStatus() {
		return Status;
	}
	/**
	 * Establece el Status.
	 * @param status.
	 */
	public void setStatus(String status) {
		Status = status;
	}
	/**
	 * Obtiene el Name.
	 * @return Name.
	 */
	public String getName() {
		return Name;
	}
	/**
	 * Establece el Name.
	 * @param name.
	 */
	public void setName(String name) {
		Name = name;
	}
	/**
	 * Obtiene el PersonalityEnum.
	 * @return PersonalityEnum.
	 */
	public String getPersonalityEnum() {
		return PersonalityEnum;
	}
	/**
	 * Establece el PersonalityEnum.
	 * @param personalityEnum
	 */
	public void setPersonalityEnum(String personalityEnum) {
		PersonalityEnum = personalityEnum;
	}
	/**
	 * Obtiene el NameContrato.
	 * @return NameContrato.
	 */
	public String getNameContrato() {
		return NameContrato;
	}
	/**
	 * Establece el NameContrato.
	 * @param nameContrato.
	 */
	public void setNameContrato(String nameContrato) {
		NameContrato = nameContrato;
	}
	/**
	 * Obtiene la Bandeja.
	 * @return Bandeja.
	 */
	public String getBandeja() {
		return Bandeja;
	}
	/**
	 * Establece la bandeja.
	 * @param bandeja.
	 */
	public void setBandeja(String bandeja) {
		Bandeja = bandeja;
	}
	
	

}
