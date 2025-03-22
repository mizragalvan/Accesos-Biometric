package mx.pagos.admc.contracts.structures;

import mx.pagos.admc.enums.FlowPurchasingEnum;

public class TrayFilter {
    private Integer idFlow;
    private Integer idUser;
    private Boolean isUserFiltered;
    private FlowPurchasingEnum status;
    private String idRequisition;
    private String documentType;
    private String supplierName;
    private String fechaInicio;
    private String fechaFin;
    
    public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

	/** Nombre del abogado asignado a la solicitud **/ 
    private String lawyerName;
    
    public final Integer getIdFlow() {
        return this.idFlow;
    }
    
    public final void setIdFlow(final Integer idFlowParameter) {
        this.idFlow = idFlowParameter;
    }
    
    public final Integer getIdUser() {
        return this.idUser;
    }
    
    public final void setIdUser(final Integer idUserParameter) {
        this.idUser = idUserParameter;
    }
    
    public final Boolean getIsUserFiltered() {
        return this.isUserFiltered;
    }
    
    public final void setIsUserFiltered(final Boolean isUserFilteredParameter) {
        this.isUserFiltered = isUserFilteredParameter;
    }
    
    public final FlowPurchasingEnum getStatus() {
        return this.status;
    }
    
    public final void setStatus(final FlowPurchasingEnum statusParameter) {
        this.status = statusParameter;
    }

	public final String getIdRequisition() {
		return this.idRequisition;
	}

	public final void setIdRequisition(final String idRequisitionParameter) {
		this.idRequisition = idRequisitionParameter;
	}

	public final String getDocumentType() {
		return this.documentType;
	}

	public final void setDocumentType(final String documentTypeParameter) {
		this.documentType = documentTypeParameter;
	}

	public final String getSupplierName() {
		return this.supplierName;
	}

	public final void setSupplierName(final String supplierNameParameter) {
		this.supplierName = supplierNameParameter;
	}

    /**
     * @return the lawyerName
     */
    public final String getLawyerName() {
        return lawyerName;
    }

    /**
     * @param lawyerName the lawyerName to set
     */
    public final void setLawyerName(final String lawyerName) {
        this.lawyerName = lawyerName;
    }
}