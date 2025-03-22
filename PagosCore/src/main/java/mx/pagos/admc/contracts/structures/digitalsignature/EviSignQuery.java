package mx.pagos.admc.contracts.structures.digitalsignature;

public class EviSignQuery {

	/*
	 * Lista con los identificadores (EviId) de las evidencias a consultar.
	 * [opcional]
	 */
	private String withUniqueIds;

	/*
	 * Lista con los localizadores (LookupKey) de las evidencias a consultar.
	 * [opcional]
	 */
	private String withLookupKeys;

	/*
	 * Retornar únicamente aquellos contratos que estén en el estado de
	 * procesamiento indicado.
	 * [opcional]
	 */
	private String onState;

	/*
	 * Retornar únicamente aquellos contratos que hayan obtenido el resultado
	 * indicado.
	 * [opcional]
	 */
	private String withOutcome;

	/*
	 * Número de elementos a retornar.
	 * [opcional]
	 */
	private Integer limit;

	/*
	 * Permite paginar los resultados, retornando únicamente aquellos resultados a
	 * partir del indicado.
	 * [opcional]
	 */
	private Integer offset;

	/*
	 * Indica si debe incluirse en el resultado obtenido el documento/contrato.
	 */
	private Boolean includeDocumentOnResult;

	/*
	 * Indica si debe incluirse en el resultado obtenido su listado de affidavits.
	 */
	private Boolean includeAffidavitsOnResult;

	/*
	 * Indica si debe incluirse en el resultado los bytes de los affidavits.
	 * Valor por defecto FALSE
	 */
	private Boolean includeAffidavitBlobsOnResult;

	/*
	 * Indica si debe incluirse en el resultado obtenido su lista de attachments.
	 */
	private Boolean includeAttachmentsOnResult;

	/*
	 * Indicasi debe incluirse en el resultado los bytes de los attachments.
	 * Valor por defecto FALSE.
	 */
	private Boolean includeAttachmentBlobsOnResult;

	
	
	public String getWithUniqueIds() {
		return withUniqueIds;
	}

	public void setWithUniqueIds(String withUniqueIds) {
		this.withUniqueIds = withUniqueIds;
	}

	public String getWithLookupKeys() {
		return withLookupKeys;
	}

	public void setWithLookupKeys(String withLookupKeys) {
		this.withLookupKeys = withLookupKeys;
	}

	public String getOnState() {
		return onState;
	}

	public void setOnState(String onState) {
		this.onState = onState;
	}

	public String getWithOutcome() {
		return withOutcome;
	}

	public void setWithOutcome(String withOutcome) {
		this.withOutcome = withOutcome;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Boolean getIncludeDocumentOnResult() {
		return includeDocumentOnResult;
	}

	public void setIncludeDocumentOnResult(Boolean includeDocumentOnResult) {
		this.includeDocumentOnResult = includeDocumentOnResult;
	}

	public Boolean getIncludeAffidavitsOnResult() {
		return includeAffidavitsOnResult;
	}

	public void setIncludeAffidavitsOnResult(Boolean includeAffidavitsOnResult) {
		this.includeAffidavitsOnResult = includeAffidavitsOnResult;
	}

	public Boolean getIncludeAffidavitBlobsOnResult() {
		return includeAffidavitBlobsOnResult;
	}

	public void setIncludeAffidavitBlobsOnResult(Boolean includeAffidavitBlobsOnResult) {
		this.includeAffidavitBlobsOnResult = includeAffidavitBlobsOnResult;
	}

	public Boolean getIncludeAttachmentsOnResult() {
		return includeAttachmentsOnResult;
	}

	public void setIncludeAttachmentsOnResult(Boolean includeAttachmentsOnResult) {
		this.includeAttachmentsOnResult = includeAttachmentsOnResult;
	}

	public Boolean getIncludeAttachmentBlobsOnResult() {
		return includeAttachmentBlobsOnResult;
	}

	public void setIncludeAttachmentBlobsOnResult(Boolean includeAttachmentBlobsOnResult) {
		this.includeAttachmentBlobsOnResult = includeAttachmentBlobsOnResult;
	}

}
