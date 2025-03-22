package mx.pagos.admc.service.administration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.pagos.admc.contracts.business.ReportesBusiness;
import mx.pagos.admc.contracts.structures.ClaseGafica;
import mx.pagos.admc.contracts.structures.FiltrosGrafica;
import mx.pagos.admc.contracts.structures.GraficaStatusDTO;
import mx.pagos.admc.contracts.structures.ReporteExcel;
import mx.pagos.admc.contracts.structures.ReporteExcelGrafica;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;

@Controller
public class ReportesService {

	private static final Logger LOG = Logger.getLogger(ReportesService.class);
	private static final String SCAPED_QUOTES = "\"";
	
	@Autowired
	private ReportesBusiness repoBusiness;

	@PostMapping(value =UrlConstants.DIAS_ATTENCION_POR_BANDEJA)
	@ResponseBody
	public final ClaseGafica graficaTiempoTardadoEnPasos(@RequestBody final Integer idRequisition) throws BusinessException {
		return repoBusiness.graficaTiempoTardadoEnPasos(idRequisition);
	}
	@PostMapping(value =UrlConstants.TOTAL_SOLICITUDES_GRAFICA)
	public @ResponseBody ClaseGafica graficaTotalSolicitudesEnPasos(@RequestBody final GraficaStatusDTO idRequisition) throws BusinessException {
		LOG.info("-------------------------------------------------ESTO ES LO QUE LLEGO-----------------------------------------------------------------------");
		LOG.info(idRequisition);
		return repoBusiness.graficaTotalSolicitudesEnPasos(idRequisition);
	}
	@PostMapping(value =UrlConstants.GRAFICA_DE_STATUS)
	@ResponseBody
	public final ClaseGafica graficaPastelStatus(@RequestBody final GraficaStatusDTO params) throws BusinessException{
		return repoBusiness.graficaPastelStatus(params);
	}
	@PostMapping(value =UrlConstants.GRAFICA_DE_AREA)
	@ResponseBody
	public final ClaseGafica graficaPastelArea(@RequestBody final GraficaStatusDTO params) throws BusinessException{
		return repoBusiness.graficaPastelArea(params);
	}
	@PostMapping(value =UrlConstants.GRAFICA_TIEMPOS_DE_BARRAS)
	@ResponseBody
	public final ClaseGafica graficaBarrasTiempos(@RequestBody final GraficaStatusDTO params) throws BusinessException{
		return repoBusiness.graficaBarrasTiempos(params);
	}
	
	@PostMapping(value =UrlConstants.CONTRATOS_CERRADOS_POR_MES)
	@ResponseBody
	public final ClaseGafica contratosCerradosPorMes(@RequestBody final Integer anio) throws BusinessException{
		return repoBusiness.contratosCerradosPorMes(anio);
	}
	
	@GetMapping(value =UrlConstants.LISTA_PARA_FILTROS)
	@ResponseBody
	public final FiltrosGrafica getListaFiltro() throws BusinessException{
		return repoBusiness.getListaFiltro();
	}
	
	@PostMapping(value =UrlConstants.DATOS_GRAFICA_FILTROS)
	@ResponseBody
	public final ClaseGafica graficaPorFiltros(@RequestBody FiltrosGrafica params) throws BusinessException{
		return repoBusiness.graficaPorFiltros(params);
	}
	
