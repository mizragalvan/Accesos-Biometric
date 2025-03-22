package mx.pagos.admc.contracts.structures;
/**
 * @author Mizraim
 *
 */
public class ConfigAnexoImg {
	/**
	 * Variable ruta de archivo.
	 */
	private String rutaArchivo;
	/**
	 * Variable de orden.
	 */
    private int orden;
    /**
     * Variable nombre.
     */
    private String name;

    public ConfigAnexoImg() {}

    public ConfigAnexoImg(int orden, String rutaArchivo, String name) {
        this.orden = orden;
        this.rutaArchivo = rutaArchivo;
        this.name = name;
    }
    /**
     * Obtiene ruta del archivo.
     * @return rutaArchivo.
     */
    public String getRutaArchivo() {
        return rutaArchivo;
    }
    /**
     * Establece ruta del archivo.
     * @param rutaArchivo.
     */
    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }
    /**
     * Obtiene el orden.
     * @return orden.
     */
    public int getOrden() {
        return orden;
    }
    /**
     * Establece el orden.
     * @param orden.
     */
    public void setOrden(int orden) {
        this.orden = orden;
    }
    /**
     * Obtiene el nombre.
     * @return name.
     */
    public String getName() {
        return name;
    }
    /**
     * Establece el nombre.
     * @param name.
     */
    public void setName(String name) {
        this.name = name;
    }
}
