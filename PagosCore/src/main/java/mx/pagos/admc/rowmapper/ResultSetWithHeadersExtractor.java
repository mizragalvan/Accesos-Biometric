package mx.pagos.admc.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class ResultSetWithHeadersExtractor implements ResultSetExtractor<String[][]> {
    @Override
    public final String[][] extractData(final ResultSet rs) throws SQLException, DataAccessException {
        final ResultSetMetaData rsmd = rs.getMetaData();
        final int columnCount = rsmd.getColumnCount();
        final List<String[]> resultList = new ArrayList<>();
        this.addHeaders(resultList, rsmd);
        while (rs.next()) {
            final String[] row = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                row[i] = rs.getString(i + 1);
            }
            resultList.add(row);
        }
        return resultList.toArray(new String[columnCount][]);
    }

    private void addHeaders(final List<String[]> resultList, final ResultSetMetaData rsmd) throws SQLException {
        final int columnCount = rsmd.getColumnCount();
        final String[] row = new String[columnCount];
        for (int index = 1; index <= columnCount; index++) {
            row[index - 1] = rsmd.getColumnName(index);
        }
        resultList.add(row);
    }
}
