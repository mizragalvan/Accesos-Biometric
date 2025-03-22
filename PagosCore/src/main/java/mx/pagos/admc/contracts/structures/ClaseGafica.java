package mx.pagos.admc.contracts.structures;

public class ClaseGafica {
	
	private String[] etiquetas;
	private String[] colores;
	private String color;
	private int[] datos;
	private int[] datosY;
	
	public String[] getEtiquetas() {
		return etiquetas;
	}
	public void setEtiquetas(String[] etiquetas) {
		this.etiquetas = etiquetas;
	}
	public String[] getColores() {
		return colores;
	}
	public void setColores(String[] colores) {
		this.colores = colores;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public int[] getDatos() {
		return datos;
	}
	public void setDatos(int[] datos) {
		this.datos = datos;
	}
	public int[] getDatosY() {
		return datosY;
	}
	public void setDatosY(int[] datosY) {
		this.datosY = datosY;
	}

}
