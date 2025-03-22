package mx.pagos.admc.contracts.daos;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import mx.engineer.utils.date.DateUtils;
import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.DatabaseUtils;
import mx.pagos.admc.contracts.interfaces.Reportes;
import mx.pagos.admc.contracts.structures.FiltrosGrafica;
import mx.pagos.admc.contracts.structures.GraficaStatusDTO;
import mx.pagos.admc.contracts.structures.ReporteExcel;
import mx.pagos.admc.contracts.structures.Requisition;
import mx.pagos.admc.contracts.structures.RequisitionStatusTurn;
import mx.pagos.general.exceptions.DatabaseException;

import java.text.SimpleDateFormat;
import java.util.Date;

@Repository("ReportesDTO")
public class ReportesDAO implements Reportes {

	private static final Logger LOG = Logger.getLogger(ReportesDAO.class);
	@Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;
	private static final String NULL = "Null";
	private static final String LIKE = "%";
	private static final String DATABASE_DATE_FORMAT = "yyyy-MM-dd";
	private static final String SINGLE_QUOTE = "'";
	private static final String WHEN = "WHEN ";
	private static final String TURN_DATE_PLUS_TURN_ATTENTION_DAYS = "(Turn.TurnDate + TURN.AttentionDays)";
	@Autowired
	private DatabaseUtils databaseUtils;
	
	@Override
	public List<Requisition> obrtenerDatosGrafica(int idRequisition) throws DatabaseException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("idRequisition", idRequisition);
			List<Requisition> lista = this.namedJdbcTemplate
					.query("SELECT * FROM REQUISITIONSTATUSTURN where IdRequisition =:idRequisition", 
							params,  
							new BeanPropertyRowMapper<Requisition>(Requisition.class));


