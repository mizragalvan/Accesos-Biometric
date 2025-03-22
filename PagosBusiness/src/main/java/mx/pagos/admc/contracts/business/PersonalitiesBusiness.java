package mx.pagos.admc.contracts.business;

import java.util.List;

import mx.pagos.admc.contracts.interfaces.Personalitable;
import mx.pagos.admc.contracts.structures.Personality;
import mx.pagos.admc.contracts.structures.RequiredDocument;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * Lógica de negocio para manejar los tipos de personalidad
 * 
 * @author Mizraim
 * 
 * @see Personality
 * @see Personalitable
 * @see BusinessException
 * @see RecordStatusEnum
 *
 */
@Service
public class PersonalitiesBusiness {

    private static final String MESAGE_NON_EXISTENT_PERSONALITY = "El tipo de personalidad ha dejado de existir";
    private static final String MESSAGE_PROBLEM_CHANGING_PERSONALITY_STATUS =
            "Ocurrió un problema al cambiar el estatus del tipo de personalidad";
    private static final String MESSAGE_PROBLEM_SAVING_PERSONALITY =
            "Ocurrió un problema al guardar el tipo de personalidad";
    private static final String MESSAGE_RETRIEVING_PERSONALITIES_ERROR =
            "Ocurrió un eror al recuperar los tipos de personalidad";
    private static final String MESSAGE_RETRIEVING_PERSONALITY_ERROR =
            "Ocurrió un eror al recuperar el tipo de personalidad";
    private static final String MESSAGE_RETRIEVING_REQUIREDDOCUMENT_ERROR =
            "Ocurrió un eror al recuperar documentos requeridos";

    private static final Logger LOG = Logger.getLogger(PersonalitiesBusiness.class);

    @Autowired
    private Personalitable personalitable;

    public final Integer saveOrUpdate(final Personality personality) throws BusinessException {
        try {
            final Integer idPersonality = this.personalitable.saveOrUpdate(personality);
            this.personalitable.deleteRequiredDocumentByPersonality(idPersonality);
            for (Integer idRequiredDocument: personality.getIdRequiredDocumentList()) 
                this.personalitable.saveRequiredDocumentByPersonality(idPersonality, idRequiredDocument);
            return idPersonality;
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_PROBLEM_SAVING_PERSONALITY, databaseException);
            throw new BusinessException(MESSAGE_PROBLEM_SAVING_PERSONALITY, databaseException);
        }
    }

    public final void changePersonalityStatus(final Integer idPersonality, final RecordStatusEnum status)
            throws BusinessException {
        try {
            final RecordStatusEnum changedStatus =
                    status.equals(RecordStatusEnum.ACTIVE) ? RecordStatusEnum.INACTIVE : RecordStatusEnum.ACTIVE;
            this.personalitable.changePersonalityStatus(idPersonality, changedStatus);
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_PROBLEM_CHANGING_PERSONALITY_STATUS, databaseException);
            throw new BusinessException(MESSAGE_PROBLEM_CHANGING_PERSONALITY_STATUS, databaseException);
        }
    }

    public final List<Personality> findAll() throws BusinessException {
        try {
            return this.personalitable.findAll();
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_RETRIEVING_PERSONALITIES_ERROR, databaseException);
            throw new BusinessException(MESSAGE_RETRIEVING_PERSONALITIES_ERROR, databaseException);
        }
    }

    public final Personality findByIdPersonality(final Integer idPersonality) throws BusinessException {
        try {
            return this.personalitable.findByIdPersonality(idPersonality);
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_RETRIEVING_PERSONALITY_ERROR, databaseException);
            throw new BusinessException(MESSAGE_RETRIEVING_PERSONALITY_ERROR, databaseException);
        } catch (EmptyResultException emptyResultException) {
            LOG.error(MESAGE_NON_EXISTENT_PERSONALITY + ". ID: " + idPersonality.toString(),
                    emptyResultException);
            throw new BusinessException(MESAGE_NON_EXISTENT_PERSONALITY, emptyResultException);
        }
    }

    public final List<Personality> findByStatus(final RecordStatusEnum status) throws BusinessException {
        try {
            return this.personalitable.findByStatus(status);
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_RETRIEVING_PERSONALITIES_ERROR, databaseException);
            throw new BusinessException(MESSAGE_RETRIEVING_PERSONALITIES_ERROR, databaseException);
        }
    }
    
    public final List<RequiredDocument> findRequiredDocumentByPersonality(final Integer idPersonality) 
            throws BusinessException {
        try {
            return this.personalitable.findRequiredDocumentByPersonality(idPersonality);
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_RETRIEVING_REQUIREDDOCUMENT_ERROR, databaseException);
            throw new BusinessException(MESSAGE_RETRIEVING_PERSONALITIES_ERROR, databaseException);
        }
    }
}
