package mx.pagos.admc.contracts.business;

import java.util.List;

import mx.pagos.admc.contracts.interfaces.UsersContractable;
import mx.pagos.admc.contracts.structures.UserLawyerEvaluator;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;
import mx.pagos.security.structures.User;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersContractBusiness {
	private static final String MESSAGE_RETIRVING_IS_TRAY_FILTERED_ERROR =
	        "Hubo un error al determinar si la bandeja es filtrada por usuario o perfil";
	private static final String ERROR_MESSAGE_FIND_DECIDER_LAWYER = 
	        "Hubo un error al recuperar a los abogados dictaminadores por flujo";
	
	private static final Logger LOG = Logger.getLogger(UsersContractBusiness.class);

    @Autowired
	private UsersContractable user;
	
	public List<UserLawyerEvaluator> findAviableLawyers(final Integer idFlow) throws BusinessException {
		try {
		    LOG.info("\n-----------------------------------\nBUSQUEDA DE ABOGADOS DISPONIBLES");
		    List<UserLawyerEvaluator> abogados = this.user.findAviableLawyersOrEvaluators(idFlow, true, false);
		    LOG.info("No.Abogados :: "+abogados.size()+"\n------------------------------------------");
		    return abogados;
		} catch (DatabaseException databaseException) {
			LOG.error("Error al obtener abogados viables", databaseException);
			throw new BusinessException("Error al obtener abogados viables", databaseException);
		}
	}
	
	public List<UserLawyerEvaluator> findAviableEvaluators(final Integer idFlow) throws BusinessException {
		try {
		    LOG.info("UsersContractBusiness -> findAviableEvaluators");
			return this.user.findAviableLawyersOrEvaluators(idFlow, false, true);
		} catch (DatabaseException databaseException) {
			LOG.error("Error al obtener evaluadores viables", databaseException);
			throw new BusinessException("Error al obtener evaluadores viables", databaseException);
		}
	}
	
	public UserLawyerEvaluator findAviableLawyer(final Integer idFlow) throws BusinessException {
		try {
		    LOG.info("UsersContractBusiness -> findAviableLawyer");
			return this.user.findAviableLawyerOrEvaluator(idFlow, true, false);
		} catch (DatabaseException databaseException) {
			LOG.error("Error al obtener abogados viables", databaseException);
			throw new BusinessException("Error al obtener abogados viables", databaseException);
		} catch (EmptyResultException emptyResultException) {
			LOG.error("Error no existe abogado disponible", emptyResultException);
			throw new BusinessException("Error no existe abogado disponible", emptyResultException);
		} 
	}
	
	public UserLawyerEvaluator findAviableEvaluator(final Integer idFlow) throws BusinessException {
		try {
		    LOG.info("UsersContractBusiness -> findAviableEvaluator");
			return this.user.findAviableLawyerOrEvaluator(idFlow, false, true);
		} catch (DatabaseException databaseException) {
			LOG.error("Error al obtener evaluadores viables", databaseException);
			throw new BusinessException("Error al obtener evaluadores viables", databaseException);
		} catch (EmptyResultException emptyResultException) {
			LOG.error("Error no existen evaluador disponible", emptyResultException);
			throw new BusinessException("Error no existe evaluador disponible", emptyResultException);
		} 
	}
	
	public Boolean findIsTrayUserFiltered(
            final Integer idUser, final FlowPurchasingEnum status, final Integer idFlow) throws BusinessException {
	    try {	        
	        return this.user.findIsTrayUserFiltered(idUser, status, idFlow);
	    } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_RETIRVING_IS_TRAY_FILTERED_ERROR, databaseException);
            throw new BusinessException(MESSAGE_RETIRVING_IS_TRAY_FILTERED_ERROR, databaseException);
        }
	}
	
	public List<User> findDeciderLawyer(final Integer idFlow) throws BusinessException {
	    try {
	        LOG.info("UsersContractBusiness -> findDeciderLawyer");
            return this.user.findDeciderLawyer(idFlow);
        } catch (DatabaseException databaseException) {
            LOG.error(ERROR_MESSAGE_FIND_DECIDER_LAWYER, databaseException);
            throw new BusinessException(ERROR_MESSAGE_FIND_DECIDER_LAWYER, databaseException);
        }
	}
}
