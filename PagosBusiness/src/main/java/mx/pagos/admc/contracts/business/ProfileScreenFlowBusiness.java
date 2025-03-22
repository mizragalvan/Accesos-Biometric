package mx.pagos.admc.contracts.business;

import java.util.List;

import mx.pagos.admc.contracts.interfaces.ProfileScreenFlowable;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.security.structures.ProfileScreenFlow;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class ProfileScreenFlowBusiness {
    private static final Logger LOG = Logger.getLogger(ProfileScreenFlowBusiness.class);
    
    @Autowired
    private ProfileScreenFlowable dao;

    public final List<ProfileScreenFlow> findFlowScreenActionByProfile(final ProfileScreenFlow bean)
            throws BusinessException {
        try {
            return this.dao.findFlowScreenActionByProfile(bean); 
        } catch (DataAccessException | DatabaseException dataBaseException) {
            LOG.error(dataBaseException.getMessage(), dataBaseException);
            throw new BusinessException("Error al obtener la informacion", dataBaseException);
        }
    }
}
