package mx.pagos.admc.contracts.business;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.engineer.utils.date.DateUtils;
import mx.pagos.admc.contracts.interfaces.Reportes;
import mx.pagos.admc.contracts.interfaces.export.AbstractExportable;
import mx.pagos.admc.contracts.structures.Area;
import mx.pagos.admc.contracts.structures.ClaseGafica;
import mx.pagos.admc.contracts.structures.FiltrosGrafica;
import mx.pagos.admc.contracts.structures.FinancialEntity;
import mx.pagos.admc.contracts.structures.GraficaStatusDTO;
import mx.pagos.admc.contracts.structures.ReporteExcel;
import mx.pagos.admc.contracts.structures.ReporteExcelGrafica;
import mx.pagos.admc.contracts.structures.Requisition;
import mx.pagos.admc.contracts.structures.RequisitionStatusTurn;
import mx.pagos.admc.contracts.structures.Supplier;
import mx.pagos.admc.core.interfaces.Configurable;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.security.business.UsersBusiness;
import mx.pagos.security.structures.User;
import mx.pagos.security.structures.UserSession;

@Service("ReporteServices")
public class ReportesBusiness extends AbstractExportable{

	@Autowired
	private Reportes reportesDAO;

	@Autowired
	private UsersBusiness usersBusiness;

	@Autowired
	Configurable configurable;

	@Autowired
	private UserSession session;

//	String[] etiquetas= {
//			"Solicitud", "Revisión Contrato Solicitado","Contratos VoBo Jurídico", "VoBo Jurídico",
//			"Firmas","Digitalización"
//	};//impresion contrato

//	String[] status = {"DRAFT_GENERATION","NEGOTIATOR_CONTRACT_REVIEW",
//			"LOAD_SUPPLIER_AREAS_APPROVAL", "APROVED_BY_JURISTIC", 
//			"SACC_SIGN_CONTRACT","SACC_SCAN_CONTRACT"};
	
//	String[] colores = {"rgba(55, 62, 172)", "rgba(75, 111, 176)", "rgba(9, 192, 184)", "rgba(140, 162, 161)",
//		      "rgba(230, 126, 34)", "rgba(87, 159, 65)", "rgba(3, 171, 33)", "rgba(196, 51, 104)",
//		      "rgba( 224, 29, 207)"}; 
	String[] status = {
			"IN_PROGRESS",
			"DRAFT_GENERATION",
			"NEGOTIATOR_CONTRACT_REVIEW",
			"LOAD_SUPPLIER_AREAS_APPROVAL",
			"APROVED_BY_JURISTIC",
			"PRINT_CONTRACT",
			"SACC_SIGN_CONTRACT",
			"SACC_SCAN_CONTRACT",
			"CANCELLED"};
	String[] etiquetas = {
			"Solicitudes por enviar",
			"Solicitud de contrato",
			"Revisión contrato solicitado",
//			"Contratos para VoBo jurídico",
			"Contratos Versión Final",
			"VoBo jurídico",
			"Contratos para impresión",
			"Firmas de contrato final",
			"Digitalización de contrato",
			"Canceladas"};

	String[] months = {"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"};

	private static final Logger LOG = Logger.getLogger(ReportesBusiness.class);

	public  ClaseGafica graficaTiempoTardadoEnPasos(int idRequisition) throws BusinessException {
		try {
			return procesaTiempoTardadoEnPasos(idRequisition);
		} catch (DatabaseException dataBaseException) {

			throw new BusinessException("Error al obtener datos para generar las gráficas", dataBaseException);
		}
	}

