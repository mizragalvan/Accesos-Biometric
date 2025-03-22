package mx.pagos.admc.contracts.structures;
/**
 * @author Mizraim
 *
 */
public class DocPageInfo {
	/**
	 * Variable que almacena la pagina.
	 */
	 private int pagina;
	 /**
	  * Variable que almacena paragraph inicio.
	  */
    private int pStart;
    /**
     * Variable que almacena paragraph fin.
     */
    private int pEnd;

    public DocPageInfo(){}

    public DocPageInfo(int p, int pI, int pF){
        this.pagina = p;
        this.pStart = pI;
        this.pEnd = pF;
    }
    /**
     * Obtiene la pagina.
     * @return pagina.
     */
    public int getPagina() {
        return pagina;
    }
    /**
     * Estable la pagina.
     * @param pagina.
     */
    public void setPagina(int pagina) {
        this.pagina = pagina;
    }
    /**
     * Obiene el inicio.
     * @return pStart.
     */
    public int getpStart() {
        return pStart;
    }
    /**
     * Establece el inicio.
     * @param pStart.
     */
    public void setpStart(int pStart) {
        this.pStart = pStart;
    }
    /**
     * Obtiene el fin.
     * @return pEnd.
     */
    public int getpEnd() {
        return pEnd;
    }
    /**
     * Establece el fin.
     * @param pEnd.
     */
    public void setpEnd(int pEnd) {
        this.pEnd = pEnd;
    }
}
