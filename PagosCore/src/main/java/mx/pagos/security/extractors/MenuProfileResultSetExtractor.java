package mx.pagos.security.extractors;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class MenuProfileResultSetExtractor implements ResultSetExtractor<String[]> {

    @Override
    public final String[] extractData(final ResultSet rs) throws SQLException, DataAccessException {
        final int menuProfileArraySize = 3;
        String[] menuProfile = new String[menuProfileArraySize];
        menuProfile = new String[menuProfileArraySize];
        menuProfile[0] = rs.getString("FactoryName");
        menuProfile[1] = rs.getString("Level");
        menuProfile[2] = rs.getString("FactoryNameParent");
        return menuProfile;
    }
}
