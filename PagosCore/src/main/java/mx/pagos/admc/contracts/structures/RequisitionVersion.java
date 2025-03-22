package mx.pagos.admc.contracts.structures;

public class RequisitionVersion {
    private Integer idRequisition;
    private Integer idRequisitionVersion;
    private Integer idSecondRequisitionVersion;
    private Integer versionNumber;
    private Integer secondVersionNumber;
    private Boolean isCompareCurrentRequisition = false;
    
    public final Integer getIdRequisition() {
        return this.idRequisition;
    }
    
    public final void setIdRequisition(final Integer idRequisitionParameter) {
        this.idRequisition = idRequisitionParameter;
    }
    
    public final Integer getIdRequisitionVersion() {
        return this.idRequisitionVersion;
    }
    
    public final void setIdRequisitionVersion(final Integer idRequisitionVersionParameter) {
        this.idRequisitionVersion = idRequisitionVersionParameter;
    }
    
    public final Integer getVersionNumber() {
        return this.versionNumber;
    }
    
    public final void setVersionNumber(final Integer versionNumberParameter) {
        this.versionNumber = versionNumberParameter;
    }

    public final Boolean getIsCompareCurrentRequisition() {
        return this.isCompareCurrentRequisition;
    }

    public final void setIsCompareCurrentRequisition(final Boolean isCompareCurrentRequisitionParameter) {
        this.isCompareCurrentRequisition = isCompareCurrentRequisitionParameter;
    }

    public final Integer getIdSecondRequisitionVersion() {
        return this.idSecondRequisitionVersion;
    }

    public final void setIdSecondRequisitionVersion(final Integer idSecondRequisitionVersionParameter) {
        this.idSecondRequisitionVersion = idSecondRequisitionVersionParameter;
    }

    public final Integer getSecondVersionNumber() {
        return this.secondVersionNumber;
    }

    public final void setSecondVersionNumber(final Integer secondVersionNumberParameter) {
        this.secondVersionNumber = secondVersionNumberParameter;
    }
}
