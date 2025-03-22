package mx.pagos.admc.contracts.interfaces;

import java.util.List;
import java.util.Map;

import mx.pagos.admc.contracts.structures.ClaseGafica;
import mx.pagos.admc.contracts.structures.FiltrosGrafica;
import mx.pagos.admc.contracts.structures.GraficaStatusDTO;
import mx.pagos.admc.contracts.structures.ReporteExcel;
import mx.pagos.admc.contracts.structures.Requisition;
import mx.pagos.admc.contracts.structures.RequisitionStatusTurn;
import mx.pagos.general.exceptions.DatabaseException;

public interface Reportes {

	List<Requisition> obrtenerDatosGrafica(int idRequisition) throws DatabaseException;

	int graficaPastelStatus(String status, GraficaStatusDTO tipo) throws DatabaseException;
	
	int graficaPastelArea(String status, GraficaStatusDTO tipo) throws DatabaseException;

	int getDefaulTimeByStep(String step) throws DatabaseException;
	
	int getDateTotalStep(String step,GraficaStatusDTO params) throws DatabaseException;
	
	int getDateRangoFechas(String step,GraficaStatusDTO params) throws DatabaseException;

	RequisitionStatusTurn getStartStep(int idRequisition, String status, int tipo) throws DatabaseException;
	
	RequisitionStatusTurn getStartStepTime(int idRequisition,String status, int tipo) throws DatabaseException;
	
	RequisitionStatusTurn getTotalSolicitudes(int idRequisition, String status) throws DatabaseException;

	int contratosPorMes(int year, int month) throws DatabaseException;

	<T> List<T> getListaFiltros(String table, Class<T> type) throws DatabaseException;

	int graficaFiltros(String status, final FiltrosGrafica params) throws DatabaseException;

	List<ReporteExcel> reporteExcelSolicitudes(FiltrosGrafica params) throws DatabaseException; 
	
	List<ReporteExcel> reporteExcelTiempoPoliticaFechas(GraficaStatusDTO params) throws DatabaseException; 
	
}
