package mx.pagos.security.structures;

import java.io.Serializable;
import java.util.List;

import mx.pagos.admc.enums.RecordStatusEnum;

/*
 * Clase que representa la estructura de la tabla PROFILE
 * */
public class Profile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idProfile;
	private String name;
	private RecordStatusEnum status;
	private Integer numberPage;
	private Integer totalRows;

	//No pertencen a la tabla Profile
	private List<ProfileScreenFlow> listaScreen;
	private List<Menu> listaMenu;
	private Integer idFlow;

	public Profile() {

	}

	public Profile(final Integer idProfileParameter) {
		this.idProfile = idProfileParameter;
	}

	public final Integer getIdProfile() {
		return this.idProfile;
	}

	public final void setIdProfile(final Integer idProfileParameter) {
		this.idProfile = idProfileParameter;
	}

	public final String getName() {
		return this.name;
	}

	public final void setName(final String nameParameter) {
		this.name = nameParameter;
	}

	public final RecordStatusEnum getStatus() {
		return this.status;
	}

	public final void setStatus(final RecordStatusEnum statusParameter) {
		this.status = statusParameter;
	}

	public List<ProfileScreenFlow> getListaScreen() {
		return listaScreen;
	}

	public void setListaScreen(List<ProfileScreenFlow> listaScreen) {
		this.listaScreen = listaScreen;
	}

	public Integer getIdFlow() {
		return idFlow;
	}

	public void setIdFlow(Integer idFlow) {
		this.idFlow = idFlow;
	}

	public List<Menu> getListaMenu() {
		return listaMenu;
	}

	public void setListaMenu(List<Menu> listaMenu) {
		this.listaMenu = listaMenu;
	}

	public final Integer getNumberPage() {
		return this.numberPage;
	}

	public final void setNumberPage(final Integer numberPageParameter) {
		this.numberPage = numberPageParameter;
	}

	public final Integer getTotalRows() {
		return this.totalRows;
	}

	public final void setTotalRows(final Integer totalRowsParameter) {
		this.totalRows = totalRowsParameter;
	}
}
