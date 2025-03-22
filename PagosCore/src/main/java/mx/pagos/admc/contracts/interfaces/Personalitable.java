package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.Personality;
import mx.pagos.admc.contracts.structures.RequiredDocument;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

/**
 * 
 * @author Mizraim
 * 
 * <p>Interfaz que contiene los métodos paara interactuar con los DAos de personalidad
 * 
 * @see Personality
 * @see RecordStatusEnum
 * @see DatabaseException
 *
 */
public interface Personalitable {
    Integer saveOrUpdate(Personality personality) throws DatabaseException;
    
    void changePersonalityStatus(Integer idPersonality, RecordStatusEnum status) throws DatabaseException;
    
    List<Personality> findAll() throws DatabaseException;
    
    Personality findByIdPersonality(Integer idPersonality) throws DatabaseException, EmptyResultException;
    
    List<Personality> findByStatus(RecordStatusEnum status) throws DatabaseException;
    
    List<RequiredDocument> findRequiredDocumentByPersonality(Integer idPersonality) throws DatabaseException;
    
    void saveRequiredDocumentByPersonality(Integer idPersonality, Integer idRequiredDocument)
    throws DatabaseException;
    
    void deleteRequiredDocumentByPersonality(Integer idPersonality) throws DatabaseException;
}
