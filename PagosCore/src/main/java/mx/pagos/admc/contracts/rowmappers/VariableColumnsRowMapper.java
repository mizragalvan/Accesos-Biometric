package mx.pagos.admc.contracts.rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

public class VariableColumnsRowMapper implements RowMapper<Map<String, String>> {

    @Override
    public final Map<String, String> mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final Integer columnsCount = rs.getMetaData().getColumnCount();
        final Map<String, String> valuesMap = new HashMap<String, String>();
        for (int index = 1; index <= columnsCount; index++) {
            valuesMap.put(rs.getMetaData().getColumnName(index), rs.getString(index));
        }
        return valuesMap;
    }
}
