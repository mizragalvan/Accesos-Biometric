package mx.pagos.security.structures;

import java.io.Serializable;

public class UserSession  implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2580606507785164716L;
	private Integer idUsuarioSession;
	private Integer numeroIntento = 0;
	private User usuario;
	private Integer idFlow;
	private Integer minutesInSession;
	
	public Integer getIdUsuarioSession() {
		return this.idUsuarioSession;
	}
	
	public void setIdUsuarioSession(final Integer idUsuarioSessionParameter) {
		this.idUsuarioSession = idUsuarioSessionParameter;
	}
	
	public Integer getNumeroIntento() {
		return this.numeroIntento;
	}
	
	public void setNumeroIntento(final Integer numeroIntentoParameter) {
		this.numeroIntento = numeroIntentoParameter;
	}
	
	public User getUsuario() {
		return this.usuario;
	}
	
	public void setUsuario(final User usuarioParameter) {
		this.usuario = usuarioParameter;
	}

    public Integer getIdFlow() {
        return this.idFlow;
    }

    public void setIdFlow(final Integer idFlowParameter) {
        this.idFlow = idFlowParameter;
    }

	public Integer getMinutesInSession() {
		return minutesInSession;
	}

	public void setMinutesInSession(final Integer minutesInSessionParameter) {
		this.minutesInSession = minutesInSessionParameter;
	}

	@Override
	public String toString() {
		return "UserSession [idUsuarioSession=" + idUsuarioSession + ", numeroIntento=" + numeroIntento + ", usuario="
				+ usuario + ", idFlow=" + idFlow + ", minutesInSession=" + minutesInSession + "]";
	}
	
	
}