	@PostMapping(value =UrlConstants.REPORTE_EXCEL_SOLICITUDES)
	@ResponseBody
	public final List<ReporteExcel> reporteExcelSolicitudes(@RequestBody FiltrosGrafica params) throws BusinessException{
		
		LOG.info("\n------------------------------------------------------\n  reporteExcelSolicitudes() :: Parametros: \n Usuario: "+params.getIdUser()+"\n Proveedor: "+params.getIdSupplier()+"\n Empresa: "+params.getIdCompany());
		return repoBusiness.reporteExcelSolicitudes(params);
	}
	@PostMapping(value =UrlConstants.REPORTE_EXCEL_TIEMPO_POLITICA_FECHAS)
	@ResponseBody
	public final List<ReporteExcel> reporteExcelTiempoPoliticaFechas(@RequestBody GraficaStatusDTO params) throws BusinessException{
//	public final ClaseGafica reporteExcelTiempoPoliticaFechas(@RequestBody GraficaStatusDTO params) throws BusinessException{
		
//		LOG.info("\n------------------------------------------------------\n  reporteExcelSolicitudes() :: Parametros: \n Usuario: "+params.getIdUser()+"\n Proveedor: "+params.getIdSupplier()+"\n Empresa: "+params.getIdCompany());
		return repoBusiness.reporteExcelTiempoPoliticaFechas(params);
//		return repoBusiness.graficaPorFechasEnPasos(params);
	}
	@PostMapping(value =UrlConstants.GENERA_EXCEL_GRAFICA_TOTAL_SOLICITUDES)
	@ResponseBody
	public final String generaExcelGraficaSolicitudes(@RequestBody ConsultaList<ReporteExcelGrafica> req) throws BusinessException{
		String reportName = repoBusiness.generarExcelGraficaSolicitudes(req);
		return reportName != null ? "\"" +  reportName + "\"" : null;
	}
	@PostMapping(value =UrlConstants.GENERA_EXCEL_GRAFICA_TOTAL_SOLICITUDES_CERRADAS_ANIO)
	@ResponseBody
	public final String generaExcelGraficaSolicitudesCerradas(@RequestBody ConsultaList<ReporteExcelGrafica> req) throws BusinessException{
		String reportName = repoBusiness.generarExcelGraficaSolicitudesCerradas(req);
		return reportName != null ? "\"" +  reportName + "\"" : null;
	}
	@PostMapping(value =UrlConstants.GENERA_EXCEL_FINALIZADOS)
	@ResponseBody
	public final String generarExcelFinalizados(@RequestBody List<ReporteExcel> params) throws BusinessException{
		String reportName = repoBusiness.generarExcelFinalizados(params);
		return reportName != null ? "\"" +  reportName + "\"" : null;
	}
	@PostMapping(value =UrlConstants.GENERA_EXCEL_SOLICITUDES)
	@ResponseBody
	public final String generaExcelSolicitudes(@RequestBody List<ReporteExcel> params) throws BusinessException{
		String reportName = repoBusiness.generarExcelSolicitudes(params);
		return reportName != null ? "\"" +  reportName + "\"" : null;
	}
	@PostMapping(value =UrlConstants.GENERA_EXCEL_AREA_SELECCIONADA_PASTEL)
	@ResponseBody
	public final String generaExcelAreaSeleccionada(@RequestBody ConsultaList<ReporteExcelGrafica> req) throws BusinessException{
		String reportName = repoBusiness.generarExcelAreaSeleccionada(req);
		return reportName != null ? "\"" +  reportName + "\"" : null;
	}
	@PostMapping(value =UrlConstants.GENERA_EXCEL_TIEMPO_POLITICA)
	@ResponseBody
	public final String generaExcelTiempo(@RequestBody ConsultaList<ReporteExcelGrafica> req) throws BusinessException{
		String reportName = repoBusiness.generarExcelTiempoPolitica(req);
		return reportName != null ? "\"" +  reportName + "\"" : null;
	}
	
	@PostMapping(value = UrlConstants.GENERAR_REPORTE_EXCEL_GRAFICA)
    @ResponseBody
    public final String descargarReporteGrafica(@RequestBody ConsultaList<ReporteExcelGrafica> req) throws BusinessException {
	   System.out.println("Entro al metodo...general excel");
       String nombreReporte = repoBusiness.generarReporteGrafica(req);
       return nombreReporte != null ? "\"" +  nombreReporte + "\"" : null; 
    }
	@PostMapping(value = UrlConstants.GENERAR_REPORTE_EXCEL_GRAFICA_PASTEL_AREAS)
    @ResponseBody
    public final String descargarReporteGraficaPastelAreas(@RequestBody ConsultaList<ReporteExcelGrafica> req) throws BusinessException {
	   System.out.println("Entro al metodo...general excel");
       String nombreReporte = repoBusiness.generarReporteGraficaPastelAreas(req);
       return nombreReporte != null ? "\"" +  nombreReporte + "\"" : null; 
    }
	@RequestMapping(value = UrlConstants.DESCARGAR_REPORTE_EXCEL, method = RequestMethod.GET)
	public final void descargarBorradorContratoPDF(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			final String reportPath = this.repoBusiness.getReportPath();
			final File document =  new File(reportPath + "/Reportes/" + request.getParameter("draftName"));
			
			this.setReponseData(response, document.getName(), document);
			final FileInputStream fileInputStream = new FileInputStream(document);
			final ServletOutputStream servletOutputStream = response.getOutputStream();
			IOUtils.copy(fileInputStream, servletOutputStream);
			fileInputStream.close();
			servletOutputStream.flush();
			servletOutputStream.close();
			
		} catch (BusinessException | IOException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}
	
	private void setReponseData(final HttpServletResponse response, final String name, final File file) {
		final int buffer = 1024 * 100;
		response.setContentType("application/x-download");
		response.setHeader("Content-Disposition", "attachment;filename=" + SCAPED_QUOTES + name + SCAPED_QUOTES);
		response.setContentLength(Long.valueOf(file.length()).intValue());
		response.setBufferSize(buffer);
	}
	

	
}
