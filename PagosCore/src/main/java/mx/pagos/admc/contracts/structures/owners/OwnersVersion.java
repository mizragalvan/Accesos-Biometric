package mx.pagos.admc.contracts.structures.owners;

public class OwnersVersion {
    private Integer idRequisitionOwners;
    private Integer idRequisitionOwnersVersion;
    private Integer versionNumber;
    
    public final Integer getIdRequisitionOwners() {
        return this.idRequisitionOwners;
    }
    
    public final void setIdRequisitionOwners(final Integer idRequisitionOwnersParameter) {
        this.idRequisitionOwners = idRequisitionOwnersParameter;
    }
    
    public final Integer getIdRequisitionOwnersVersion() {
        return this.idRequisitionOwnersVersion;
    }
    
    public final void setIdRequisitionOwnersVersion(final Integer idRequisitionOwnersVersionParameter) {
        this.idRequisitionOwnersVersion = idRequisitionOwnersVersionParameter;
    }
    
    public final Integer getVersionNumber() {
        return this.versionNumber;
    }
    
    public final void setVersionNumber(final Integer versionNumberParameter) {
        this.versionNumber = versionNumberParameter;
    }
}
