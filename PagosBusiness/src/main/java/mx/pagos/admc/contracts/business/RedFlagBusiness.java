package mx.pagos.admc.contracts.business;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.pagos.admc.contracts.interfaces.RedFlagable;
import mx.pagos.admc.contracts.structures.RedFlag;


@Service
public class RedFlagBusiness {
private static final Logger LOG = Logger.getLogger(RedFlagBusiness.class);
    
    @Autowired
    private RedFlagable redFlagable;

    public final List<RedFlag> save(RedFlag redFlag, Integer idUsuario) {        
        try {
            LOG.debug("Se guardará: RedFlag");
            if(valid(redFlag)) {
            	redFlag.setIdUser(idUsuario);
            	Integer idRed = this.redFlagable.save(redFlag);  
            	LOG.debug("Guardado exitoso. Se regreso el id " + idRed);
                return findByIdRequisition(redFlag.getIdRequisition());
            }
            return null;
        } catch (Exception e) {
        	 LOG.error("RedFlagBusiness - findByIdRequisition: " + e.getMessage());
            return null;
        }
    }
    
    public final List<RedFlag> findByIdRequisition(final Integer idRequisition) {
        try {                  
            LOG.debug("Se obtendrá la lista: RedFlags por idRequisition " + idRequisition);
            return this.redFlagable.findByIdRequisition(idRequisition);
        } catch (Exception e) {
            LOG.error("RedFlagBusiness - findByIdRequisition: " + e.getMessage());
            return null;
        }
    }
    
    private boolean valid(RedFlag redFlag) {
    	if(redFlag == null) {
    		return false;
    	}
    	if(redFlag.getCommentText() == null || redFlag.getCommentText().trim().isEmpty()) {
    		return false;
    	}
    	if(redFlag.getIdRequisition() == null || redFlag.getIdRequisition() <= 0) {
    		return false;
    	}
    	return true;
    }

}
