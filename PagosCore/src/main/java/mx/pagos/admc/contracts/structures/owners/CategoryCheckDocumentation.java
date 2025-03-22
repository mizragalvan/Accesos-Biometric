package mx.pagos.admc.contracts.structures.owners;

public class CategoryCheckDocumentation {
    private Integer idCategory;
    private Integer idCheckDocumentation;
    
    public final Integer getIdCategory() {
        return this.idCategory;
    }
    
    public final void setIdCategory(final Integer idCategoryParameter) {
        this.idCategory = idCategoryParameter;
    }
    
    public final Integer getIdCheckDocumentation() {
        return this.idCheckDocumentation;
    }
    
    public final void setIdCheckDocumentation(final Integer idCheckDocumentationParameter) {
        this.idCheckDocumentation = idCheckDocumentationParameter;
    }
}
