package mx.pagos.admc.service.administration;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.pagos.admc.contracts.business.UnitBusiness;
import mx.pagos.admc.contracts.structures.Unit;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;

import org.apache.log4j.Logger;

@Controller
public class UnitService {
	private static final Logger LOG = Logger.getLogger(UnitService.class);
	@Autowired
	UnitBusiness unitBusiness;
	
	
	@PostMapping(value= UrlConstants.SAVE_OR_UPDATE_UNIT)
	@ResponseBody
	public final Integer saveOrUpdate(@RequestBody final Unit unit, final HttpServletResponse response)   {	
		Integer idUnit = 0;
		try {
			idUnit = unitBusiness.saveOrUpdate(unit);
			LOG.debug("Guardado de unidad exitoso " + idUnit);
		} catch (BusinessException e) {
			LOG.error(e.getMessage(), e);
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
	        response.setHeader(Constants.HEADER_MESSAGE, e.getMessage());
		}
		return idUnit;
	}
	
	@PostMapping(value=UrlConstants.FIND_UNIT_BY_ID)
	@ResponseBody
	public final Unit getUnitById(@RequestBody final Integer idUnit, final HttpServletResponse response) {
		Unit answer =null;
		try {
			answer = unitBusiness.getUnitById(idUnit);
			LOG.debug("Unidad recuperada con exito " + idUnit);
		} catch (BusinessException e) {
			LOG.error(e.getMessage(), e);
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
	        response.setHeader(Constants.HEADER_MESSAGE, e.getMessage());
		}
		
		return answer;
	}
	
	@GetMapping(value= UrlConstants.FIND_ALL_UNITS)
	@ResponseBody
	public final List<Unit> getAll(final HttpServletResponse response) {
		List<Unit> answer =null;
		try {
			answer = unitBusiness.getAll();
			LOG.debug("Lista de unidades recuperada con exito ");
		} catch (BusinessException e) {
			LOG.error(e.getMessage(), e);
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
	        response.setHeader(Constants.HEADER_MESSAGE, e.getMessage());
		}
		
		return answer;
	}
	
	@PostMapping(value=UrlConstants.DELETE_UNIT)
	@ResponseBody
	public final Integer deleteUnitById(@RequestBody final Integer idUnit, final HttpServletResponse response) {
		Integer id = 0;
		try {
			id = unitBusiness.deleteUnitById(idUnit);
			LOG.debug("Eliminado de unidad exitoso " + idUnit);
		} catch (BusinessException e) {
			LOG.error(e.getMessage(), e);
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
	        response.setHeader(Constants.HEADER_MESSAGE, e.getMessage());
		}
		return id;
	}

}
