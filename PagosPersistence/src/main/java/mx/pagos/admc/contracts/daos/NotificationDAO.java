package mx.pagos.admc.contracts.daos;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.Notifiable;
import mx.pagos.admc.contracts.structures.Notification;
import mx.pagos.general.constants.QueryConstants;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class NotificationDAO implements Notifiable {
	
	private static final String DATABASE_DATE_FORMAT = "yyyy-MM-dd";
	private static final String SINGLE_QUOTE = "'";
	private static final String FROM_NOTIFICATION = QueryConstants.FROM + TableConstants.TABLE_NOTIFICATION;
	
	@Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;

    @Override
    public List<Notification> findNotificationsByIdUser(final Integer idUser) throws DatabaseException {
        try {
            return this.namedjdbcTemplate.query(this.buildFindByAvailableQuery(idUser),
            		                            new BeanPropertyRowMapper<Notification>(Notification.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al obtener los comentarios", dataAccessException);
        }
    }
    
    @Override
    public Integer saveNotification (Notification notification) throws DatabaseException{
    	try {
    		  final MapSqlParameterSource namedParameters = this.createSaveNamedParameters(notification);
  	        final KeyHolder keyHolder = new GeneratedKeyHolder();
  	        this.namedjdbcTemplate.update(this.buildInsertQuery(), namedParameters, keyHolder, 
  	                new String[]{"IdNotification"});
  	        return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
        	return null;
//            throw new DatabaseException("Error al obtener los comentarios", dataAccessException);
        }
    }
    
    public void updateNotification (Integer idNotification, boolean unread) throws DatabaseException {
    	try {
  		  final MapSqlParameterSource namedParameters = this.createUpdateNamedParameters(idNotification, unread);
	        final KeyHolder keyHolder = new GeneratedKeyHolder();
	        this.namedjdbcTemplate.update(this.buildUpdateQuery(), namedParameters, keyHolder, 
	                new String[]{"IdNotification"});
      } catch (DataAccessException dataAccessException) {
          throw new DatabaseException("Error al obtener los comentarios", dataAccessException);
      }
    }
    
    private String buildFindByAvailableQuery (final Integer idUser) {
        final StringBuilder query = new StringBuilder();
        final SimpleDateFormat toDateTimeFormat = new SimpleDateFormat(DATABASE_DATE_FORMAT);
        final String formatedTodayDate = SINGLE_QUOTE + toDateTimeFormat.format(new Date()) + SINGLE_QUOTE;
        this.buildSelectAllQuery(query);
        query.append(" WHERE expirationDate >= ").append(formatedTodayDate);
        query.append(" AND IdUser = " + idUser.toString());
        query.append(" ORDER BY unread, createDate ASC ");
        return query.toString();
    }
    
    private void buildSelectAllQuery(final StringBuilder query) {
        query.append(QueryConstants.SELECT);
        query.append(TableConstants.ID_NOTIFICATION + QueryConstants.COMMA);
        this.buildSelectAllNonPrimaryKeyFields(query);
        query.append(FROM_NOTIFICATION);
    }
    
    private void buildSelectAllNonPrimaryKeyFields(final StringBuilder query) {
        query.append(TableConstants.MESSAGE + QueryConstants.COMMA);
        query.append(TableConstants.UNREAD + QueryConstants.SPACE);
    }
    
    private MapSqlParameterSource createSaveNamedParameters(Notification notificacion) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_USER, notificacion.getIdUser());
        namedParameters.addValue(TableConstants.MESSAGE, notificacion.getMessage());
        namedParameters.addValue(TableConstants.UNREAD, notificacion.isUnread() ? 1 : 0);
        namedParameters.addValue(TableConstants.CREATE_DATE, notificacion.getCreateDate());
        namedParameters.addValue(TableConstants.EXPIRATION_DATE, notificacion.getExpirationDate());
        return namedParameters;
    }

    private String buildInsertQuery() {
	    final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO NOTIFICATION ");
        query.append("(IdUser, Message, CreateDate, ExpirationDate, unread)");
        query.append("VALUES (:IdUser, :message, :createDate, :expirationDate, :unread)");
        return query.toString();
    }
    
    private MapSqlParameterSource createUpdateNamedParameters(Integer idNotification, boolean unread) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_NOTIFICATION, idNotification);
        namedParameters.addValue(TableConstants.UNREAD, unread ? 1 : 0);
        return namedParameters;
    }
    
    private String buildUpdateQuery() {
	    final StringBuilder query = new StringBuilder();
        query.append("UPDATE NOTIFICATION ");
        query.append("SET unread =:unread ");
        query.append("WHERE IdNotification =:IdNotification");
        return query.toString();
    }
}
