package mx.pagos.admc.contracts.business;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.engineer.utils.date.DateUtils;
import mx.pagos.admc.contracts.interfaces.ContratosCelebradosInterface;
import mx.pagos.admc.contracts.structures.ContratosCelebrados;
import mx.pagos.admc.contracts.structures.FiltrosGrafica;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

@Service
public class ContratosCelebradosBusiness {

	private static final Logger LOG = Logger.getLogger(ContratosCelebradosBusiness.class);
	
	@Autowired
	private ContratosCelebradosInterface celebradosRepo;
	
	public ConsultaList<ContratosCelebrados> obtnerListaContratosCelebrados(FiltrosGrafica params)throws BusinessException{
		ConsultaList<ContratosCelebrados> answer = new ConsultaList<>();
		try {
			List<ContratosCelebrados> res =celebradosRepo.obtnerListaContratosCelebrados(params); 
			res.forEach(e ->{
				//e.getStatus()
				switch (e.getStatus()) {
				case "REQUISITION_CLOSE":
					if(e.getFechaFinContrato()==null || DateUtils.stringToDate("yyyy-MM-dd", e.getFechaFinContrato()).after(new Date()))
						e.setStatus("Vigente");
					else
						e.setStatus("Finalizada");
					break;
				case "CANCELED_CONTRACT":
					e.setStatus("Cancelada");
					break;
				}
				
			});
			answer.setList(res);
			answer.setParam4(celebradosRepo.selectCountPagination(params));
			return answer;
		} catch (DatabaseException e) {
			LOG.error("Error al obtener la lista de solicitudes terminadas", e);
			throw new BusinessException("Error al obtener la lista de solicitudes terminadas", e);
		}
	}
	public ConsultaList<ContratosCelebrados> obtenerTotalContratosCelebrados(FiltrosGrafica params)throws BusinessException{
		ConsultaList<ContratosCelebrados> answer = new ConsultaList<>();
		try {
			List<ContratosCelebrados> res =celebradosRepo.obtnerListaTotalContratosCelebrados(params); 
			res.forEach(e ->{
				//e.getStatus()
				switch (e.getStatus()) {
				case "REQUISITION_CLOSE":
					if(e.getFechaFinContrato()==null || DateUtils.stringToDate("yyyy-MM-dd", e.getFechaFinContrato()).after(new Date()))
						e.setStatus("Vigente");
					else
						e.setStatus("Finalizada");
					break;
				case "CANCELED_CONTRACT":
					e.setStatus("Cancelada");
					break;
				}
				
			});
			answer.setList(res);
			answer.setParam4(celebradosRepo.selectCountPagination(params));
			return answer;
		} catch (DatabaseException e) {
			LOG.error("Error al obtener la lista de solicitudes terminadas", e);
			throw new BusinessException("Error al obtener la lista de solicitudes terminadas", e);
		}
	}
}
