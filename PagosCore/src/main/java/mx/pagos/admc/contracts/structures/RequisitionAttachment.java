package mx.pagos.admc.contracts.structures;


/**
 * 
 * @author Mizraim
 *
 */
public class RequisitionAttachment {
    private Integer IdRequisition;
    private Integer IdDocument;



	public RequisitionAttachment() {
		super();
	}


	public RequisitionAttachment(final Integer idRequisition, final Integer idDocument) {
		super();
		IdRequisition = idRequisition;
		IdDocument = idDocument;
	}

	public final Integer getIdRequisition() {
		return IdRequisition;
	}
	public final void setIdRequisition(final Integer idRequisition) {
		IdRequisition = idRequisition;
	}

	public final Integer getIdDocument() {
		return IdDocument;
	}
	public final void setIdDocument(final Integer idDocument) {
		IdDocument = idDocument;
	}



}
