/**
 * 
 */
package mx.pagos.admc.core.enums;

/**
 * @author Mizraim
 *
 */
public enum ApplicationEnum {

	GWT("GWT"), ANGULAR("ANGULAR"), DEFAULT("GWT");

	private String name;

	ApplicationEnum(final String nameParam) {
		this.name = nameParam;
	}

	public String getName() {
		return this.name;
	}

	ApplicationEnum getApplicationEnum() {
		switch (name) {
		case "GWT":
			return GWT;
		case "ANGULAR":
			return ANGULAR;
		case "DEFAULT":
			return GWT;
		}
		return null;
	}

	public String getApplicationEnum1() {
		return this.name;
	}

	public void setApplicationEnum1(String nombre) {
		this.name = nombre;
	}

	@Override
	public String toString() {
		return this.name();
	}

}
