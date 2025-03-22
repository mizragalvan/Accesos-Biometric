package mx.pagos.admc.contracts.structures;

import java.util.ArrayList;
import java.util.List;

import mx.pagos.admc.enums.PersonalityEnum;
import mx.pagos.admc.enums.RecordStatusEnum;

/**
 * 
 * Estructura que tiene los campos necesarios para almacenar el tipo de personalidad
 * 
 * @author Mizraim
 * 
 */
public class Personality {
    private Integer idPersonality;
    private String name;
    private RecordStatusEnum status;
    private PersonalityEnum personType;
    private List<Integer> idRequiredDocumentList = new ArrayList<>();
    
    public final Integer getIdPersonality() {
        return this.idPersonality;
    }
    
    public final void setIdPersonality(final Integer idPersonalityParameter) {
        this.idPersonality = idPersonalityParameter;
    }
    
    public final String getName() {
        return this.name;
    }
    
    public final void setName(final String nameParameter) {
        this.name = nameParameter;
    }

    public final RecordStatusEnum getStatus() {
        return this.status;
    }

    public final void setStatus(final RecordStatusEnum statusParameter) {
        this.status = statusParameter;
    }

    public final PersonalityEnum getPersonalityEnum() {
        return this.personType;
    }

    public final void setPersonalityEnum(final PersonalityEnum personTypeParameter) {
        this.personType = personTypeParameter;
    }

    public final List<Integer> getIdRequiredDocumentList() {
        return this.idRequiredDocumentList;
    }

    public final void setIdRequiredDocumentList(final List<Integer> idRequiredDocumentParameter) {
        this.idRequiredDocumentList = idRequiredDocumentParameter;
    }
}
