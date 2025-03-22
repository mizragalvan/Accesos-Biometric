package mx.pagos.admc.contracts.structures.digitalsignature;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DocumentPscWorld {

	/**
	 * Es el Usuario de Servicio que se proporcionó mediante el Portal de Consumo NOM TS
	 */
	private String usuario;
	
	/**
	 * Es la Contraseña de Servicio que se proporcionó mediante el Portal de Consumo NOM TS
	 */
	private String password;
	
	/**
	 * Representación SHA256 del documento al que se le generará la constancia NOM151
	 * Es la representación en SHA-256 del archivo original.
	 */
	private String hash;
	
	/**
	 * Es la cadena con la cual el sistema identificará el archivo enviado para generar una 
	 * Constancia de Conservación o Sello de Tiempo. 
	 */
	private String identificador;

	@JsonProperty("TimeStamp")
	private String timeStamp;
	
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	
}