			return lista;

		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al recuperar áreas", dataAccessException);
		}catch (Exception e) {
			throw new DatabaseException("Error al recuperar áreas", e);
		}
	}


	@Override
	public  int graficaPastelStatus(String status, GraficaStatusDTO params) throws DatabaseException{
		try {
			
			//Log.info(this.getClass().getSimpleName()+ " parametros :: "+params.toString());
			MapSqlParameterSource param = generaParametro(params);
			param.addValue("status", status);
			return this.namedJdbcTemplate.queryForObject(createQuery(params.getTipo()), 
					param, Integer.class);

		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al recuperar la cantidad de solicitudes por estatus", dataAccessException);
		}
	}
			
	@Override
	public  int graficaPastelArea(String status, GraficaStatusDTO params) throws DatabaseException{
		try {
			
			//Log.info(this.getClass().getSimpleName()+ " parametros :: "+params.toString());
			final MapSqlParameterSource param = this.ParameterSourceAreaFechas(status,params);
			final Integer i;
			if(params.getFechaInicio()==null && params.getFechaFin()==null) {
				i=1;
			}else {
				i=2;
			}
//			param.addValue("status", status);
			return this.namedJdbcTemplate.queryForObject(createQueryArea(i), 
					param, Integer.class);

		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al recuperar la cantidad de solicitudes por estatus", dataAccessException);
		}
	}

	private String createQuery(int tipo) {

		StringBuilder query = new StringBuilder();
		query.append("select count(*) from REQUISITION where status =:status ");

		switch (tipo) {
		case 1:
			query.append("and month(StageStartDate) =:mes and year(StageStartDate) =:anio");
			break;
		case 2:
			query.append("and year(StageStartDate) =:anio");
			break;
		case 3:
			query.append("and StageStartDate BETWEEN :fechaInicio and :fechaFin");
			break;
		}

		//Log.info(this.getClass().getSimpleName()+" - QUERY :: \n"+query.toString());
		return query.toString();
	}
	private String createQueryArea(int tipo) {

		StringBuilder sql = new StringBuilder();
//		query.append("select count(*) from REQUISITION where status =:status ");
		switch (tipo) {
		case 1:
			
			sql.append("SELECT COUNT(*)  ");
			sql.append("FROM ( ");
			sql.append("select DISTINCT r.IdRequisition as idRequisition, CONCAT(u.Name,' ', u.FirstLastName,' ',  u.SecondLastName) as nombreSolicitante, ");
			sql.append("c.Name as tipoDocumento,p.Name as nombreSolicitud,  "); 
//			sql.append("TURN.Turn AS Turn, TURN.AttentionDays AS AttentionDays, ");
			sql.append("CASE ");
			sql.append("WHEN s.IdPersonality =1 THEN s.CompanyName " );
			sql.append("WHEN s.IdPersonality =2 THEN s.CommercialName " );
			sql.append("WHEN s.IdPersonality =3 THEN s.CompanyName " );
			sql.append("WHEN s.IdPersonality =4 THEN s.CommercialName " );
			sql.append("ELSE s.CompanyName ");
			sql.append("END as nombreProveedor, " ); 
			sql.append("s.Rfc as rfc,a.Name as area, st.Name as status, r.ApplicationDate as fechaCreacion,  ");
			sql.append("(SELECT DATEDIFF(DAY,r.ApplicationDate,(SELECT DISTINCT TOP 1 TurnDate from REQUISITIONSTATUSTURN where IdRequisition  = r.IdRequisition order by TurnDate DESC)))as totaldias, ");
			sql.append("(SELECT DISTINCT TOP 1 TurnDate from REQUISITIONSTATUSTURN where IdRequisition  = r.IdRequisition order by TurnDate DESC) as fechaUltimaModificacion ");
//			sql.append("(SELECT TOP 1 TurnDate from REQUISITIONSTATUSTURN where IdRequisition  = r.IdRequisition order by TurnDate DESC) as fechaUltimaModificacion ");
			sql.append("from REQUISITION r ");
			sql.append("inner join USERS u on r.IdApplicant = u.IdUser "); 
			sql.append("inner join CATALOGDOCTYPE c on r.IdDocument = c.IdDocument " ); 
			sql.append("inner join SUPPLIER s on r.IdSupplier = s.IdSupplier ");
			sql.append("inner join SCREEN st on r.Status = st.FlowStatus ");
			sql.append("inner join AREA a on a.IdArea = r.IdAreaTender ");
			sql.append("inner join PROFILEUSER pr on pr.IdUser = u.IdUser ");
			sql.append("inner join PROFILE p on p.IdProfile = pr.IdProfile ");
//			sql.append("inner join REQUISITIONSTATUSTURN TURN ON r.IdRequisition = TURN.IdRequisition AND r.Status = TURN.Status ");
			sql.append(" where a.IdArea =:area and r.Status=:status) as Subconsulta");	

		break;
		case 2:
//			sql.append("SELECT COUNT(*) ");
//			sql.append("FROM ( ");
//			sql.append("SELECT DISTINCT rs.Status,rs.IdRequisition,  a.IdArea, a.Name, rs.TurnDate ");
//			sql.append("FROM  REQUISITIONSTATUSTURN rs ");
//			sql.append("INNER JOIN REQUISITION r on r.IdRequisition = rs.IdRequisition ");
//			sql.append("INNER JOIN AREA a on a.IdArea = r.IdAreaTender WHERE r.Status=:status and rs.Status =:status ");
//			sql.append("and a.IdArea =:area and rs.TurnDate BETWEEN :fechaInicio and :fechaFin ");
//			sql.append(") AS Subconsulta ");
			/**/
			sql.append("SELECT COUNT(*)  ");
			sql.append("FROM ( ");
			sql.append("select DISTINCT r.IdRequisition as idRequisition, CONCAT(u.Name,' ', u.FirstLastName,' ',  u.SecondLastName) as nombreSolicitante, ");
			sql.append("c.Name as tipoDocumento,p.Name as nombreSolicitud,  "); 
			sql.append("TURN.Turn AS Turn, TURN.AttentionDays AS AttentionDays, ");
			sql.append("CASE ");
			sql.append("WHEN s.IdPersonality =1 THEN s.CompanyName " );
			sql.append("WHEN s.IdPersonality =2 THEN s.CommercialName " );
			sql.append("WHEN s.IdPersonality =3 THEN s.CompanyName " );
			sql.append("WHEN s.IdPersonality =4 THEN s.CommercialName " );
			sql.append("ELSE s.CompanyName ");
			sql.append("END as nombreProveedor, " ); 
			sql.append("s.Rfc as rfc,a.Name as area, st.Name as status, r.ApplicationDate as fechaCreacion,  ");
			sql.append("(SELECT DATEDIFF(DAY,r.ApplicationDate,(SELECT DISTINCT TOP 1 TurnDate from REQUISITIONSTATUSTURN where IdRequisition  = r.IdRequisition order by TurnDate DESC)))as totaldias, ");
			sql.append("(SELECT DISTINCT TOP 1 TurnDate from REQUISITIONSTATUSTURN where IdRequisition  = r.IdRequisition order by TurnDate DESC) as fechaUltimaModificacion ");
//			sql.append("(SELECT TOP 1 TurnDate from REQUISITIONSTATUSTURN where IdRequisition  = r.IdRequisition order by TurnDate DESC) as fechaUltimaModificacion ");
			sql.append("from REQUISITION r ");
			sql.append("inner join USERS u on r.IdApplicant = u.IdUser "); 
			sql.append("inner join CATALOGDOCTYPE c on r.IdDocument = c.IdDocument " ); 
			sql.append("inner join SUPPLIER s on r.IdSupplier = s.IdSupplier ");
			sql.append("inner join SCREEN st on r.Status = st.FlowStatus ");
			sql.append("inner join AREA a on a.IdArea = r.IdAreaTender ");
			sql.append("inner join PROFILEUSER pr on pr.IdUser = u.IdUser ");
			sql.append("inner join PROFILE p on p.IdProfile = pr.IdProfile ");
			sql.append("inner join REQUISITIONSTATUSTURN TURN ON r.IdRequisition = TURN.IdRequisition AND r.Status = TURN.Status ");
			sql.append(" WHERE r.Status=:status ");
			sql.append("and a.IdArea =:area and r.ApplicationDate BETWEEN :fechaInicio and :fechaFin ");
			sql.append(" )AS Subconsulta ");	
			break;
		}
		
/*
 		sql.append("select DISTINCT   r.IdRequisition, r.Status , ");
		sql.append("(SELECT DISTINCT TOP 1 TurnDate from REQUISITIONSTATUSTURN where IdRequisition  = r.IdRequisition order by TurnDate DESC ) as fechaUltimaModificacion  ");
		sql.append(" from REQUISITION r ");
		sql.append("inner join AREA a on a.IdArea = r.IdAreaTender where a.IdArea =:status ");
		sql.append(" and r.Status=:tipo or StageStartDate BETWEEN :fechaInicio and :fechaFin");
 */
		return sql.toString();
	}
	private MapSqlParameterSource ParameterSourceAreaFechas(String status, GraficaStatusDTO params) {
        final MapSqlParameterSource param = new MapSqlParameterSource();
//        String[] bandeja = {
//	            "DRAFT_GENERATION",
//	            "NEGOTIATOR_CONTRACT_REVIEW",
//	            "LOAD_SUPPLIER_AREAS_APPROVAL",
//	            "APROVED_BY_JURISTIC",
//	            "PRINT_CONTRACT",
//	            "SACC_SIGN_CONTRACT",
//	            "SACC_SCAN_CONTRACT",
//	            "CANCELLED",
//	            "REQUISITION_CLOSE",
//	            "EN_REVISION_JURIDICO"
//	            };
        String[] bandeja = {
        		"CANCELLED_CONTRACT",
        		"REQUISITION_CLOSE",
        		"NEGOTIATOR_CONTRACT_REVIEW",
        		"SACC_SIGN_CONTRACT",
	            "DRAFT_GENERATION",
	            "CANCELLED",
	            "LOAD_SUPPLIER_AREAS_APPROVAL",
	            "PRINT_CONTRACT",
	            "EN_REVISION_JURIDICO",
	            "APROVED_BY_JURISTIC",
	            "SACC_SCAN_CONTRACT",
	            };
        param.addValue("status", status);
        param.addValue("mes", params.getMes());
		param.addValue("anio", params.getAnio());
        param.addValue("anio", params.getAnio());
        LOG.info("----------------------bandeja seleccionada---------------------------------------------------------");
//        LOG.info("bandeja seleccionada ::: "+bandeja[params.getTipo()]);
        param.addValue("area", params.getTipo());
        LOG.info("-------------------------------------------------------------------------------");
        LOG.info("fecha inicio "+params.getFechaInicio());
        LOG.info("fecha final "+params.getFechaFin());
        param.addValue("fechaInicio", params.getFechaInicio());
		param.addValue("fechaFin", params.getFechaFin());
        LOG.info("-------------------------------------------------------------------------------");
        return param;
    }
	private MapSqlParameterSource generaParametro(GraficaStatusDTO params) {
		MapSqlParameterSource param = new MapSqlParameterSource();
		switch (params.getTipo()) {
		case 1:
			param.addValue("mes", params.getMes());
			param.addValue("anio", params.getAnio());

			break;
		case 2:
			param.addValue("anio", params.getAnio());
			break;
		case 3:
			param.addValue("fechaInicio", params.getFechaInicio());
			param.addValue("fechaFin", params.getFechaFin());
			break;
		}
		return param;
	}

    @Override
    public int getDefaulTimeByStep(String step) throws DatabaseException {
        try {

            MapSqlParameterSource param = new MapSqlParameterSource();
            param.addValue("status", step);
			return this.namedJdbcTemplate.queryForObject("select time from ALERTCONFIG where Status =:status", 
			param, Integer.class);

        } catch (EmptyResultDataAccessException emptyDataAccessException) {
            return 0;
        } catch (DataAccessException dataAccessException) {

            throw new DatabaseException("Error al recuperar la cantidad de solicitudes por estatus",
                    dataAccessException);
        }
    }
    @Override
    public int getDateTotalStep(String step,GraficaStatusDTO params) throws DatabaseException {
        try {
        	final Integer i;
            MapSqlParameterSource param = new MapSqlParameterSource();
//        	final MapSqlParameterSource param = this.createFindRequisitions(step, params);
          param.addValue("status", step);
          param.addValue("fechaInicio", params.getFechaInicio());
			param.addValue("fechaFin", params.getFechaFin());
			if(params.getFechaInicio()==null && params.getFechaFin()==null) {
				i=1;
			}else {
				i=2;
			}
			return this.namedJdbcTemplate.queryForObject(QueryTotalSolicitudes(i),
			param, Integer.class);

        } catch (EmptyResultDataAccessException emptyDataAccessException) {
            return 0;
        } catch (DataAccessException dataAccessException) {

            throw new DatabaseException("Error al recuperar la cantidad de solicitudes por estatus",
                    dataAccessException);
        }
    }
    @Override
    public int getDateRangoFechas(String step,GraficaStatusDTO params) throws DatabaseException {
        try {
        	final Integer i;
          MapSqlParameterSource param = new MapSqlParameterSource();
          param.addValue("Status", step);
          param.addValue("fechaInicio", params.getFechaInicio());
			param.addValue("fechaFin", params.getFechaFin());
			LOG.info("El estatus es : "+step);
			LOG.info("La Fecha Inicio es : "+params.getFechaInicio());
			LOG.info("La Fecha Final  es : "+params.getFechaFin());

			return this.namedJdbcTemplate.queryForObject(QueryFechasPolitica(),
			param, Integer.class);

        } catch (EmptyResultDataAccessException emptyDataAccessException) {
            return 0;
        } catch (DataAccessException dataAccessException) {

            throw new DatabaseException("Error al recuperar la cantidad de solicitudes por estatus",
                    dataAccessException);
        }
    }
	private String QueryTotalSolicitudes(int tipo) {

		StringBuilder query = new StringBuilder();
		switch (tipo) {
		case 1:

		this.buildSelectAllDistinctForTrayQuery(query);
		this.buildFindRequisitionsForTrayJoinsQuery(query);
		this.buildFindRequisitionsForTrayFiltersQuery(query);
		
		break;
		case 2:
//			query.append("SELECT COUNT(*) ");
//			query.append("from  REQUISITIONSTATUSTURN rs ");
//			query.append("where rs.Status =:status  ");
//			query.append("and rs.TurnDate BETWEEN :fechaInicio and :fechaFin ");
			this.buildSelectAllDistinctForTrayQuery(query);
			this.buildFindRequisitionsForTrayJoinsQuery(query);
			this.buildFindRequisitionsForTrayFiltersFechasQuery(query);
			break;
		}
		return query.toString();
	}
	private String QueryFechasPolitica() {

		StringBuilder query = new StringBuilder();

		this.buildSelectAllDistinctForTrayQuery2(query);
		this.buildFindRequisitionsForTrayJoinsQuery2(query);
		this.buildFindRequisitionsForTrayFiltersQuery2(query);

		return query.toString();
	}
	
	private void buildSelectAllDistinctForTrayQuery(final StringBuilder query) {
		query.append("SELECT COUNT(*)  ");
		query.append("FROM ( ");
		query.append("SELECT DISTINCT SUPPLIER.CompanyName AS SupplierName, DOCUMENTTYPE.Name AS DocumentTypeName,");
		query.append("REQUISITION.IdRequisition, REQUISITION.ApplicationDate, REQUISITION.Status, ");
		query.append(
				"CONCAT (USERLAWYER.Name, ' ' , USERLAWYER.FirstLastName, ' ', USERLAWYER.SecondLastName) As LawyerName, ");
		query.append("TURN.Turn AS Retry, COMMENTS.CommentText AS Comment, TURN.TurnDate, ");
		query.append("REQUISITION.ContractType as contractType, ");
		query.append("AREA.Name as area, ");
		query.append("unit.name as unidad, "); // FIXME ENTITY

		this.buildSemaphoreFields(query);
		this.buildRequisitionJoins(query);
	}
	private void buildSemaphoreFields(final StringBuilder query) {
		final SimpleDateFormat toDateTimeFormat = new SimpleDateFormat(DATABASE_DATE_FORMAT);
		final String formatedTodayDate = SINGLE_QUOTE + toDateTimeFormat.format(new Date()) + SINGLE_QUOTE;
		query.append("CASE ");
		query.append(WHEN).append(
				this.databaseUtils.buildDateDiffSSqlFunction(formatedTodayDate, TURN_DATE_PLUS_TURN_ATTENTION_DAYS));
		query.append(" < REDSEMAPHORE.RedSemaphore");
		query.append(" * TURN.AttentionDays THEN 'RED' ");
		query.append(WHEN).append(
				this.databaseUtils.buildDateDiffSSqlFunction(formatedTodayDate, TURN_DATE_PLUS_TURN_ATTENTION_DAYS));
		query.append(" < YELLOWSEMAPHORE.YellowSemaphore");
		query.append(" * TURN.AttentionDays THEN 'YELLOW' ");
		query.append("ELSE 'GREEN' END AS Semaphore, FINANCIALENTITY.Name AS Empresa, ");
		query.append("TURN.Turn AS Turn, TURN.AttentionDays AS AttentionDays ");
	}
	private void buildRequisitionJoins(final StringBuilder query) {
		query.append("FROM REQUISITION ");
		query.append(
				" LEFT JOIN REQUISITIONFINANCIALENTITY ON REQUISITIONFINANCIALENTITY.IdRequisition =  REQUISITION.IdRequisition ");
		query.append(
				" LEFT JOIN FINANCIALENTITY ON FINANCIALENTITY.IdFinancialEntity = REQUISITIONFINANCIALENTITY.IdFinancialEntity ");
		query.append(" LEFT JOIN SUPPLIER ON REQUISITION.IdSupplier = SUPPLIER.IdSupplier ");
		query.append("LEFT JOIN DOCUMENTTYPE ON REQUISITION.IdDocumentType = DOCUMENTTYPE.IdDocumentType ");
		query.append("LEFT JOIN SCREEN ON REQUISITION.Status = SCREEN.FlowStatus ");
		query.append("LEFT JOIN AREA ON REQUISITION.IdAreaTender = AREA.IdArea ");
		query.append("LEFT JOIN unit ON REQUISITION.IdUnit = unit.idUnit ");

	}
	private void buildFindRequisitionsForTrayJoinsQuery(final StringBuilder query) {
		query.append(
				"INNER JOIN REQUISITIONSTATUSTURN TURN ON REQUISITION.IdRequisition = TURN.IdRequisition AND REQUISITION.Status = TURN.Status ");
		query.append("LEFT JOIN REQUISITION LAWYERSUBDIRECTOR ");
		query.append("ON LAWYERSUBDIRECTOR.IdRequisition = REQUISITION.IdRequisition ");
		query.append("LEFT JOIN USERS ON USERS.IdUser = REQUISITION.IdApplicant ");
		query.append("LEFT JOIN USERS USERLAWYER ON USERLAWYER.IdUser = REQUISITION.IdLawyer ");
		query.append("LEFT JOIN REQUISITIONUSERSVOBO USERSVOBO ");
		query.append("ON USERSVOBO.IdRequisition = REQUISITION.IdRequisition ");
		query.append("LEFT JOIN REQUISITION USERSVOBOREQUISITION ");
		query.append("ON USERSVOBOREQUISITION.IdRequisition = USERSVOBO.IdRequisition ");
		query.append("LEFT JOIN COMMENTS ON COMMENTS.IdRequisition = REQUISITION.IdRequisition ");
		query.append("AND COMMENTS.FlowStatus = :status ");
		query.append("LEFT JOIN (SELECT IdRequisition, MAX(CreationDate) AS CreationDate FROM COMMENTS WHERE ");
		query.append("FlowStatus = :status GROUP BY ");
		query.append("IdRequisition) RECENT_COMMENT ON COMMENTS.IdRequisition = RECENT_COMMENT.IdRequisition,");
		query.append(
				"(SELECT CAST(Value AS DECIMAL(3,2)) AS RedSemaphore FROM CONFIGURATION WHERE Name = 'RED_SEMAPHORE_PERCENTAGE') REDSEMAPHORE,");
		query.append(
				"(SELECT CAST(Value AS DECIMAL(3,2)) AS YellowSemaphore FROM CONFIGURATION WHERE Name = 'YELLOW_SEMAPHORE_PERCENTAGE') YELLOWSEMAPHORE ");
	}
	private void buildFindRequisitionsForTrayFiltersQuery(final StringBuilder query) {
		query.append("WHERE  REQUISITION.Status = :status ");
		query.append("AND (COMMENTS.CreationDate = RECENT_COMMENT.CreationDate OR COMMENTS.CreationDate IS NULL) AND ");
//		this.buildInboxFilter2(query);
		query.append(
				"TURN.TurnDate = (SELECT MAX(TurnDate) FROM REQUISITIONSTATUSTURN TURNDATE WHERE TURNDATE.IdRequisition = REQUISITION.IdRequisition) AND (");
		query.append(
				"( :status = REQUISITION.Status AND REQUISITION.Status IN (");
		query.append(this.databaseUtils.arrayToTableFunc(
				"SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'APPLICANT_BUSY_STATUS'"));
		query.append(
				")) OR (:status = REQUISITION.Status AND REQUISITION.Status IN (");
		query.append(this.databaseUtils.arrayToTableFunc(
				"SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'APPLICANTSUBDIRECTOR_BUSY_STATUS'"));
		query.append(
				")) OR (:status = LAWYERSUBDIRECTOR.Status AND LAWYERSUBDIRECTOR.Status IN (");
		query.append(this.databaseUtils.arrayToTableFunc(
				"SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'LAWYERSUBDIRECTOR_BUSY_STATUS'"));
		query.append(
				")) OR ( :status = REQUISITION.Status AND REQUISITION.Status IN (");
		query.append(this.databaseUtils
				.arrayToTableFunc("SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'LAWYER_BUSY_STATUS'"));
		query.append(
				")) OR (USERSVOBO.IsVoBoGiven = 0 AND :status = USERSVOBOREQUISITION.Status AND USERSVOBOREQUISITION.Status IN (");
		query.append(this.databaseUtils.arrayToTableFunc(
				"SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'USERSVOBOREQUISITION_BUSY_STATUS'"));
		query.append(
				")) OR ( :status = REQUISITION.Status AND REQUISITION.Status IN (");
		query.append(this.databaseUtils.arrayToTableFunc(
				"SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'EVALUATOR_BUSY_STATUS'"));
		query.append(")))) as Subconsulta ");
	}
	private void buildFindRequisitionsForTrayFiltersFechasQuery(final StringBuilder query) {
		query.append("WHERE  REQUISITION.Status = :status ");
		query.append("AND (COMMENTS.CreationDate = RECENT_COMMENT.CreationDate OR COMMENTS.CreationDate IS NULL) AND ");
//		this.buildInboxFilter2(query);
		query.append(
				"TURN.TurnDate = (SELECT MAX(TurnDate) FROM REQUISITIONSTATUSTURN TURNDATE WHERE TURNDATE.IdRequisition = REQUISITION.IdRequisition) AND (");
		query.append(
				"( :status = REQUISITION.Status AND REQUISITION.Status IN (");
		query.append(this.databaseUtils.arrayToTableFunc(
				"SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'APPLICANT_BUSY_STATUS'"));
		query.append(
				")) OR (:status = REQUISITION.Status AND REQUISITION.Status IN (");
		query.append(this.databaseUtils.arrayToTableFunc(
				"SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'APPLICANTSUBDIRECTOR_BUSY_STATUS'"));
		query.append(
				")) OR (:status = LAWYERSUBDIRECTOR.Status AND LAWYERSUBDIRECTOR.Status IN (");
		query.append(this.databaseUtils.arrayToTableFunc(
				"SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'LAWYERSUBDIRECTOR_BUSY_STATUS'"));
		query.append(
				")) OR ( :status = REQUISITION.Status AND REQUISITION.Status IN (");
		query.append(this.databaseUtils
				.arrayToTableFunc("SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'LAWYER_BUSY_STATUS'"));
		query.append(
				")) OR (USERSVOBO.IsVoBoGiven = 0 AND :status = USERSVOBOREQUISITION.Status AND USERSVOBOREQUISITION.Status IN (");
		query.append(this.databaseUtils.arrayToTableFunc(
				"SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'USERSVOBOREQUISITION_BUSY_STATUS'"));
		query.append(
				")) OR ( :status = REQUISITION.Status AND REQUISITION.Status IN (");
		query.append(this.databaseUtils.arrayToTableFunc(
				"SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'EVALUATOR_BUSY_STATUS'"));
		query.append("))) AND TURN.TurnDate BETWEEN :fechaInicio and :fechaFin ) as Subconsulta ");
	}

    private MapSqlParameterSource createFindRequisitions(
            final String step, final GraficaStatusDTO params) {
        LOG.info("QUERY -> createFindRequisitionsToCreateOneFromAnotherParameters -PARAMETROS LIKE ");
        final MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("status", step);
//        source.addValue("fechaInicio", params.getFechaInicio());
//        source.addValue("fechaFin", params.getFechaFin());
        source.addValue(TableConstants.Fecha_Inicio, LIKE + params.getFechaInicio() + LIKE);
        source.addValue(TableConstants.Fecha_Inicio + NULL, params.getFechaInicio());
        source.addValue(TableConstants.Fecha_Final, LIKE + params.getFechaFin() + LIKE);
        source.addValue(TableConstants.Fecha_Final + NULL, params.getFechaFin());
//        source.addValue(TableConstants.SUPPLIER_NAME, LIKE + supplierParameter + LIKE);
//        source.addValue(TableConstants.SUPPLIER_NAME + NULL, supplierParameter);
        return source;
    }

    @Override
	public RequisitionStatusTurn getStartStep(int idRequisition, String status, int tipo) throws DatabaseException {
		try {
			LOG.info("idRequisition ::: "+idRequisition+"\n"+"Status :::: "+status+"\n"+"Tipo ::::: "+tipo);
			StringBuilder sql =  new StringBuilder();
			sql.append("SELECT TOP 1 * FROM REQUISITIONSTATUSTURN where IdRequisition =:idRequisition and Status =:status ORDER BY TurnDate");
			sql.append(tipo ==1 ? "" :" DESC");
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("idRequisition", idRequisition);
			params.addValue("status", status);
			List<RequisitionStatusTurn> beans = this.namedJdbcTemplate
					.query(sql.toString(), params, new BeanPropertyRowMapper<RequisitionStatusTurn>(RequisitionStatusTurn.class));

			return beans.size()==1 ? beans.get(0) : null;

		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al recuperar los dias tardados en cada paso por peticion", dataAccessException);
		}
	}
    @Override
	public RequisitionStatusTurn getStartStepTime(int idRequisition, String status, int tipo) throws DatabaseException {
		try {
			StringBuilder sql =  new StringBuilder();
//			sql.append("SELECT TOP 1 * FROM REQUISITIONSTATUSTURN where IdRequisition =:idRequisition and Status =:status ORDER BY TurnDate");
			sql.append("SELECT TOP 1 * FROM REQUISITIONSTATUSTURN where IdRequisition =:idRequisition and Status =:status ORDER BY TurnDate");
			sql.append(tipo ==1 ? "" :" DESC");
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("idRequisition", idRequisition);
			params.addValue("status", status);
//			params.addValue("idRequisition", idRequisition);
			params.addValue("status", status);
			//implementacion de pruebas
//			sql.append("SELECT rs.IdRequisition, rs.Status, rs.Turn, rs.TurnDate, rs.AttentionDays, rs.Stage, rs.active   ");
//			sql.append("FROM REQUISITIONSTATUSTURN rs ");
//			sql.append("inner join REQUISITION r on r.IdRequisition = rs.IdRequisition ");
//			sql.append("where rs.IdRequisition  = r.IdRequisition and rs.Status =:status ORDER BY TurnDate DESC ");
//		//comentada	sql.append("where rs.Status =:status ORDER BY rs.TurnDate");

			List<RequisitionStatusTurn> beans = this.namedJdbcTemplate
					.query(sql.toString(), params, new BeanPropertyRowMapper<RequisitionStatusTurn>(RequisitionStatusTurn.class));

			return beans.size()==1 ? beans.get(0) : null;
//			return beans;


		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al recuperar los dias tardados en cada paso por peticion", dataAccessException);
		}
	}
	@Override
	public RequisitionStatusTurn getTotalSolicitudes(int idRequisition, String status) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters =this.createFindTotalParameters(idRequisition, status);
			StringBuilder sql =  new StringBuilder();
			sql.append("SELECT * FROM REQUISITION where ((:IdRequisitionNull IS NULL)  OR (IdRequisition LIKE :IdRequisition)) and Status =:Status ");
