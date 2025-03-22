package mx.pagos.admc.contracts.daos;


import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import mx.engineer.utils.date.DateUtils;
import mx.pagos.admc.contracts.interfaces.ContratosCelebradosInterface;
import mx.pagos.admc.contracts.structures.ContratosCelebrados;
import mx.pagos.admc.contracts.structures.FiltrosGrafica;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class ContratosCelebradosDAO implements ContratosCelebradosInterface{
	private static final Logger LOG = Logger.getLogger(ContratosCelebradosDAO.class);
	
	@Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> obtnerListaContratosCelebrados(FiltrosGrafica params) throws DatabaseException{
		try {
			MapSqlParameterSource param = creaParametrosFiltros(params, false);
			return this.namedJdbcTemplate.query(sqlSolicitud(params, false), 
					param, new BeanPropertyRowMapper(ContratosCelebrados.class));
		} catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al recuperar la cantidad de solicitudes por estatus", dataAccessException);
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> obtnerListaTotalContratosCelebrados(FiltrosGrafica params) throws DatabaseException{
		try {
			MapSqlParameterSource param = creaParametrosFiltros(params, false);
			return this.namedJdbcTemplate.query(sqlTotalSolicitudes(params, false), 
					param, new BeanPropertyRowMapper(ContratosCelebrados.class));
		} catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al recuperar la cantidad de solicitudes por estatus", dataAccessException);
		}
	}
	private String sqlSolicitud(FiltrosGrafica params, boolean count) {
		StringBuilder sql = new StringBuilder();
		sql.append("select ");
		if(count)
			sql.append("count(*) ");
		else {
			sql.append("r.IdRequisition as idRequisition, CONCAT(u.Name,' ', u.FirstLastName,' ',  u.SecondLastName) as nombreSolicitante, ");
			sql.append("CASE ");
			sql.append("WHEN s.IdPersonality =1 THEN s.CommercialName " );
			sql.append("WHEN s.IdPersonality =2 THEN s.CompanyName " );
			sql.append("WHEN s.IdPersonality =3 THEN s.CommercialName " );
			sql.append("ELSE s.CompanyName ");
			sql.append("END as nombreProveedor, " ); 
			sql.append("s.Rfc as rfc, r.Status as status, r.ValidityStartDate as fechaInicioContrato, r.ValidityEndDate as fechaFinContrato, "); 
			sql.append("r.isExpiredAttended as expiredAttended ");
		}
		
		sql.append("from REQUISITION r ");
		sql.append("inner join USERS u on r.IdApplicant = u.IdUser "); 
		sql.append("inner join CATALOGDOCTYPE c on r.IdDocument = c.IdDocument " ); 
		sql.append("inner join SUPPLIER s on r.IdSupplier = s.IdSupplier ");
		
		if(params.getIdCompany() >0) {
			LOG.info("////////////// ENTRA AL IDCOMPANY :::::::::: ");
			sql.append("INNER JOIN REQUISITIONFINANCIALENTITY f ON f.IdRequisition = r.IdRequisition");
			
//		sql.append(" where r.status IN ('REQUISITION_CLOSE', 'CANCELED_CONTRACT') ");
		}
		sql.append(" where r.status IN ('REQUISITION_CLOSE', 'CANCELED_CONTRACT') ");
		if(params.getIdRequisition() >0) {
			sql.append("and r.IdRequisition =:idRequisition ");
		}
		if(params.getIdUser()>0) { 
			sql.append("and r.IdApplicant =:idApplicant ");
		}
		if(params.getIdArea()>0) {
			sql.append("and r.idArea =:idArea ");
		}
		if(params.getIdSupplier() >0) {
			sql.append("and r.IdSupplier =:idSupplier ");
		}
		if(params.getIdCompany() >0) {
			sql.append("and f.IdFinancialEntity =:idCompany ");
		}
		if(params.getStartDate() != null && params.getEndDate() != null ) {
			sql.append("and ValidityEndDate BETWEEN :startDate and :endDate ");
		}
		if(!count)
			sql.append("ORDER BY r.IdRequisition OFFSET :offset ROWS FETCH NEXT :next ROWS ONLY");
		
		
		return sql.toString();
	}
		StringBuilder sql = new StringBuilder();
		private String sqlTotalSolicitudes(FiltrosGrafica params, boolean count) {
			StringBuilder sql = new StringBuilder();
			sql.append("select ");
			if(count)
				sql.append("count(*) ");
			else {
		sql.append("r.IdRequisition as idRequisition, CONCAT(u.Name,' ', u.FirstLastName,' ',  u.SecondLastName) as nombreSolicitante, ");
		sql.append("CASE ");
		sql.append("WHEN s.IdPersonality =1 THEN s.CommercialName " );
		sql.append("WHEN s.IdPersonality =2 THEN s.CompanyName " );
		sql.append("WHEN s.IdPersonality =3 THEN s.CommercialName " );
		sql.append("ELSE s.CompanyName ");
		sql.append("END as nombreProveedor, " ); 
		sql.append("s.Rfc as rfc, r.Status as status, r.ValidityStartDate as fechaInicioContrato, r.ValidityEndDate as fechaFinContrato, "); 
		sql.append("r.isExpiredAttended as expiredAttended ");
			}
		
		sql.append("from REQUISITION r ");
		sql.append("inner join USERS u on r.IdApplicant = u.IdUser "); 
		sql.append("inner join CATALOGDOCTYPE c on r.IdDocument = c.IdDocument " ); 
		sql.append("inner join SUPPLIER s on r.IdSupplier = s.IdSupplier ");
		LOG.info("PARAM SON :::::::"+params.toString());
		if(params.getIdCompany() >0) {
			sql.append("INNER JOIN REQUISITIONFINANCIALENTITY f ON f.IdRequisition = r.IdRequisition");
			
//		sql.append(" where r.status IN ('REQUISITION_CLOSE', 'CANCELED_CONTRACT') ");
		}
		sql.append(" where r.status IN ('REQUISITION_CLOSE', 'CANCELED_CONTRACT') ");
		if(params.getIdRequisition() >0) {
			sql.append("and r.IdRequisition =:idRequisition ");
		}
		if(params.getIdUser()>0) {
			sql.append("and r.IdApplicant =:idApplicant ");
		}
		if(params.getIdArea()>0) {
			sql.append("and r.idArea =:idArea ");
		}
		if(params.getIdSupplier() >0) {
			sql.append("and r.IdSupplier =:idSupplier ");
		}
		if(params.getIdCompany() >0) {
			sql.append("and f.IdFinancialEntity =:idCompany ");
		}
		if(params.getStartDate() != null && params.getEndDate() != null ) {
			sql.append("and ValidityEndDate BETWEEN :startDate and :endDate ");
		}
		if(!count) {
//			sql.append("ORDER BY r.IdRequisition OFFSET :offset ROWS FETCH NEXT :next ROWS ONLY");
			sql.append("ORDER BY r.IdRequisition ASC");
		}
		
		return sql.toString();
	}
	
	private MapSqlParameterSource creaParametrosFiltros(FiltrosGrafica params, boolean count) {
		MapSqlParameterSource param = new MapSqlParameterSource();
		if(params.getIdUser()>0) 
			param.addValue("idApplicant", params.getIdUser());
		
		if(params.getIdRequisition()>0) 
			param.addValue("idRequisition", params.getIdRequisition());
		
		if(params.getIdArea()>0) 
			param.addValue("idArea", params.getIdArea());
		
		
		if(params.getIdSupplier() >0) 
			param.addValue("idSupplier", params.getIdSupplier());
		
		if(params.getIdCompany() >0)
			param.addValue("idCompany", params.getIdCompany());
		
		if(params.getStartDate() != null && params.getEndDate() != null ) {
			param.addValue("startDate", DateUtils.dateToString("yyyy-MM-dd", params.getStartDate()));
			param.addValue("endDate", DateUtils.dateToString("yyyy-MM-dd", params.getEndDate()));
		}
		
		if(!count) {
			param.addValue("offset", (params.getPageIndex()*params.getPageSize()));
			param.addValue("next", params.getPageSize());
		}
			
		return param;
	}
	
	@Override
	public  int selectCountPagination(FiltrosGrafica params) throws DatabaseException {
		try {
			MapSqlParameterSource param = creaParametrosFiltros(params, true);
			return this.namedJdbcTemplate.queryForObject(sqlSolicitud(params, true), param,
					Integer.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al recuperar la cantidad de solicitudes por estatus",
					dataAccessException);
		}
	}
	   

}
