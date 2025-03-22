package mx.pagos.admc.service.administration;

import javax.servlet.http.HttpServletResponse;

import mx.pagos.admc.contracts.business.PersonalitiesBusiness;
import mx.pagos.admc.contracts.structures.Personality;
import mx.pagos.admc.contracts.structures.RequiredDocument;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PersonalityService {
    private static final Logger LOG = Logger.getLogger(PersonalityService.class);
    
    @Autowired
    private PersonalitiesBusiness personalitiesBusiness;
    
    @RequestMapping (value = UrlConstants.PERSONALITY_FIND_BY_STATUS, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Personality> findByStatus(@RequestBody final ConsultaList<Personality>  personality, 
            final HttpServletResponse response) {
        try {
            LOG.info("Se va a consultar por estatus: " + personality.getParam1());
            final ConsultaList<Personality> listResponse = new ConsultaList<Personality>();
            listResponse.setList(this.personalitiesBusiness.findByStatus(
                    RecordStatusEnum.valueOf(personality.getParam1())));
            return listResponse;
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<Personality>();
    }
    
    @RequestMapping (value = UrlConstants.PERSONALITY_FIND_ACTIVE, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Personality> findByStatus(final HttpServletResponse response) {
        try {
            final ConsultaList<Personality> listResponse = new ConsultaList<Personality>();
            listResponse.setList(this.personalitiesBusiness.findByStatus(RecordStatusEnum.ACTIVE));
            return listResponse;
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<Personality>();
    }
    
    @RequestMapping (value = UrlConstants.PERSONALITY_FIND_REQUIRED_DOCUMENT, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<RequiredDocument> findRequiredDocumentByPersonality(
            @RequestBody final ConsultaList<RequiredDocument> requiredDocument, final HttpServletResponse response) {
        try {
            LOG.info("Se va a obtener documentos requeridos" + requiredDocument.getParam4());
            final ConsultaList<RequiredDocument> listResponse = new ConsultaList<RequiredDocument>();
            listResponse.setList(this.personalitiesBusiness.findRequiredDocumentByPersonality(
                    requiredDocument.getParam4()));
            
            LOG.info("PERSONALITY_FIND_REQUIRED_DOCUMENT");
            for (RequiredDocument doc : listResponse.getList()) {
				LOG.info("DOC ("+doc.getIsRequired()+") ::  "+doc.getName());
			}
            return listResponse;
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<RequiredDocument>();
    }
}