//			sql.append(tipo ==1 ? "" :" DESC");
//			MapSqlParameterSource params = new MapSqlParameterSource();
//			params.addValue("idRequisition", idRequisition);
//			params.addValue("status", status);
			List<RequisitionStatusTurn> beans = this.namedJdbcTemplate
					.query(sql.toString(), namedParameters, new BeanPropertyRowMapper<RequisitionStatusTurn>(RequisitionStatusTurn.class));

			return beans.size()==1 ? beans.get(0) : null;

		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al recuperar los dias tardados en cada paso por peticion", dataAccessException);
		}
	}
	  private MapSqlParameterSource createFindTotalParameters(
	            final int idUser, final String status) {
	        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	        namedParameters.addValue(TableConstants.ID_REQUISITION, LIKE + idUser + LIKE);
	        namedParameters.addValue(TableConstants.ID_REQUISITION + NULL, idUser);
	        namedParameters.addValue(TableConstants.STATUS, status.toString());
	        return namedParameters;
	    }
	@Override
	public int contratosPorMes(int year, int month) throws DatabaseException{
		try {

			String sql ="select count(*) from REQUISITION where " + 
					"YEAR(StageStartDate) = :year and " + 
					"MONTH(StageStartDate) =:month and " + 
					"Status = 'REQUISITION_CLOSE' ";
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("year",year );
			param.addValue("month",month );
			return this.namedJdbcTemplate.queryForObject(sql, param, Integer.class);

		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al recuperar la cantidad de solicitudes por estatus", dataAccessException);
		}

	}


	@Override
	public <T> List<T> getListaFiltros(String table ,Class<T> type) throws DatabaseException{
		try {
			String sql = "select * from "+table;
			return this.namedJdbcTemplate.query(sql, new MapSqlParameterSource(), new BeanPropertyRowMapper(type));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al recuperar los dias tardados en cada paso por peticion", dataAccessException);
		}
	}


	@Override
	public  int graficaFiltros(String status, final FiltrosGrafica params) throws DatabaseException{
		try {
			MapSqlParameterSource param = creaParametrosFiltros(params);
			param.addValue("status", status);
			return this.namedJdbcTemplate.queryForObject(crearSqlPorFiltros(params), 
					param, Integer.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al recuperar la cantidad de solicitudes por estatus", dataAccessException);
		}
	}

	private String crearSqlPorFiltros(FiltrosGrafica params) {
		StringBuilder sql = new StringBuilder();
		sql.append("Select count(*) ");
		sql.append("from requisition r ");
		if(params.getIdCompany() >0) {
			sql.append("INNER JOIN REQUISITIONFINANCIALENTITY f ON f.IdRequisition = r.IdRequisition");
		}

		sql.append(" where r.status =:status ");

		if(params.getIdUser()>0) {
			sql.append("and r.IdApplicant =:idApplicant ");
		}

		if(params.getIdArea()>0) {
			//sql.append(params.getIdUser() >0 ? "and ": "");
			sql.append("and r.IdAreaTender =:idArea ");
		}

		if(params.getIdSupplier() >0) {
			//sql.append(params.getIdArea() >0 ? "and ": "");
			sql.append("and r.IdSupplier =:idSupplier ");
		}

		if(params.getIdCompany() >0) {
			//sql.append(params.getIdSupplier() >0 ? "and ": "");
			sql.append("and f.IdFinancialEntity =:idCompany ");
		}
//		Log.info(this.getClass().getSimpleName() +" PARAMS :: "+ sql.toString());
//		Log.info(this.getClass().getSimpleName() +" QUERY :: "+ sql.toString());
		return sql.toString();
	}

	private MapSqlParameterSource creaParametrosFiltros(FiltrosGrafica params) {
		MapSqlParameterSource param = new MapSqlParameterSource();
		if(params.getIdUser()>0) 
			param.addValue("idApplicant", params.getIdUser());
		
		if(params.getIdArea()>0) 
			param.addValue("idArea", params.getIdArea());
		
		
		if(params.getIdSupplier() >0) 
			param.addValue("idSupplier", params.getIdSupplier());

		if(params.getIdCompany() >0)
			param.addValue("idCompany", params.getIdCompany());

		if(params.isOnlyFinished()) {
			param.addValue("startDate", DateUtils.dateToString("yyyy-MM-dd", params.getStartDate()));
			param.addValue("endDate", DateUtils.dateToString("yyyy-MM-dd", params.getEndDate()));

		}

		return param;
	}

	@Override
	public  List<ReporteExcel> reporteExcelSolicitudes(final FiltrosGrafica params) throws DatabaseException{
		try {
			MapSqlParameterSource param = creaParametrosFiltros(params);
			return this.namedJdbcTemplate.query(sqlReporteEstatusSolicitud(params), 
					param, new BeanPropertyRowMapper<ReporteExcel>(ReporteExcel.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al recuperar la cantidad de solicitudes por estatus", dataAccessException);
		}
	}
	@Override
	public  List<ReporteExcel> reporteExcelTiempoPoliticaFechas(final GraficaStatusDTO datos) throws DatabaseException{
		try {
			StringBuilder sql =  new StringBuilder();
			 MapSqlParameterSource param = new MapSqlParameterSource();
	          param.addValue("Status", datos.getIdArea());
	          param.addValue("fechaInicio", datos.getFechaInicio());
				param.addValue("fechaFin", datos.getFechaFin());
				LOG.info("El estatus es : "+datos.getIdArea());
				LOG.info("La Fecha Inicio es : "+datos.getFechaInicio());
				LOG.info("La Fecha Final  es : "+datos.getFechaFin());

			
				return this.namedJdbcTemplate.query(this.sqlReporteEstatusTiempoPoliticaFechas(datos), param,
						new BeanPropertyRowMapper<>(ReporteExcel.class));

	} catch (DataAccessException dataAccessException) {
		throw new DatabaseException(dataAccessException+" Error al recuperar la cantidad de solicitudes por estatus");
	}
	}

	private String sqlReporteEstatusSolicitud(FiltrosGrafica params) {
		StringBuilder sql = new StringBuilder();
		sql.append("select r.IdRequisition as idRequisition, u.IdUser,  CONCAT(u.Name,' ', u.FirstLastName,' ',  u.SecondLastName) as nombreSolicitante, ");
		sql.append("c.Name as tipoDocumento, MAX(p.Name) as nombreSolicitud,  "); 
//		sql.append("TURN.Turn AS Turn, TURN.AttentionDays AS AttentionDays, ");
		sql.append("CASE ");
		sql.append("WHEN s.IdPersonality =1 THEN s.CompanyName " );
		sql.append("WHEN s.IdPersonality =2 THEN s.CommercialName " );
		sql.append("WHEN s.IdPersonality =3 THEN s.CompanyName " );
		sql.append("WHEN s.IdPersonality =4 THEN s.CommercialName " );
		sql.append("ELSE s.CompanyName ");
		sql.append("END as nombreProveedor, " ); 
		sql.append("s.Rfc as rfc,a.Name as area, st.Name as status, r.ApplicationDate as fechaCreacion,  ");
		sql.append("(SELECT DATEDIFF(DAY,r.ApplicationDate,(SELECT TOP 1 TurnDate from REQUISITIONSTATUSTURN where IdRequisition  = r.IdRequisition order by TurnDate DESC)))as totaldias, ");
		sql.append("(SELECT TOP 1 TurnDate from REQUISITIONSTATUSTURN where IdRequisition  = r.IdRequisition order by TurnDate DESC) as fechaUltimaModificacion ");
		sql.append("from REQUISITION r ");
		sql.append("INNER JOIN USERS u on r.IdApplicant = u.IdUser "); 
		sql.append("INNER JOIN CATALOGDOCTYPE c on r.IdDocument = c.IdDocument " ); 
		sql.append("INNER JOIN SUPPLIER s on r.IdSupplier = s.IdSupplier ");
		sql.append("INNER JOIN SCREEN st on r.Status = st.FlowStatus ");
		sql.append("INNER JOIN AREA a on a.IdArea = r.IdAreaTender ");
		sql.append("INNER JOIN PROFILEUSER pr ON pr.IdUser = u.IdUser  ");
		sql.append("INNER JOIN PROFILE p ON pr.IdProfile = p.IdProfile ");
//		sql.append("INNER JOIN REQUISITIONSTATUSTURN TURN ON r.IdRequisition = TURN.IdRequisition AND r.Status = TURN.Status ");
		
		LOG.info("  FILTROS  \n IdCompany: "+params.getIdCompany()+"\n IdUser: "+params.getIdUser()+"\n IdsAreas: "+params.getIdsAreas()+"\n IdSupplier: "+params.getIdSupplier()
		+"\n IdCompany: "+params.getIdCompany()+"\n OnlyFinished: "+params.isOnlyFinished());
	
		if(params.getIdCompany() >0)
			sql.append("INNER JOIN REQUISITIONFINANCIALENTITY f ON f.IdRequisition = r.IdRequisition");

		sql.append(" where r.status"+( params.isOnlyFinished() ? "='REQUISITION_CLOSE' ":" is not null "));

		if(params.getIdUser()>0) 
			sql.append("and r.IdApplicant =:idApplicant ");

		if(params.getIdsAreas() != null) {
		    sql.append("and r.IdAreaTender IN (" + params.getIdsAreas() + ") ");
		 } else if (params.getIdArea() > 0) {
		    sql.append("and r.IdAreaTender IN (" + params.getIdArea() + ") ");
		 }

		if(params.getIdSupplier() >0) 
			sql.append("and r.IdSupplier =:idSupplier ");

		if(params.getIdCompany() >0) 
			sql.append("and f.IdFinancialEntity =:idCompany ");

		if(params.isOnlyFinished()) {
			sql.append("and ValidityEndDate BETWEEN :startDate and :endDate");
		}
		sql.append("GROUP BY r.IdRequisition, u.IdUser, CONCAT(u.Name, ' ', u.FirstLastName, ' ', u.SecondLastName), c.Name, s.IdPersonality, s.CompanyName, s.CommercialName, s.Rfc, a.Name, st.Name, r.ApplicationDate ");
		sql.append(" order by r.IdRequisition");
		
		return sql.toString();
	}

	private String sqlReporteEstatusTiempoPoliticaFechas(GraficaStatusDTO params) {
		StringBuilder query = new StringBuilder();
		this.buildSelectAllDistinctForTrayQuery2(query);
		this.buildFindRequisitionsForTrayJoinsQuery2(query);
		this.buildFindRequisitionsForTrayFiltersQuery2(query);
//		query.append("ORDER BY TURN.TurnDate DESC");
		
		return query.toString();
//		return null;
	}
	private void buildSelectAllDistinctForTrayQuery2(final StringBuilder query) {
//		query.append("SELECT COUNT(*)  ");
//		query.append("FROM ( ");
		query.append("SELECT DISTINCT SUPPLIER.CompanyName AS SupplierName, DOCUMENTTYPE.Name AS DocumentTypeName,");
		query.append("REQUISITION.IdRequisition, REQUISITION.ApplicationDate, REQUISITION.Status, ");
		query.append(
				"CONCAT (USERLAWYER.Name, ' ' , USERLAWYER.FirstLastName, ' ', USERLAWYER.SecondLastName) As LawyerName, ");
		query.append("TURN.Turn AS Retry, COMMENTS.CommentText AS Comment, TURN.TurnDate, ");
		query.append("REQUISITION.ContractType as contractType, ");
		query.append("AREA.Name as area, ");
		query.append("unit.name as unidad, "); // FIXME ENTITY

		this.buildSemaphoreFields2(query);
		this.buildRequisitionJoins2(query);
	}
	private void buildSemaphoreFields2(final StringBuilder query) {
		final SimpleDateFormat toDateTimeFormat = new SimpleDateFormat(DATABASE_DATE_FORMAT);
		final String formatedTodayDate = SINGLE_QUOTE + toDateTimeFormat.format(new Date()) + SINGLE_QUOTE;
		query.append("CASE ");
		query.append(WHEN).append(
				this.databaseUtils.buildDateDiffSSqlFunction(formatedTodayDate, TURN_DATE_PLUS_TURN_ATTENTION_DAYS));
		query.append(" < REDSEMAPHORE.RedSemaphore");
		query.append(" * TURN.AttentionDays THEN 'RED' ");
		query.append(WHEN).append(
				this.databaseUtils.buildDateDiffSSqlFunction(formatedTodayDate, TURN_DATE_PLUS_TURN_ATTENTION_DAYS));
		query.append(" < YELLOWSEMAPHORE.YellowSemaphore");
		query.append(" * TURN.AttentionDays THEN 'YELLOW' ");
		query.append("ELSE 'GREEN' END AS Semaphore, FINANCIALENTITY.Name AS Empresa, ");
		query.append("TURN.Turn AS Turn, TURN.AttentionDays AS AttentionDays ");
	}
	private void buildRequisitionJoins2(final StringBuilder query) {
		query.append("FROM REQUISITION ");
		query.append(
				" LEFT JOIN REQUISITIONFINANCIALENTITY ON REQUISITIONFINANCIALENTITY.IdRequisition =  REQUISITION.IdRequisition ");
		query.append(
				" LEFT JOIN FINANCIALENTITY ON FINANCIALENTITY.IdFinancialEntity = REQUISITIONFINANCIALENTITY.IdFinancialEntity ");
		query.append(" LEFT JOIN SUPPLIER ON REQUISITION.IdSupplier = SUPPLIER.IdSupplier ");
		query.append("LEFT JOIN DOCUMENTTYPE ON REQUISITION.IdDocumentType = DOCUMENTTYPE.IdDocumentType ");
		query.append("LEFT JOIN SCREEN ON REQUISITION.Status = SCREEN.FlowStatus ");
		query.append("LEFT JOIN AREA ON REQUISITION.IdAreaTender = AREA.IdArea ");
		query.append("LEFT JOIN unit ON REQUISITION.IdUnit = unit.idUnit ");

	}
	private void buildFindRequisitionsForTrayJoinsQuery2(final StringBuilder query) {
		query.append(
				"INNER JOIN REQUISITIONSTATUSTURN TURN ON REQUISITION.IdRequisition = TURN.IdRequisition AND REQUISITION.Status = TURN.Status ");
		query.append("LEFT JOIN REQUISITION LAWYERSUBDIRECTOR ");
		query.append("ON LAWYERSUBDIRECTOR.IdRequisition = REQUISITION.IdRequisition ");
		query.append("LEFT JOIN USERS ON USERS.IdUser = REQUISITION.IdApplicant ");
		query.append("LEFT JOIN USERS USERLAWYER ON USERLAWYER.IdUser = REQUISITION.IdLawyer ");
		query.append("LEFT JOIN REQUISITIONUSERSVOBO USERSVOBO ");
		query.append("ON USERSVOBO.IdRequisition = REQUISITION.IdRequisition ");
		query.append("LEFT JOIN REQUISITION USERSVOBOREQUISITION ");
		query.append("ON USERSVOBOREQUISITION.IdRequisition = USERSVOBO.IdRequisition ");
		query.append("LEFT JOIN COMMENTS ON COMMENTS.IdRequisition = REQUISITION.IdRequisition ");
		query.append("AND COMMENTS.FlowStatus = :Status ");
		query.append("LEFT JOIN (SELECT IdRequisition, MAX(CreationDate) AS CreationDate FROM COMMENTS WHERE ");
		query.append("FlowStatus = :Status GROUP BY ");
		query.append("IdRequisition) RECENT_COMMENT ON COMMENTS.IdRequisition = RECENT_COMMENT.IdRequisition,");
		query.append(
				"(SELECT CAST(Value AS DECIMAL(3,2)) AS RedSemaphore FROM CONFIGURATION WHERE Name = 'RED_SEMAPHORE_PERCENTAGE') REDSEMAPHORE,");
		query.append(
				"(SELECT CAST(Value AS DECIMAL(3,2)) AS YellowSemaphore FROM CONFIGURATION WHERE Name = 'YELLOW_SEMAPHORE_PERCENTAGE') YELLOWSEMAPHORE ");
	}
	private void buildFindRequisitionsForTrayFiltersQuery2(final StringBuilder query) {
		query.append("WHERE REQUISITION.Status = :Status ");
		query.append("AND (COMMENTS.CreationDate = RECENT_COMMENT.CreationDate OR COMMENTS.CreationDate IS NULL) AND ");
//		this.buildInboxFilter2(query);
		query.append(
				"TURN.TurnDate = (SELECT MAX(TurnDate) FROM REQUISITIONSTATUSTURN TURNDATE WHERE TURNDATE.IdRequisition = REQUISITION.IdRequisition) AND (");
		query.append(
				"( :Status = REQUISITION.Status AND REQUISITION.Status IN (");
		query.append(this.databaseUtils.arrayToTableFunc(
				"SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'APPLICANT_BUSY_STATUS'"));
		query.append(
				")) OR ( :Status = REQUISITION.Status AND REQUISITION.Status IN (");
		query.append(this.databaseUtils.arrayToTableFunc(
				"SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'APPLICANTSUBDIRECTOR_BUSY_STATUS'"));
		query.append(
				")) OR (:Status = LAWYERSUBDIRECTOR.Status AND LAWYERSUBDIRECTOR.Status IN (");
		query.append(this.databaseUtils.arrayToTableFunc(
				"SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'LAWYERSUBDIRECTOR_BUSY_STATUS'"));
		query.append(
				")) OR (:Status = REQUISITION.Status AND REQUISITION.Status IN (");
		query.append(this.databaseUtils
				.arrayToTableFunc("SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'LAWYER_BUSY_STATUS'"));
		query.append(
				")) OR (USERSVOBO.IsVoBoGiven = 0 AND :Status = USERSVOBOREQUISITION.Status AND USERSVOBOREQUISITION.Status IN (");
		query.append(this.databaseUtils.arrayToTableFunc(
				"SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'USERSVOBOREQUISITION_BUSY_STATUS'"));
		query.append(
				")) OR ((REQUISITION.IdEvaluator = :IdUser OR :isUserFiltered = 0) AND :IdFlow = REQUISITION.IdFlow AND :Status = REQUISITION.Status AND REQUISITION.Status IN (");
		query.append(this.databaseUtils.arrayToTableFunc(
				"SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'EVALUATOR_BUSY_STATUS'"));
		query.append("))) ");
	}

	
}
