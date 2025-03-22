package mx.pagos.security.rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import mx.pagos.security.extractors.MenuProfileResultSetExtractor;

public class MenuProfileRowMapper implements RowMapper<String[]> {

    @Override
    public final String[] mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final MenuProfileResultSetExtractor menuProfileResultSetExtractor = new MenuProfileResultSetExtractor();
        return menuProfileResultSetExtractor.extractData(rs);
    }
}
