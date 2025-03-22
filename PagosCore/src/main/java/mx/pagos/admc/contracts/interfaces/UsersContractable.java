package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.UserLawyerEvaluator;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;
import mx.pagos.security.structures.User;

public interface UsersContractable {
	List<UserLawyerEvaluator> findAviableLawyersOrEvaluators(final Integer idFlow, final Boolean isLawyer, 
	        final Boolean isEvaluator) throws DatabaseException;
	
	UserLawyerEvaluator findAviableLawyerOrEvaluator(Integer idFlow, final Boolean isLawyer, 
	        final Boolean isEvaluator) throws DatabaseException, EmptyResultException;

    Boolean findIsTrayUserFiltered(Integer idUser, FlowPurchasingEnum status, Integer idFlow) throws DatabaseException;
    
    List<User> findDeciderLawyer(final Integer idFlow) throws DatabaseException;
}