	private ClaseGafica procesaTiempoTardadoEnPasos(int idRequisition) throws DatabaseException {
		ClaseGafica answer= new ClaseGafica();
		String[] status = {
	            "DRAFT_GENERATION",//0
	            "NEGOTIATOR_CONTRACT_REVIEW",//1
	            "IN_REVIEW_JURISTIC",//2
	            "LOAD_SUPPLIER_AREAS_APPROVAL",//3
	            "APROVED_BY_JURISTIC",//4
	            "EN_REVISION_JURIDICO",//5
	            "PRINT_CONTRACT",//6
	            "SACC_SIGN_CONTRACT",//7
	            "SACC_SCAN_CONTRACT",//8
	            "REQUISITION_CLOSE",//9
	            "CANCELED_CONTRACT"};
	    String[] etiquetas = {
	            "Solicitud de contrato",
	            "Revisión contrato solicitado",
	            "Revisión contrato solicitado (jurídico)",
	            "Contratos Versión Final",
	            "VoBo jurídico",
	            "Contratos V.F. (Rechazadas)",
	            "Contratos para impresión",
	            "Firmas de contrato final",
	            "Digitalización de contrato",
	            "Canceladas"};
	    int[] datos=new int[status.length];
		int[] datosY = new int[status.length];

		for (int i = 0; i <= status.length-1; i++) {
			datos[i] = this.reportesDAO.getDefaulTimeByStep(status[i]);

			if(i < (status.length-1)) {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				RequisitionStatusTurn inicio = this.reportesDAO.getStartStep(idRequisition, status[i], 1 );
				RequisitionStatusTurn fin = this.reportesDAO.getStartStep(idRequisition, status[i+1], 2);
				System.out.println("position:   "+i);
				try {
					if(inicio !=null && fin ==null && i==1 ) {
						inicio = this.reportesDAO.getStartStep(idRequisition, status[i], 1 );
						fin = this.reportesDAO.getStartStep(idRequisition, status[i+2], 2);
					}else
						if(inicio !=null && fin !=null && i==1 ) {
							inicio = this.reportesDAO.getStartStep(idRequisition, status[i], 1 );
							fin = this.reportesDAO.getStartStep(idRequisition, status[i+1], 2);
						}
					if(inicio !=null && fin !=null && i==2 ) {
						inicio = this.reportesDAO.getStartStep(idRequisition, status[i], 1 );
						fin = this.reportesDAO.getStartStep(idRequisition, status[i-1], 2);
					}
					
					if(inicio !=null && fin ==null && i==4 ) {
						inicio = this.reportesDAO.getStartStep(idRequisition, status[i], 1 );
						fin = this.reportesDAO.getStartStep(idRequisition, status[i+2], 2);
					}else
					
					if(inicio !=null && fin !=null && i==4 ) {
						inicio = this.reportesDAO.getStartStep(idRequisition, status[i], 1 );
						fin = this.reportesDAO.getStartStep(idRequisition, status[i+1], 2);
					}
					
					if(inicio !=null && fin !=null && i==5 ) {
						inicio = this.reportesDAO.getStartStep(idRequisition, status[i], 1 );
						fin = this.reportesDAO.getStartStep(idRequisition, status[i-1], 2);
					}


					if(inicio ==null || fin ==null) 
						datosY[i] =0;
					

					else 

						datosY[i] =(DateUtils.daysBetweenTwoDates(
								formatter.parse(inicio.getTurnDate().substring(0, 10)), 
								formatter.parse(fin.getTurnDate().substring(0, 10))));

					System.out.println("Dias:   "+datosY[i]);
					
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

		answer.setEtiquetas(etiquetas);
		answer.setDatos(datos);
		answer.setDatosY(datosY);
		return answer;

	}

	public  ClaseGafica graficaTotalSolicitudesEnPasos(GraficaStatusDTO params) throws BusinessException {
		try {
			return procesaTotalSolicitudesEnPasos(params);
		} catch (DatabaseException dataBaseException) {

			throw new BusinessException("Error al obtener datos para generar las gráficas", dataBaseException);
		}
	}
	private ClaseGafica procesaTotalSolicitudesEnPasos(GraficaStatusDTO params) throws DatabaseException {
		ClaseGafica answer= new ClaseGafica();
		String[] colores = {"rgba(255, 99, 132)", "rgba(255, 159, 64)", "rgba(255, 205, 86)", "rgba(75, 192, 192)",
			      "rgba(54, 162, 235)", "rgba(153, 102, 255)", "rgba(0, 0, 0)","rgba( 128, 128, 128, 1)", "rgba(55, 62, 172)",
			      "rgba( 75, 111, 176)","rgba( 255, 192, 203, 1)"};
		String[] status = {
	            "DRAFT_GENERATION",
	            "NEGOTIATOR_CONTRACT_REVIEW",
	            "IN_REVIEW_JURISTIC",
	            "LOAD_SUPPLIER_AREAS_APPROVAL",
	            "EN_REVISION_JURIDICO",
	            "APROVED_BY_JURISTIC",
	            "PRINT_CONTRACT",
	            "SACC_SIGN_CONTRACT",
	            "SACC_SCAN_CONTRACT",
	            };
//	            ""};
	    String[] etiquetas = {
	            "Solicitud de contrato",
	            "Revisión contrato solicitado",
	            "Revisión contrato solicitado (Jurídico)",
	            "Contratos Versión Final",
	            "Contratos V.F. (Rechazadas)",
	            "VoBo jurídico",
	            "Contratos para impresión",
	            "Firmas de contrato final",
	            "Digitalización de contrato",
	    		};
//	    		""};
		int[] datos=new int[status.length];
		int[] datosY = new int[status.length];

		try {
		for (int i = 0; i <= status.length-1; i++) {
			datos[i] = this.reportesDAO.getDateTotalStep(status[i],params);
			if(i < (status.length-1)) {

				} 
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		answer.setColores(colores);
		answer.setEtiquetas(etiquetas);
		answer.setDatos(datos);
		answer.setDatosY(datosY);
		return answer;

	}
	public  ClaseGafica graficaPorFechasEnPasos(GraficaStatusDTO params) throws BusinessException {
//		public  List<ReporteExcel> graficaPorFechasEnPasos(GraficaStatusDTO params) throws BusinessException {
		try {
			return procesaFechasEnPasos(params);
		} catch (DatabaseException dataBaseException) {

			throw new BusinessException("Error al obtener datos para generar las gráficas", dataBaseException);
		}
	}
	private ClaseGafica procesaFechasEnPasos(GraficaStatusDTO params) throws DatabaseException {
//		private List<ReporteExcel> procesaFechasEnPasos(GraficaStatusDTO params) throws DatabaseException {
		ClaseGafica answer= new ClaseGafica();
//		String[] colores = {"rgba(255, 99, 132, 0.2)", "rgba(255, 159, 64, 0.2)", "rgba(255, 205, 86, 0.2)", "rgba(75, 192, 192, 0.2)",
//	      "rgba(54, 162, 235, 0.2)", "rgba(153, 102, 255, 0.2)", "rgba(201, 203, 207, 0.2)", "rgba(55, 62, 172, 0.2)",
//	      "rgba( 75, 111, 176, 0.2)"};
		String[] colores = {"rgba(255, 99, 132)", "rgba(255, 159, 64)", "rgba(255, 205, 86)", "rgba(75, 192, 192)",
			      "rgba(54, 162, 235)", "rgba(153, 102, 255)", "rgba(0, 0, 0)","rgba( 128, 128, 128, 1)", "rgba(55, 62, 172)",
			      "rgba( 75, 111, 176)","rgba( 255, 192, 203, 1)"};
		String[] status = {
	            "DRAFT_GENERATION",//"21",
	            "NEGOTIATOR_CONTRACT_REVIEW",//"22",
	            "IN_REVIEW_JURISTIC",//"37",
	            "LOAD_SUPPLIER_AREAS_APPROVAL",//"24",
	            "EN_REVISION_JURIDICO",//"36",
	            "APROVED_BY_JURISTIC",//"34",
	            "PRINT_CONTRACT",//"35",
	            "SACC_SIGN_CONTRACT",//"27",
	            "SACC_SCAN_CONTRACT",//"28",
	            };
//	            ""};
	    String[] etiquetas = {
	            "Solicitud de contrato",
	            "Revisión contrato solicitado",
	            "Revisión contrato solicitado (Jurídico)",
	            "Contratos Versión Final",
	            "Contratos V.F. (Rechazadas)",
	            "VoBo jurídico",
	            "Contratos para impresión",
	            "Firmas de contrato final",
	            "Digitalización de contrato",
//	            "Canceladas",
//	    		"Contrato Cancelado",
//	    		"Solicitud Finalizada"
	    		};
//	    		""};
		int[] datos=new int[status.length];
		int[] datosY = new int[status.length];

		try {
		for (int i = 0; i <= status.length-1; i++) {
			datos[i] = this.reportesDAO.getDateRangoFechas(status[i],params);
			if(i < (status.length-1)) {

				} 
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		answer.setColores(colores);
		answer.setEtiquetas(etiquetas);
		answer.setDatos(datos);
		answer.setDatosY(datosY);
		return answer;

	}



	public final ClaseGafica graficaPastelStatus(GraficaStatusDTO params) throws BusinessException{
		ClaseGafica answer ;	
		try {
 
		    if (params.getFechaInicio() != null) {		        
		        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Mexico_City"));
		        cal.setTime(params.getFechaInicio());
		        
		        params.setMes(cal.get(Calendar.MONTH)+1);
		        params.setAnio(cal.get(Calendar.YEAR));
		    }
		
			
			answer = new ClaseGafica();
			String[] colores = {"rgba(230, 126, 34) ,rgba(55, 62, 172)", "rgba(75, 111, 176)", "rgba(9, 192, 184)", "rgba(140, 162, 161)",
				      "rgba(230, 126, 34)", "rgba(87, 159, 65)", "rgba(3, 171, 33)", "rgba(196, 51, 104)",
				      "rgba( 224, 29, 207)","rgba( 173, 216, 230, 1)","rgba( 218, 165, 32, 1)","rgba( 160, 82, 45, 1)","rgba( 30, 144, 255, 1)",
				      "rgba( 34, 139, 34, 1)","rgba( 186, 85, 211, 1)"}; 
//			String[] status = {
//			        "REQUISITION_CLOSE",
//					"DRAFT_GENERATION",
//					"NEGOTIATOR_CONTRACT_REVIEW",
//					"REQUISITION",
//					"LOAD_SUPPLIER_AREAS_APPROVAL",
//					"APROVED_BY_JURISTIC",
//					"PRINT_CONTRACT",
//					"SACC_SIGN_CONTRACT",
//					"SACC_SCAN_CONTRACT",
//					"CANCELLED"};
			 String[] status = {
	                    "REQUISITION_CLOSE",
	                    "IN_PROGRESS",
	                    "DRAFT_GENERATION",
	                    "NEGOTIATOR_CONTRACT_REVIEW",
	                    "IN_REVIEW_JURISTIC",
	                    "LOAD_SUPPLIER_AREAS_APPROVAL",
	                    "EN_REVISION_JURIDICO",
	                    "APROVED_BY_JURISTIC",
	                    "PRINT_CONTRACT",
	                    "SACC_SIGN_CONTRACT",
	                    "SACC_SCAN_CONTRACT",
	                    "CANCELLED"};
			String[] etiquetas = {
			        "Solicitudes Cerradas",
					"Solicitudes por enviar",
					"Solicitud de contrato",
					"Revisión contrato solicitado",
					"Revisión contrato solicitado (Jurídico)",
					"Contratos Versión Final",
					"Contratos V.F. Rechazadas",
					"VoBo jurídico",
					"Contratos para impresión",
					"Firmas de contrato final",
					"Digitalización de contrato",
					"Canceladas"};
			int[] count = new int[status.length];
			for (int i = 0; i < status.length; i++)
				count[i]=this.reportesDAO.graficaPastelStatus(status[i], params);

			answer.setColores(colores);
			answer.setEtiquetas(etiquetas);
			answer.setDatos(count);
		} catch (DatabaseException e) {
			throw new BusinessException("Error al obtener datos para generar las gráficas", e);
		}
		return answer;
	}
	public final ClaseGafica graficaPastelArea(GraficaStatusDTO params) throws BusinessException{
		ClaseGafica answer ;	
		try {
 
		    if (params.getFechaInicio() != null) {		        
		        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Mexico_City"));
		        cal.setTime(params.getFechaInicio());
		        
		        params.setMes(cal.get(Calendar.MONTH)+1);
		        params.setAnio(cal.get(Calendar.YEAR));
		    }
		
			
			answer = new ClaseGafica();
			String[] colores = {
				"rgba(255, 0, 0, 1) ,rgba(0, 255, 0, 1)", "rgba(0, 0, 255, 1)", "rgba(255, 255, 0, 1)", 
				"rgba(255, 0, 255, 1)","rgba(0, 255, 255, 1)", "rgba(128, 0, 0, 1)", "rgba(0, 128, 0, 1)", 
				"rgba(0, 0, 128, 1)","rgba( 128, 128, 0, 1)","rgba( 128, 0, 128, 1)","rgba( 0, 128, 128, 1)",
				"rgba( 255, 165, 0, 1)","rgba( 255, 192, 203, 1)","rgba( 128, 128, 128, 1)","rgba( 255, 255, 255, 1)",
				"rgba( 0, 0, 0, 1)","rgba( 210, 105, 30, 1)","rgba( 255, 20, 147, 1)","rgba( 0, 139, 139, 1)",
				"rgba( 138, 43, 226, 1)","rgba( 244, 164, 96, 1)","rgba( 70, 130, 180, 1)","rgba( 173, 216, 230, 1)",
				"rgba( 218, 165, 32, 1)","rgba( 160, 82, 45, 1)","rgba( 30, 144, 255, 1)","rgba( 34, 139, 34, 1)","rgba( 186, 85, 211, 1)"
				}; 
//			String[] status = {
//			        "REQUISITION_CLOSE",
//					"DRAFT_GENERATION",
//					"NEGOTIATOR_CONTRACT_REVIEW",
//					"REQUISITION",
//					"LOAD_SUPPLIER_AREAS_APPROVAL",
//					"APROVED_BY_JURISTIC",
//					"PRINT_CONTRACT",
//					"SACC_SIGN_CONTRACT",
//					"SACC_SCAN_CONTRACT",
//					"CANCELLED"};
//			 String[] status = {
//	                    "REQUISITION_CLOSE",
//	                    "IN_PROGRESS",
//	                    "DRAFT_GENERATION",
//	                    "NEGOTIATOR_CONTRACT_REVIEW",
//	                    "LOAD_SUPPLIER_AREAS_APPROVAL",
//	                    "APROVED_BY_JURISTIC",
//	                    "PRINT_CONTRACT",
//	                    "SACC_SIGN_CONTRACT",
//	                    "SACC_SCAN_CONTRACT",
//	                    "CANCELLED"};
//			int[] status= {19,9,3,8,12,15,28,21,26,16,18,5,2,6,23,11,20,4,13,17,7,1,10,24,25,27,22,29,14};
//			String[] status = {
			String[] status = {
					"DRAFT_GENERATION",
					"NEGOTIATOR_CONTRACT_REVIEW",
					"IN_REVIEW_JURISTIC",
		            "LOAD_SUPPLIER_AREAS_APPROVAL",
		            "EN_REVISION_JURIDICO",
		            "APROVED_BY_JURISTIC",
		            "PRINT_CONTRACT",
		            "SACC_SIGN_CONTRACT",
		            "SACC_SCAN_CONTRACT",
		            "REQUISITION_CLOSE",
		            "CANCELLED",
		            "CANCELED_CONTRACT",
		            };
			String[] etiquetas = {
					"Solicitud de contrato",					
					"Revisión contrato solicitado",
					"Revisión contrato solicitado (Jurídico)",
			        "Contratos Versión Final",
			        "Contratos V.F. (Rechazadas)",
			        "VoBo jurídico",
			        "Contratos para impresión",					
			        "Firmas de contrato final",
					"Digitalización de contrato",
					"Solicitudes Cerradas",
					"Solicitudes Canceladas",
					"Contrato Cancelado",
					};
//			 String[] etiquetas= {
//					 "Suministros",/*19*/
//					 "Finanzas y Tesorería",/*9*/
//					 "Comercial",/*3*/
//					 "Energía",/*8*/
//					 "Logística",/*12*/
//					 "Metálicos",/*15*/
//					 "Suministros Logística",/*28*/
//					 "Jurídico",/*21*/
//					 "Industrial",/*26*/
//					 "Personas",/*16*/
//					 "Servicios Generales",/*18*/
//					 "Contabilidad",/*5*/
//					 "Comercio Exterior",/*2*/
//					 "Desarrollo de Mercado",/*6*/
//					 "Otro",/*23*/
//					 "Inteligencia de Mercado",/*11*/
//					 "TI",/*20*/
//					 "Cuentas por Cobrar",/*4*/
//					 "Marketing",/*13*/
//					 "Seguridad Patrimonial",/*17*/
//					 "Distribución",/*7*/
//					 "Auditoría Interna",/*1*/
//					 "Ingeniería",/*10*/
//					 "JURIDICO CORPORATIVO",/*24*/
//					 "Comunicación Externa",/*25*/
//					 "Seguridad Industrial",/*27*/
//					 "Dirección General",/*22*/
//					 "Medio Ambiente",/*29*/
//					 "Medio Ambiente y Seguridad Industrial"/*14*/
//			 };
			int[] count = new int[status.length];
			for (int i = 0; i < status.length; i++)
				count[i]=this.reportesDAO.graficaPastelArea(status[i], params);

			answer.setColores(colores);
			answer.setEtiquetas(etiquetas);
			answer.setDatos(count);
		} catch (DatabaseException e) {
			throw new BusinessException("Error al obtener datos para generar las gráficas", e);
		}
		return answer;
	}
	public  ClaseGafica graficaBarrasTiempos(GraficaStatusDTO params) throws BusinessException {
		try {
			return procesaGraficaBarraTiempoEnPasos(params);
		} catch (DatabaseException dataBaseException) {

			throw new BusinessException("Error al obtener datos para generar las gráficas", dataBaseException);
		}
	}
	private ClaseGafica procesaGraficaBarraTiempoEnPasos(GraficaStatusDTO params) throws DatabaseException {
		ClaseGafica answer= new ClaseGafica();
		int idRequisition=params.getIdRequisition();
		String[] status = {
	            "DRAFT_GENERATION",
	            "NEGOTIATOR_CONTRACT_REVIEW",
	            "IN_REVIEW_JURISTIC",
	            "LOAD_SUPPLIER_AREAS_APPROVAL",
	            "EN_REVISION_JURIDICO",
	            "APROVED_BY_JURISTIC",
	            "PRINT_CONTRACT",
	            "SACC_SIGN_CONTRACT",
	            "SACC_SCAN_CONTRACT",
	            "REQUISITION_CLOSE",
	            "CANCELED_CONTRACT"};
	    String[] etiquetas = {
	            "Solicitud de contrato",
	            "Revisión contrato solicitado",
	            "Revisión contrato solicitado (Jurídico)",
	            "Contratos Versión Final",
	            "Contratos V.F. (Rechazadas)",
	            "VoBo jurídico",
	            "Contratos para impresión",
	            "Firmas de contrato final",
	            "Digitalización de contrato",
	            "Canceladas"
	            };
		int[] datos=new int[status.length];
		int[] datosY = new int[status.length];

		for (int i = 0; i <= status.length-1; i++) {
			datos[i] = this.reportesDAO.getDefaulTimeByStep(status[i]);

			if(i < (status.length-1)) {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				RequisitionStatusTurn inicio = this.reportesDAO.getStartStepTime(idRequisition, status[i], 1 );
				RequisitionStatusTurn fin = this.reportesDAO.getStartStepTime(idRequisition, status[i+1], 2);

				try {
					if(inicio ==null || fin ==null) 
						datosY[i] =0;
					else
				
						datosY[i] =(DateUtils.daysBetweenTwoDates(
								formatter.parse(inicio.getTurnDate().substring(0, 10)), 
								formatter.parse(fin.getTurnDate().substring(0, 10))));

					System.out.println("Dias:   "+datosY[i]);

				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

		answer.setEtiquetas(etiquetas);
		answer.setDatos(datos);
		answer.setDatosY(datosY);
		return answer;

	}

	public ClaseGafica contratosCerradosPorMes(int year) throws BusinessException{
		ClaseGafica res = new ClaseGafica();
		try {
			int data[] = new int[months.length];
			for (int i = 0; i <=11; i++) 
				data[i] = reportesDAO.contratosPorMes(year, (i+1)); 

			res.setEtiquetas(months);
			res.setDatos(data);
		} catch (DatabaseException e) {
			throw new BusinessException("Error al obtener datos para generar las gráficas", e);
		} 
		return res;
	}


	public final FiltrosGrafica getListaFiltro() throws BusinessException{
		FiltrosGrafica answer = new FiltrosGrafica();

		try {
			answer.setUsers(reportesDAO.getListaFiltros("USERS", User.class));
			answer.setAreas(reportesDAO.getListaFiltros("AREA", Area.class));
			answer.setSuppliers(reportesDAO.getListaFiltros("SUPPLIER", Supplier.class));
			answer.setCompanies(reportesDAO.getListaFiltros("FINANCIALENTITY", FinancialEntity.class));

		} catch (DatabaseException e) {
			throw new BusinessException("Error al obtener datos para generar las gráficas", e);
		}

		return answer;
	}

	public ClaseGafica graficaPorFiltros(FiltrosGrafica params) throws BusinessException{
		ClaseGafica answer = null;
		try {
			answer =  new ClaseGafica();
			int[] datos = new int[status.length];

			for (int i = 0; i < status.length; i++)
				datos[i] =  reportesDAO.graficaFiltros(status[i],params);
			answer.setDatos(datos);
			answer.setEtiquetas(etiquetas);
		} catch (DatabaseException e) {
			throw new BusinessException("Error al obtener datos para generar las gráficas", e);
		}
		return answer;
	}

	@SuppressWarnings("unchecked")
	public List<ReporteExcel> reporteExcelSolicitudes(FiltrosGrafica params) throws BusinessException {
		try {
//			if (params.getIdArea() == 0) {
//				String areas = "";
//				User user = this.usersBusiness.findByUserId(this.session.getUsuario().getIdUser());				
//				params.setIdArea(user.getIdArea());				
//								
//				for(Integer idArea: user.getAreasReporte()) {
//					areas+=idArea + ",";
//				}
//				areas = areas.substring(0, areas.length()-1);
//				params.setIdsAreas(areas);
//			}		
			LOG.info("  FILTROS  \n Usuario: "+params.getIdUser()+"\n Proveedor: "+params.getIdSupplier()+"\n Empresa: "+params.getIdCompany()
			+"\n Area: "+params.getIdArea());
			List<ReporteExcel> res = reportesDAO.reporteExcelSolicitudes(params);
			LOG.info("REPORTES RECUPERADOS :: "+res.size()+"\n--------------------------------------------------------");
			return res;
		} catch (DatabaseException e) {
			LOG.error("Error al obtener datos para generar el Excel de Solicitudes",e);
			throw new BusinessException("Error al obtener datos para generar el Excel de Solicitudes", e);
		}
	}
	@SuppressWarnings("unchecked")
	public List<ReporteExcel> reporteExcelTiempoPoliticaFechas(GraficaStatusDTO params) throws BusinessException {
		try {
//			if (params.getIdArea() == 0) {
//				String areas = "";
//				User user = this.usersBusiness.findByUserId(this.session.getUsuario().getIdUser());				
//				params.setIdArea(user.getIdArea());				
//								
//				for(Integer idArea: user.getAreasReporte()) {
//					areas+=idArea + ",";
//				}
//				areas = areas.substring(0, areas.length()-1);
//				params.setIdsAreas(areas);
//			}		
//			LOG.info("  FILTROS  \n Usuario: "+params.getIdUser()+"\n Proveedor: "+params.getIdSupplier()+"\n Empresa: "+params.getIdCompany()
//			+"\n Area: "+params.getIdArea());
			List<ReporteExcel> res = reportesDAO.reporteExcelTiempoPoliticaFechas(params);
			LOG.info("REPORTES RECUPERADOS :: "+res.size()+"\n--------------------------------------------------------");
			return res;
		} catch (DatabaseException e) {
			LOG.error("Error al obtener datos para generar el Excel de Solicitudes",e);
			throw new BusinessException("Error al obtener datos para generar el Excel de Solicitudes", e);
		}
	}
	public String generarExcelSolicitudes(List<ReporteExcel> list)  {

		try {
			String[] columns = { "ID", "Nombre Solicitante", "Tipo Documento",
					"Proveedor", "Área","Estatus","Responsable","Fecha Creación","Fecha Última Modificación","Días" };
//			String[] columns = { "ID", "Nombre Solicitante", "Tipo Documento",
//					"Proveedor", "Área","Estatus","Fecha Creación","Fecha Última Modificación" };

			XSSFWorkbook workbook = new XSSFWorkbook();

			XSSFSheet sheet = workbook.createSheet("solicitudes");// creating a blank sheet

			XSSFFont headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 14);
			headerFont.setColor(IndexedColors.BLACK.getIndex());

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);

			Row headerRow = sheet.createRow(0);

			for (int i = 0; i < columns.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(columns[i]);
				cell.setCellStyle(headerCellStyle);
				sheet.autoSizeColumn(i);
			}


			int rownum = 1;
			sheet.setColumnWidth(0, 4500);
			sheet.setColumnWidth(1, 7500);
			sheet.setColumnWidth(2, 7500);
			sheet.setColumnWidth(3, 7500);
			sheet.setColumnWidth(4, 7500);
			sheet.setColumnWidth(5, 7500);
			sheet.setColumnWidth(6, 7500);
			for (ReporteExcel req : list) {
				Row row = sheet.createRow(rownum++);
				createList(req, row);

			}

			String filePath = getReportPath();
			String fileName = "Reporte-Solicitudes" + new Date().getTime() + ".xlsx";
			File report = new File(filePath + "/Reportes/" + fileName);
			FileOutputStream out = new FileOutputStream(report);
			workbook.write(out);
			out.close();

			return fileName;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}
	public String generarExcelFinalizados(List<ReporteExcel> list)  {

		try {
			String[] columns = { "Folio", "Contratista", "RFC",
					"Estatus","Fecha inicio contrato","Fecha fin contrato" };

			XSSFWorkbook workbook = new XSSFWorkbook();

			XSSFSheet sheet = workbook.createSheet("solicitudes");// creating a blank sheet

			XSSFFont headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 14);
			headerFont.setColor(IndexedColors.BLACK.getIndex());

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);

			Row headerRow = sheet.createRow(0);

			for (int i = 0; i < columns.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(columns[i]);
				cell.setCellStyle(headerCellStyle);
				sheet.autoSizeColumn(i);
			}


			int rownum = 1;
			sheet.setColumnWidth(0, 2500);
			sheet.setColumnWidth(1, 12500);
			sheet.setColumnWidth(2, 5500);
			sheet.setColumnWidth(3, 4500);
			sheet.setColumnWidth(4, 7500);
			sheet.setColumnWidth(5, 7500);
			for (ReporteExcel req : list) {
				Row row = sheet.createRow(rownum++);
				createListFinalizados(req, row);

			}

			String filePath = getReportPath();
			String fileName = "Reporte-Contratos-Finalizados" + new Date().getTime() + ".xlsx";
			File report = new File(filePath + "/Reportes/" + fileName);
			FileOutputStream out = new FileOutputStream(report);
			workbook.write(out);
			out.close();

			return fileName;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	public String generarExcelGraficaSolicitudes(ConsultaList<ReporteExcelGrafica> list)  {

		try {
//			String[] columns = { "ID", "Nombre Solicitante", "Tipo Documento",
//					"Proveedor", "Área","Estatus","Fecha Creación","Fecha Última Modificación" };
			 String pngImagenURL = list.getParam1();
		        String encodingPrefix = "base64,";
		        //int contentStartIndex = pngImagenURL.indexOf(encodingPrefix) + encodingPrefix.length();
		        //byte[] imageDate1 = org.apache.commons.codec.binary.Base64.decodeBase64(pngImagenURL.substring(contentStartIndex));
		        
		        String[] columns = list.getList().get(0).getListaColumna();
		        XSSFWorkbook workbook = new XSSFWorkbook();
		        XSSFSheet sheet = workbook.createSheet("Datos");
		        XSSFSheet sheetTwo = workbook.createSheet("Gráfica");

	  
		        //int pictureIdx = workbook.addPicture(imageDate1, workbook.PICTURE_TYPE_PNG);
		        //XSSFDrawing drawing = sheetTwo.createDrawingPatriarch();
		        //XSSFCreationHelper helper = workbook.getCreationHelper();
		        //XSSFClientAnchor anchor = helper.createClientAnchor();
		        //anchor.setCol1(2);
		        //anchor.setCol2(3);
		        //anchor.setRow1(3);
		        //anchor.setRow2(4);

		        //Picture picture = drawing.createPicture(anchor, pictureIdx);
		        //picture.resize();
		        
		        XSSFFont headerFont = workbook.createFont();
	            headerFont.setBold(true);
	            headerFont.setFontHeightInPoints((short) 14);
	            headerFont.setColor(IndexedColors.BLACK.getIndex());
	            
	            CellStyle headerCellStyle = workbook.createCellStyle();
	            headerCellStyle.setFont(headerFont);
	            Row headerRow = sheet.createRow(0);
	            
	            for (int i = 0; i < columns.length; i++) {
	                Cell cell = headerRow.createCell(i);
	                cell.setCellValue(columns[i]);
	                cell.setCellStyle(headerCellStyle);
	                sheet.autoSizeColumn(i);
	            }

	            int rownum = 1;
	            sheet.setColumnWidth(0, 7500);
	            for (ReporteExcelGrafica req : list.getList()) {
	                Row row = sheet.createRow(rownum++);
				 createListGraficaSolicitudes(req, row, list.getList().get(0).getListaColumna());
					LOG.info("generarExcelSolicitudes :: "+"\n--------------------------------------------------------");
			}

			String filePath = getReportPath();
			String fileName = "Reporte-Solicitudes" + new Date().getTime() + ".xlsx";
			File report = new File(filePath + "/Reportes/" + fileName);
			FileOutputStream out = new FileOutputStream(report);
			workbook.write(out);
			out.close();

			return fileName;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info("TRUENA EN REPORTESBUSSNESS");
		}

		return null;

	}
	public String generarExcelGraficaSolicitudesCerradas(ConsultaList<ReporteExcelGrafica> list)  {

		try {
			 String pngImagenURL = list.getParam1();
		        String encodingPrefix = "base64,";
		        
		        String[] columns = list.getList().get(0).getListaColumna();
		        XSSFWorkbook workbook = new XSSFWorkbook();
		        XSSFSheet sheet = workbook.createSheet("Datos");
		        XSSFSheet sheetTwo = workbook.createSheet("Gráfica");

		        XSSFFont headerFont = workbook.createFont();
	            headerFont.setBold(true);
	            headerFont.setFontHeightInPoints((short) 14);
	            headerFont.setColor(IndexedColors.BLACK.getIndex());
	            
	            CellStyle headerCellStyle = workbook.createCellStyle();
	            headerCellStyle.setFont(headerFont);
	            Row headerRow = sheet.createRow(0);
	            
	            for (int i = 0; i < columns.length; i++) {
	                Cell cell = headerRow.createCell(i);
	                cell.setCellValue(columns[i]);
	                cell.setCellStyle(headerCellStyle);
	                sheet.autoSizeColumn(i);
	            }

	            int rownum = 1;
	            sheet.setColumnWidth(0, 7500);
	            for (ReporteExcelGrafica req : list.getList()) {
	                Row row = sheet.createRow(rownum++);
	                createListGraficaSolicitudesCerradas(req, row, list.getList().get(0).getListaColumna());
					LOG.info("generarExcelSolicitudes :: "+"\n--------------------------------------------------------");
			}

			String filePath = getReportPath();
			String fileName = "Reporte-Solicitudes-Cerradas" + new Date().getTime() + ".xlsx";
			File report = new File(filePath + "/Reportes/" + fileName);
			FileOutputStream out = new FileOutputStream(report);
			workbook.write(out);
			out.close();

			return fileName;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info("TRUENA EN REPORTESBUSSNESS");
		}

		return null;

	}
	public String generarExcelAreaSeleccionada(ConsultaList<ReporteExcelGrafica> list)  {

		try {
//			String[] columns = { "ID", "Nombre Solicitante", "Tipo Documento",
//					"Proveedor", "Área","Estatus","Fecha Creación","Fecha Última Modificación" };
			 String pngImagenURL = list.getParam1();
		        String encodingPrefix = "base64,";
		        //int contentStartIndex = pngImagenURL.indexOf(encodingPrefix) + encodingPrefix.length();
		        //byte[] imageDate1 = org.apache.commons.codec.binary.Base64.decodeBase64(pngImagenURL.substring(contentStartIndex));
		        
		        String[] columns = list.getList().get(0).getListaColumna();
		        XSSFWorkbook workbook = new XSSFWorkbook();
		        XSSFSheet sheet = workbook.createSheet("Datos");
		        XSSFSheet sheetTwo = workbook.createSheet("Gráfica");

	  
		        //int pictureIdx = workbook.addPicture(imageDate1, workbook.PICTURE_TYPE_PNG);
		        //XSSFDrawing drawing = sheetTwo.createDrawingPatriarch();
		        //XSSFCreationHelper helper = workbook.getCreationHelper();
		        //XSSFClientAnchor anchor = helper.createClientAnchor();
		        //anchor.setCol1(2);
		        //anchor.setCol2(3);
		        //anchor.setRow1(3);
		        //anchor.setRow2(4);

		        //Picture picture = drawing.createPicture(anchor, pictureIdx);
		        //picture.resize();
		        
		        XSSFFont headerFont = workbook.createFont();
	            headerFont.setBold(true);
	            headerFont.setFontHeightInPoints((short) 14);
	            headerFont.setColor(IndexedColors.BLACK.getIndex());
	            
	            CellStyle headerCellStyle = workbook.createCellStyle();
	            headerCellStyle.setFont(headerFont);
	            Row headerRow = sheet.createRow(0);
	            
	            for (int i = 0; i < columns.length; i++) {
	                Cell cell = headerRow.createCell(i);
	                cell.setCellValue(columns[i]);
	                cell.setCellStyle(headerCellStyle);
	                sheet.autoSizeColumn(i);
	            }

	            int rownum = 1;
	            sheet.setColumnWidth(0, 7500);
	            for (ReporteExcelGrafica req : list.getList()) {
	                Row row = sheet.createRow(rownum++);
	                createListGraficaAreaSeleccionada(req, row, list.getList().get(0).getListaColumna());
					LOG.info("generarExcelSolicitudes :: "+"\n--------------------------------------------------------");
			}

			String filePath = getReportPath();
			String fileName = "Reporte-Solicitudes" + new Date().getTime() + ".xlsx";
			File report = new File(filePath + "/Reportes/" + fileName);
			FileOutputStream out = new FileOutputStream(report);
			workbook.write(out);
			out.close();

			return fileName;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}
	public String generarExcelTiempoPolitica(ConsultaList<ReporteExcelGrafica> list)  {
		LOG.info("generarExcelSolicitudes :: "+"\n--------------------------------------------------------");
		LOG.info("Lista :: "+list.getList().size());
		try {
			 String pngImagenURL = list.getParam1();
		        String encodingPrefix = "base64,";
		     
		        String[] columns = list.getList().get(0).getListaColumna();
		        XSSFWorkbook workbook = new XSSFWorkbook();
		        XSSFSheet sheet = workbook.createSheet("Datos");
		        XSSFSheet sheetTwo = workbook.createSheet("Gráfica");

	  
		    
		        
		        XSSFFont headerFont = workbook.createFont();
	            headerFont.setBold(true);
	            headerFont.setFontHeightInPoints((short) 14);
	            headerFont.setColor(IndexedColors.BLACK.getIndex());
	            
	            CellStyle headerCellStyle = workbook.createCellStyle();
	            headerCellStyle.setFont(headerFont);
	            Row headerRow = sheet.createRow(0);
	            
	            for (int i = 0; i < columns.length; i++) {
	                Cell cell = headerRow.createCell(i);
	                cell.setCellValue(columns[i]);
	                cell.setCellStyle(headerCellStyle);
	                sheet.autoSizeColumn(i);
	            }

	            int rownum = 1;
	            sheet.setColumnWidth(0, 7500);
	            for (ReporteExcelGrafica req : list.getList()) {
	                Row row = sheet.createRow(rownum++);
	                createListGraficaTiempoPolitica(req, row, list.getList().get(0).getListaColumna());
					LOG.info("generarExcelSolicitudes :: "+"\n--------------------------------------------------------");
			}

			String filePath = getReportPath();
			String fileName = "Reporte-Solicitudes" + new Date().getTime() + ".xlsx";
			File report = new File(filePath + "/Reportes/" + fileName);
			FileOutputStream out = new FileOutputStream(report);
			workbook.write(out);
			out.close();

			return fileName;
		} catch (Exception e) {
			LOG.error("\n--------------------------------------------------------"+"error en "+"generarExcelSolicitudes :: "+"\n--------------------------------------------------------");
			e.printStackTrace();
		}

		return null;

	}
	public String generarReporteGrafica(ConsultaList<ReporteExcelGrafica> lista) {
	    try {
	        String pngImagenURL = lista.getParam1();
	        String encodingPrefix = "base64,";
	        //int contentStartIndex = pngImagenURL.indexOf(encodingPrefix) + encodingPrefix.length();
	        //byte[] imageDate1 = org.apache.commons.codec.binary.Base64.decodeBase64(pngImagenURL.substring(contentStartIndex));
	        
	        String[] columns = lista.getList().get(0).getListaColumna();
	        XSSFWorkbook workbook = new XSSFWorkbook();
	        XSSFSheet sheet = workbook.createSheet("Datos");
	        XSSFSheet sheetTwo = workbook.createSheet("Gráfica");

  
	        //int pictureIdx = workbook.addPicture(imageDate1, workbook.PICTURE_TYPE_PNG);
	        //XSSFDrawing drawing = sheetTwo.createDrawingPatriarch();
	        //XSSFCreationHelper helper = workbook.getCreationHelper();
	        //XSSFClientAnchor anchor = helper.createClientAnchor();
	        //anchor.setCol1(2);
	        //anchor.setCol2(3);
	        //anchor.setRow1(3);
	        //anchor.setRow2(4);

	        //Picture picture = drawing.createPicture(anchor, pictureIdx);
	        //picture.resize();
	        
	        XSSFFont headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 14);
            headerFont.setColor(IndexedColors.BLACK.getIndex());
            
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            Row headerRow = sheet.createRow(0);
            
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
                sheet.autoSizeColumn(i);
            }
            int rownum = 1;
            sheet.setColumnWidth(0, 7500);
            for (ReporteExcelGrafica req : lista.getList()) {
                Row row = sheet.createRow(rownum++);
                createListGrafica(req, row, lista.getList().get(0).getListaColumna());
            }
                        
	        String filePath = getReportPath();
	        String fileName = "Reporte-Grafica" + new Date().getTime() + ".xlsx";
	        File report = new File(filePath + "/Reportes/" + fileName);
	        FileOutputStream out = new FileOutputStream(report);
	        workbook.write(out);
	        out.close();

	        return fileName;
	    } catch (Exception e) {
			e.printStackTrace();
        }
	    return null;
	    
	}

	public String generarReporteGraficaPastelAreas(ConsultaList<ReporteExcelGrafica> lista) {
	    try {
	        String pngImagenURL = lista.getParam1();
	        String encodingPrefix = "base64,";
	        //int contentStartIndex = pngImagenURL.indexOf(encodingPrefix) + encodingPrefix.length();
	        //byte[] imageDate1 = org.apache.commons.codec.binary.Base64.decodeBase64(pngImagenURL.substring(contentStartIndex));
	        
	        String[] columns = lista.getList().get(0).getListaColumna();
	        XSSFWorkbook workbook = new XSSFWorkbook();
	        XSSFSheet sheet = workbook.createSheet("Datos");
	        XSSFSheet sheetTwo = workbook.createSheet("Gráfica");

  
	        //int pictureIdx = workbook.addPicture(imageDate1, workbook.PICTURE_TYPE_PNG);
	        //XSSFDrawing drawing = sheetTwo.createDrawingPatriarch();
	        //XSSFCreationHelper helper = workbook.getCreationHelper();
	        //XSSFClientAnchor anchor = helper.createClientAnchor();
	        //anchor.setCol1(2);
	        //anchor.setCol2(3);
	        //anchor.setRow1(3);
	        //anchor.setRow2(4);

	        //Picture picture = drawing.createPicture(anchor, pictureIdx);
	        //picture.resize();
	        
	        XSSFFont headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 14);
            headerFont.setColor(IndexedColors.BLACK.getIndex());
            
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            Row headerRow = sheet.createRow(0);
            
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerCellStyle);
                sheet.autoSizeColumn(i);
            }
            int rownum = 1;
            sheet.setColumnWidth(0, 7500);
            for (ReporteExcelGrafica req : lista.getList()) {
                Row row = sheet.createRow(rownum++);
                createListGraficaPastelAreas(req, row, lista.getList().get(0).getListaColumna());
            }
                        
	        String filePath = getReportPath();
	        String fileName = "Reporte-Grafica" + new Date().getTime() + ".xlsx";
	        File report = new File(filePath + "/Reportes/" + fileName);
	        FileOutputStream out = new FileOutputStream(report);
	        workbook.write(out);
	        out.close();

	        return fileName;
	    } catch (Exception e) {
			e.printStackTrace();
        }
	    return null;
	    
	}
	private void createList(ReporteExcel req, Row row) {
		Cell cell = row.createCell(0);
		cell.setCellValue(req.getIdRequisition());

		cell = row.createCell(1);
		cell.setCellValue(req.getNombreSolicitante());

		cell = row.createCell(2);
		cell.setCellValue(req.getTipoDocumento());

		cell = row.createCell(3);
		cell.setCellValue(req.getNombreProveedor());

		cell = row.createCell(4);
		cell.setCellValue(req.getArea());

		cell = row.createCell(5);
		cell.setCellValue(req.getStatus());
		
		cell = row.createCell(6);
		cell.setCellValue(req.getNombreSolicitud());
		
		cell = row.createCell(7);
		cell.setCellValue(req.getFechaCreacion());

		cell = row.createCell(8);
		cell.setCellValue(req.getFechaUltimaModificacion());

//		cell = row.createCell(6);
//		cell.setCellValue(req.getFechaCreacion());
//
//		cell = row.createCell(7);
//		cell.setCellValue(req.getFechaUltimaModificacion());
		
//		cell = row.createCell(9);
//		cell.setCellValue(req.getAttentiondays());
		cell = row.createCell(9);
		cell.setCellValue(req.getTotaldias());
	}
	private void createListFinalizados(ReporteExcel req, Row row) {
		Cell cell = row.createCell(0);
		cell.setCellValue(req.getIdRequisition());

		cell = row.createCell(1);
		cell.setCellValue(req.getNombreProveedor());

		cell = row.createCell(2);
		cell.setCellValue(req.getRfc());

		cell = row.createCell(3);
		cell.setCellValue(req.getStatus());

		cell = row.createCell(4);
		cell.setCellValue(req.getFechaInicioContrato());

		cell = row.createCell(5);
		cell.setCellValue(req.getFechaFinContrato());
		
	}
	 private void createListGraficaSolicitudes(ReporteExcelGrafica req, Row row, String[] columna) {
	        Cell cellv = row.createCell(0);
	        cellv.setCellValue(req.getStatus());
	  
	        if (req.getTotalSolicitudes() != null) {
	            Cell cell = row.createCell(this.getPosition("Total Solicitudes", columna));
	            cell.setCellValue(req.getTotalSolicitudes());
	            LOG.info("createListGraficaSolicitudes :: "+"\n-------------------------SUSSEFULL-------------------------------");
	        }
	 }
	 private void createListGraficaSolicitudesCerradas(ReporteExcelGrafica req, Row row, String[] columna) {
	        Cell cellv = row.createCell(0);
	        cellv.setCellValue(req.getStatus());
	        LOG.info("req.getTotal() :: "+req.getTotal());
	        if (req.getTotalSolicitudes() != null) {
	            Cell cell = row.createCell(this.getPosition("Solicitudes Cerradas", columna));
	            cell.setCellValue(req.getTotalSolicitudes());
	            LOG.info("createListGraficaSolicitudesCerradas :: "+"\n-------------------------SUSSEFULL getTotalSolicitudes()-------------------------------");
	        }
	            Cell cell = row.createCell(this.getPosition("Total Solicitudes Cerradas", columna));
	            cell.setCellValue(req.getTotal());
	            LOG.info("createListGraficaSolicitudesCerradas :: "+"\n-------------------------SUSSEFULL getTotal()-------------------------------");

	 }
	 private void createListGraficaAreaSeleccionada(ReporteExcelGrafica req, Row row, String[] columna) {
	        Cell cellv = row.createCell(0);
	        cellv.setCellValue(req.getStatus());
	  
	        if (req.getTotalSolicitudes() != null) {
	            Cell cell = row.createCell(this.getPosition("Solicitudes/Bandeja", columna));
	            cell.setCellValue(req.getTotalSolicitudes());
	            LOG.info("createListGraficaSolicitudes :: "+"\n-------------------------SUSSEFULL-------------------------------");
	        }
	        if (req.getPorcentaje() != null) {
	            Cell cell = row.createCell(this.getPosition("Porcentaje", columna));
	            cell.setCellValue(req.getPorcentaje());
	            LOG.info("createListGraficaSolicitudes :: "+"\n-------------------------SUSSEFULL-------------------------------");
	        }
	        if (req.getSumaSolicitudes() != null) {
	            Cell cell = row.createCell(this.getPosition("Total Solicitudes", columna));
	            cell.setCellValue(req.getSumaSolicitudes());
	            LOG.info("createListGraficaSolicitudes :: "+"\n-------------------------SUSSEFULL-------------------------------");
	        }
	 }
	 private void createListGraficaTiempoPolitica(ReporteExcelGrafica req, Row row, String[] columna) {
	        Cell cellv = row.createCell(0);
	        cellv.setCellValue(req.getStatus());
	  
	        if (req.getTiempoEstablecido() != null) {
	            Cell cell = row.createCell(this.getPosition("Tiempo Establecido", columna));
	            cell.setCellValue(req.getTiempoEstablecido());
	            LOG.info("createListGraficaSolicitudes :: "+"\n-------------------------SUSSEFULL-------------------------------");
	        }
	        if (req.getTiempoSolucion() != null) {
	            Cell cell = row.createCell(this.getPosition("Tiempo Solución", columna));
	            cell.setCellValue(req.getTiempoSolucion());
	            LOG.info("createListGraficaSolicitudes :: "+"\n-------------------------SUSSEFULL-------------------------------");
	        }
	 }
    private void createListGrafica(ReporteExcelGrafica req, Row row, String[] columna) {
        Cell cellv = row.createCell(0);
        cellv.setCellValue(req.getStatus());
        
        if (req.getAuditoriaInterna() != null) {
            Cell cell = row.createCell(this.getPosition("Auditoría Interna", columna));
            cell.setCellValue(req.getAuditoriaInterna());
        }
        if (req.getComercioExterior() != null) {
            Cell cell = row.createCell(this.getPosition("Comercio Exterior", columna));
            cell.setCellValue(req.getComercioExterior());
        }
        if (req.getComercial() != null) {
            Cell cell = row.createCell(this.getPosition("Comercial", columna));
            cell.setCellValue(req.getComercial());
        }
        if (req.getCuentasCobrar() != null) {
           Cell cell = row.createCell(this.getPosition("Cuentas por Cobrar", columna));
           cell.setCellValue(req.getCuentasCobrar());
        }
        if (req.getContabilidad() != null) {
            Cell cell = row.createCell(this.getPosition("Contabilidad", columna));
            cell.setCellValue(req.getContabilidad());
        }
        
        if (req.getDesarrolloDeMercado() != null) {
            Cell cell = row.createCell(this.getPosition("Desarrollo de Mercado", columna));
            cell.setCellValue(req.getDesarrolloDeMercado());
        }
        
        if (req.getDistribucion() != null) {
            Cell cell = row.createCell(this.getPosition("Distribución", columna));
            cell.setCellValue(req.getDistribucion());
        }
        
        if (req.getEnergia() != null) {
            Cell cell = row.createCell(this.getPosition("Energía", columna));
            cell.setCellValue(req.getEnergia());
        }
        
        if (req.getFinanzasyTesoreria() != null) {
            Cell cell = row.createCell(this.getPosition("Finanzas y Tesorería", columna));
            cell.setCellValue(req.getFinanzasyTesoreria());
        }
        if (req.getIngenieria() != null) {
            Cell cell = row.createCell(this.getPosition("Ingeniería", columna));
            cell.setCellValue(req.getIngenieria());
        }
        
        if (req.getInteligenciaMercado() != null) {
            Cell cell = row.createCell(this.getPosition("Inteligencia de Mercado", columna));
            cell.setCellValue(req.getInteligenciaMercado());
        }
        
        if (req.getLogistica() != null) {
            Cell cell = row.createCell(this.getPosition("Logística", columna));
            cell.setCellValue(req.getLogistica());
        }
        
        if (req.getMarketing() != null) {
            Cell cellLogis = row.createCell(this.getPosition("Marketing", columna));
            cellLogis.setCellValue(req.getMarketing());
        }
        
        if (req.getMedioAmbiSeguridadIndu() != null) {
            Cell cell = row.createCell(this.getPosition("Medio Ambiente y Seguridad Industrial", columna));
            cell.setCellValue(req.getMedioAmbiSeguridadIndu());
        }
        
        if (req.getMetalicos() != null) {
            Cell cell = row.createCell(this.getPosition("Metálicos", columna));
            cell.setCellValue(req.getMetalicos());
        }
        
        if (req.getPersonas() != null) {
            Cell cell = row.createCell(this.getPosition("Personas", columna));
            cell.setCellValue(req.getPersonas());
        }
        
        if (req.getSeguridadPatrinomial() != null) {
            Cell cell = row.createCell(this.getPosition("Seguridad Patrimonial", columna));
            cell.setCellValue(req.getSeguridadPatrinomial());
        }
        
        if (req.getServiciosGenerales() != null) {
            Cell cell = row.createCell(this.getPosition("Servicios Generales", columna));
            cell.setCellValue(req.getServiciosGenerales());
        }
        
        if (req.getSuministros() != null) {
            Cell cell = row.createCell(this.getPosition("Suministros", columna));
            cell.setCellValue(req.getSuministros());
        }
        
        if (req.getTi() != null) {
            Cell cellIndustri = row.createCell(this.getPosition("TI", columna));
            cellIndustri.setCellValue(req.getTi());
        } 
        
        if (req.getJuridico() != null) {
            Cell cellJurid = row.createCell(this.getPosition("Jurídico", columna));
            cellJurid.setCellValue(req.getJuridico());
        }
        
        if (req.getDireccionGeneral() != null) {
            Cell cellIndustri = row.createCell(this.getPosition("Dirección General", columna));
            cellIndustri.setCellValue(req.getDireccionGeneral());
        } 
        
        if (req.getOtro() != null) {
            Cell cell = row.createCell(this.getPosition("Otro", columna));
            cell.setCellValue(req.getOtro());
        }
        
        if (req.getJuridicoCorparativo() != null) {
            Cell cell = row.createCell(this.getPosition("JURIDICO CORPORATIVO", columna));
            cell.setCellValue(req.getJuridicoCorparativo());
        }
        
        if (req.getComunicacionExterna() != null) {
            Cell cell = row.createCell(this.getPosition("Comunicación Externa", columna));
            cell.setCellValue(req.getComunicacionExterna());
        }
       
        if (req.getIndustrial() != null) {
            Cell cellIndustri = row.createCell(this.getPosition("Industrial", columna));
            cellIndustri.setCellValue(req.getIndustrial());
        } 
       
        if (req.getSeguridadIndustrial() != null) {
            Cell cellSeguridad = row.createCell(this.getPosition("Seguridad Industrial", columna));
            cellSeguridad.setCellValue(req.getSeguridadIndustrial());
        }
        
        if (req.getSuministrosLogística() != null) {
            Cell cellSuminLogi = row.createCell(this.getPosition("Suministros Logística", columna));
            cellSuminLogi.setCellValue(req.getSuministrosLogística());
        }
        
        if (req.getMedioAmbiente() != null) {
            Cell cellAudInte = row.createCell(this.getPosition("Medio Ambiente", columna));
            cellAudInte.setCellValue(req.getMedioAmbiente());
        }
      
        if (req.getTotal() != null) {
            Cell cellTotal = row.createCell(this.getPosition("Total", columna));
            cellTotal.setCellValue(req.getTotal());
        }
    }
    private void createListGraficaPastelAreas(ReporteExcelGrafica req, Row row, String[] columna) {
        Cell cellv = row.createCell(0);
        cellv.setCellValue(req.getStatus());
        
        if (req.getAuditoriaInterna() != null) {
            Cell cell = row.createCell(this.getPosition("Auditoría Interna", columna));
            cell.setCellValue(req.getAuditoriaInterna());
        }
        if (req.getComercioExterior() != null) {
            Cell cell = row.createCell(this.getPosition("Comercio Exterior", columna));
            cell.setCellValue(req.getComercioExterior());
        }
        if (req.getComercial() != null) {
            Cell cell = row.createCell(this.getPosition("Comercial", columna));
            cell.setCellValue(req.getComercial());
        }
        if (req.getCuentasCobrar() != null) {
           Cell cell = row.createCell(this.getPosition("Cuentas por Cobrar", columna));
           cell.setCellValue(req.getCuentasCobrar());
        }
        if (req.getContabilidad() != null) {
            Cell cell = row.createCell(this.getPosition("Contabilidad", columna));
            cell.setCellValue(req.getContabilidad());
        }
        
        if (req.getDesarrolloDeMercado() != null) {
            Cell cell = row.createCell(this.getPosition("Desarrollo de Mercado", columna));
            cell.setCellValue(req.getDesarrolloDeMercado());
        }
        
        if (req.getDistribucion() != null) {
            Cell cell = row.createCell(this.getPosition("Distribución", columna));
            cell.setCellValue(req.getDistribucion());
        }
        
        if (req.getEnergia() != null) {
            Cell cell = row.createCell(this.getPosition("Energía", columna));
            cell.setCellValue(req.getEnergia());
        }
        
        if (req.getFinanzasyTesoreria() != null) {
            Cell cell = row.createCell(this.getPosition("Finanzas y Tesorería", columna));
            cell.setCellValue(req.getFinanzasyTesoreria());
        }
        if (req.getIngenieria() != null) {
            Cell cell = row.createCell(this.getPosition("Ingeniería", columna));
            cell.setCellValue(req.getIngenieria());
        }
        
        if (req.getInteligenciaMercado() != null) {
            Cell cell = row.createCell(this.getPosition("Inteligencia de Mercado", columna));
            cell.setCellValue(req.getInteligenciaMercado());
        }
        
        if (req.getLogistica() != null) {
            Cell cell = row.createCell(this.getPosition("Logística", columna));
            cell.setCellValue(req.getLogistica());
        }
        
        if (req.getMarketing() != null) {
            Cell cellLogis = row.createCell(this.getPosition("Marketing", columna));
            cellLogis.setCellValue(req.getMarketing());
        }
        
        if (req.getMedioAmbiSeguridadIndu() != null) {
            Cell cell = row.createCell(this.getPosition("Medio Ambiente y Seguridad Industrial", columna));
            cell.setCellValue(req.getMedioAmbiSeguridadIndu());
        }
        
        if (req.getMetalicos() != null) {
            Cell cell = row.createCell(this.getPosition("Metálicos", columna));
            cell.setCellValue(req.getMetalicos());
        }
        
        if (req.getPersonas() != null) {
            Cell cell = row.createCell(this.getPosition("Personas", columna));
            cell.setCellValue(req.getPersonas());
        }
        
        if (req.getSeguridadPatrinomial() != null) {
            Cell cell = row.createCell(this.getPosition("Seguridad Patrimonial", columna));
            cell.setCellValue(req.getSeguridadPatrinomial());
        }
        
        if (req.getServiciosGenerales() != null) {
            Cell cell = row.createCell(this.getPosition("Servicios Generales", columna));
            cell.setCellValue(req.getServiciosGenerales());
        }
        
        if (req.getSuministros() != null) {
            Cell cell = row.createCell(this.getPosition("Suministros", columna));
            cell.setCellValue(req.getSuministros());
        }
        
        if (req.getTi() != null) {
            Cell cellIndustri = row.createCell(this.getPosition("TI", columna));
            cellIndustri.setCellValue(req.getTi());
        } 
        
        if (req.getJuridico() != null) {
            Cell cellJurid = row.createCell(this.getPosition("Jurídico", columna));
            cellJurid.setCellValue(req.getJuridico());
        }
        
        if (req.getDireccionGeneral() != null) {
            Cell cellIndustri = row.createCell(this.getPosition("Dirección General", columna));
            cellIndustri.setCellValue(req.getDireccionGeneral());
        } 
        
        if (req.getOtro() != null) {
            Cell cell = row.createCell(this.getPosition("Otro", columna));
            cell.setCellValue(req.getOtro());
        }
        
        if (req.getJuridicoCorparativo() != null) {
            Cell cell = row.createCell(this.getPosition("JURIDICO CORPORATIVO", columna));
            cell.setCellValue(req.getJuridicoCorparativo());
        }
        
        if (req.getComunicacionExterna() != null) {
            Cell cell = row.createCell(this.getPosition("Comunicación Externa", columna));
            cell.setCellValue(req.getComunicacionExterna());
        }
       
        if (req.getIndustrial() != null) {
            Cell cellIndustri = row.createCell(this.getPosition("Industrial", columna));
            cellIndustri.setCellValue(req.getIndustrial());
        } 
       
        if (req.getSeguridadIndustrial() != null) {
            Cell cellSeguridad = row.createCell(this.getPosition("Seguridad Industrial", columna));
            cellSeguridad.setCellValue(req.getSeguridadIndustrial());
        }
        
        if (req.getSuministrosLogística() != null) {
            Cell cellSuminLogi = row.createCell(this.getPosition("Suministros Logística", columna));
            cellSuminLogi.setCellValue(req.getSuministrosLogística());
        }
        
        if (req.getMedioAmbiente() != null) {
            Cell cellAudInte = row.createCell(this.getPosition("Medio Ambiente", columna));
            cellAudInte.setCellValue(req.getMedioAmbiente());
        }
//        if (req.getPorcentaje() != null) {
//            Cell cellTotal = row.createCell(this.getPosition("Porcentaje", columna));
//            cellTotal.setCellValue(req.getPorcentaje());
//        }
      
        if (req.getTotal() != null) {
            Cell cellTotal = row.createCell(this.getPosition("Total", columna));
            cellTotal.setCellValue(req.getTotal());
        }
        if (req.getAreaName() != null) {
            Cell cellTotal = row.createCell(this.getPosition("Nombre Área", columna));
            cellTotal.setCellValue(req.getAreaName());
        }
        if (req.getAreaTotal() != null) {
            Cell cellTotal = row.createCell(this.getPosition("Total/Área", columna));
            cellTotal.setCellValue(req.getAreaTotal());
        }
    }
    
    private int getPosition(String area, String[] columna) {
        int index = 0;
        for (int i = 0; i < columna.length; i++) {
          if (columna[i].trim().equals(area)) {
              return i;
          }
        }
      return index;
    }

	@Override
	public String[][] getCatalogAsMatrix() throws BusinessException {
		return null;
	}

	public String getReportPath() throws BusinessException{
		try {
			return this.configurable.findByName("ROOT_PATH");
		} catch (Exception e) {
			throw new BusinessException("No existe la ruta configurada", e);
		}
	}

}
