/**
 * 
 */
package mx.pagos.admc.core.enums;

/**
 * @author Mizraim
 *
 */
public enum ContratistaEnum {

	PROVEEDOR("PROVEEDOR"), CLIENTE("CLIENTE"), INTERCOMPANY("INTERCOMPANY");

	private String name;

	ContratistaEnum(final String nameParam) {
		this.name = nameParam;
	}
	
	public String getName() {
		return this.name;
	}

	ContratistaEnum getContratistaEnum() {
		switch (name) {
		case "PROVEEDOR":
			return PROVEEDOR;
		case "CLIENTE":
			return CLIENTE;
		case "INTERCOMPANY":
			return INTERCOMPANY;
		}
		return null;
	}

	public String getContratistaEnum1() {
		return this.name;
	}

	public void setContratista(String nombre) {
		this.name = nombre;
	}

	@Override
	public String toString() {
		return this.name();
	}
}
