package mx.pagos.admc.contracts.daos;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.DatabaseUtils;
import mx.pagos.admc.contracts.interfaces.Noticeable;
import mx.pagos.admc.contracts.structures.Notice;
import mx.pagos.general.constants.QueryConstants;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class NoticesDAO implements Noticeable {
    private static final String DATABASE_DATE_FORMAT = "yyyy-MM-dd";
    private static final String SINGLE_QUOTE = "'";
    private static final String UPDATE_NOTICE_SET = QueryConstants.UPDATE + TableConstants.TABLE_NOTICE
            + QueryConstants.SET;
    private static final String FROM_NOTICE = QueryConstants.FROM + TableConstants.TABLE_NOTICE;
    private static final String DELETE_FROM_NOTICE = QueryConstants.DELETE_FROM + TableConstants.TABLE_NOTICE;
    private static final String INSERT_INTO_NOTICE = QueryConstants.INSERT_INTO + TableConstants.TABLE_NOTICE
            + QueryConstants.LEFT_BRACES;
    @Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;
    
    @Autowired
    private DatabaseUtils databaseUtils;
    
	@Override
	public Integer saveOrUpdate(final Notice notice) throws DatabaseException {
	    return notice.getIdNotice() == null ? this.insertNotice(notice) : this.updateNotice(notice);
	}
	
	private Integer insertNotice(final Notice notice) throws DatabaseException {
	    try {
	        final MapSqlParameterSource namedParameters = this.createInsertNoticenamedParameters(notice);
	        final KeyHolder keyHolder = new GeneratedKeyHolder();
	        this.namedjdbcTemplate.update(this.buildInsertNoticeQuery(), namedParameters, keyHolder, 
	                new String[]{"IdNotice"});
	        return keyHolder.getKey().intValue();
	    } catch (DataAccessException dataAccessException) {
	        throw new DatabaseException("Error al insertar el aviso", dataAccessException);
	    }
	}
	
	private Integer updateNotice(final Notice notice) throws DatabaseException {
	    try {
	        final MapSqlParameterSource namedParameters = this.createUpdatetNoticenamedParameters(notice);
	        this.namedjdbcTemplate.update(this.buildUpdateUserQuery(), namedParameters);
	        return notice.getIdNotice();
	    } catch (DataAccessException dataAccessException) {
	        throw new DatabaseException("Error al Intentar Modificar Aviso", dataAccessException);
	    }
	}
	
	@Override
	public List<Notice> findByAvailable() throws DatabaseException {
	    try {
	        return this.namedjdbcTemplate.query(this.buildFindByAvailableQuery(),
	                new BeanPropertyRowMapper<Notice>(Notice.class));
	    } catch (DataAccessException dataAccessException) {
	        throw new DatabaseException("Error al obtener los avisos disponibles", dataAccessException);
	    }
	}

    @Override
	public Notice findByNoticeId(final Integer idNotice) throws DatabaseException {
	    try {
	        final MapSqlParameterSource namedParameters = this.createFindByUserIdNamedParameters(idNotice);
	        return this.namedjdbcTemplate.queryForObject(this.buildFindNoticeByIdQuery(),
	                namedParameters, new BeanPropertyRowMapper<Notice>(Notice.class));
	    } catch (DataAccessException dataAccessException) {
	        final Throwable cause = dataAccessException.getMostSpecificCause();
	        final Throwable exception = EmptyResultDataAccessException.class.equals(cause.getClass()) ? 
	                cause : dataAccessException;
	        throw new DatabaseException("Error al Recuperar el Aviso", exception);
	    }
	}
    
    private String buildFindByAvailableQuery() {
        final StringBuilder query = new StringBuilder();
        final SimpleDateFormat toDateTimeFormat = new SimpleDateFormat(DATABASE_DATE_FORMAT);
        final String formatedTodayDate = SINGLE_QUOTE + toDateTimeFormat.format(new Date()) + SINGLE_QUOTE;
        this.buildSelectAllQuery(query);
        query.append(" WHERE DueDate >= ").append(formatedTodayDate);
        query.append(" ORDER BY DueDate ASC ");
        return query.toString();
    }

    private MapSqlParameterSource createUpdatetNoticenamedParameters(final Notice notice) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_NOTICE, notice.getIdNotice());
        this.setNoticeNamedParameters(notice, namedParameters);
        return namedParameters;
    }

    private MapSqlParameterSource createInsertNoticenamedParameters(
            final Notice notice) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        this.setNoticeNamedParameters(notice, namedParameters);
        return namedParameters;
    }

    private String buildUpdateUserQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(UPDATE_NOTICE_SET);
        query.append(TableConstants.NOTICE_TYPE + QueryConstants.EQUAL_TAG + TableConstants.NOTICE_TYPE);
        query.append(QueryConstants.COMMA);
        query.append(TableConstants.CREATION_DATE + QueryConstants.EQUAL_TAG + TableConstants.CREATION_DATE);
        query.append(QueryConstants.COMMA);
        query.append(TableConstants.DUE_DATE + QueryConstants.EQUAL_TAG + TableConstants.DUE_DATE);
        query.append(QueryConstants.SPACE + QueryConstants.WHERE);
        query.append(TableConstants.ID_NOTICE + QueryConstants.EQUAL_TAG + TableConstants.ID_NOTICE);
        return query.toString();
    }

    private String buildInsertNoticeQuery() {
	    final StringBuilder query = new StringBuilder();
        query.append(INSERT_INTO_NOTICE);
        final SimpleDateFormat toDateTimeFormat = new SimpleDateFormat(DATABASE_DATE_FORMAT);
        final Date  today = new Date();
        final String formatedTodayDate = SINGLE_QUOTE + toDateTimeFormat.format(new Date(today.getTime() - (24*60*60*1000))) + SINGLE_QUOTE;
        this.buildSelectAllNonPrimaryKeyFields(query);
        query.append(QueryConstants.RIGHT_BRACES);
        query.append(QueryConstants.VALUES_TAG);
        query.append(TableConstants.NOTICE_TYPE).append(QueryConstants.COMMA).append("CAST(").append(formatedTodayDate);
        query.append(" AS DATE), :");
        query.append(TableConstants.DUE_DATE + QueryConstants.RIGHT_BRACES);
        return query.toString();
    }

    private void buildSelectAllNonPrimaryKeyFields(final StringBuilder query) {
        query.append(TableConstants.NOTICE_TYPE + QueryConstants.COMMA);
        query.append(TableConstants.CREATION_DATE + QueryConstants.COMMA);
        query.append(TableConstants.DUE_DATE + QueryConstants.SPACE);
    }

    private void setNoticeNamedParameters(final Notice notice, final MapSqlParameterSource namedParameters) {
        namedParameters.addValue(TableConstants.NOTICE_TYPE, notice.getNoticeType());
        namedParameters.addValue(TableConstants.CREATION_DATE, notice.getCreationDate());
        namedParameters.addValue(TableConstants.DUE_DATE, notice.getDueDate());
    }

    private String buildFindNoticeByIdQuery() {
            final StringBuilder query = new StringBuilder();
            this.buildSelectAllQuery(query);
            query.append(QueryConstants.WHERE);
            query.append(TableConstants.ID_NOTICE + QueryConstants.EQUAL_TAG + TableConstants.ID_NOTICE);
            return query.toString();
    }

    private void buildSelectAllQuery(final StringBuilder query) {
        query.append(QueryConstants.SELECT);
        query.append(TableConstants.ID_NOTICE + QueryConstants.COMMA);
        this.buildSelectAllNonPrimaryKeyFields(query);
        query.append(FROM_NOTICE);
    }

    private MapSqlParameterSource createFindByUserIdNamedParameters(final Integer idNotice) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        this.addIdUserNamedParameter(namedParameters, idNotice);
        return namedParameters;
    }

    private void addIdUserNamedParameter(final MapSqlParameterSource namedParameters, final Integer idNotice) {
        namedParameters.addValue(TableConstants.ID_NOTICE, idNotice);
    }
    
    private String buildDeleteByIdQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(DELETE_FROM_NOTICE);
        query.append(QueryConstants.WHERE);
        query.append(TableConstants.ID_NOTICE + QueryConstants.EQUAL_TAG + TableConstants.ID_NOTICE);
        return query.toString();
    }
    
    public Integer countByavailable() {
        return this.namedjdbcTemplate.queryForObject(this.buildCountByAvailableQuery(), new MapSqlParameterSource(),
                Integer.class);
    }

    private String buildCountByAvailableQuery() {
        final StringBuilder query = new StringBuilder();
        final SimpleDateFormat toDateTimeFormat = new SimpleDateFormat(DATABASE_DATE_FORMAT);
        final String formatedTodayDate = SINGLE_QUOTE + toDateTimeFormat.format(new Date()) + SINGLE_QUOTE;
        query.append(QueryConstants.SELECT_COUNT + TableConstants.ID_NOTICE + QueryConstants.RIGHT_BRACES);
        query.append(FROM_NOTICE);
        query.append(" WHERE CAST(DueDate AS DATE) >= CAST(").append(formatedTodayDate).append(" AS DATE)");
        return query.toString();
    }

    @Override
    public void deleteById(final Integer idNotice) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByUserIdNamedParameters(idNotice);
            this.namedjdbcTemplate.update(this.buildDeleteByIdQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public List<Notice> findAll() throws DatabaseException {
        try {
            return this.namedjdbcTemplate.query(this.buildFindAll(),
                    new BeanPropertyRowMapper<Notice>(Notice.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al obtener todos los avisos", dataAccessException);
        }
    }
    
    private String buildFindAll() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        return query.toString();
    }

    @Override
    public List<Notice> findAllNoticesAvailablePaged(final Integer pagesNumber, final Integer itemsNumber) 
            throws DatabaseException {
        try {
            final String paginatedQuery = this.databaseUtils.buildPaginatedQuery(TableConstants.ID_NOTICE, 
                    this.buildFindByAvailableQuery(), pagesNumber, itemsNumber);
            System.out.println("Query find all " + paginatedQuery);
            return this.namedjdbcTemplate.query(paginatedQuery, new BeanPropertyRowMapper<Notice>(Notice.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public Long countAllNoticesAvailablesRecords() throws DatabaseException {
        try {
            final String countItems = this.databaseUtils.countTotalRows(this.buildFindByAvailableQuery());
            return this.namedjdbcTemplate.queryForObject(countItems, new MapSqlParameterSource(), Long.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
}
