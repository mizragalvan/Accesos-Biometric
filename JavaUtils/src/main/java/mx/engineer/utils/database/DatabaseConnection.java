package mx.engineer.utils.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;

public class DatabaseConnection {
	private static final String BAD_PASSWORD = "BAD_PASSWORD";
    private static final String BAD_USERNAME = "BAD_USERNAME";
    private static final String BAD_URL = "BAD_URL";
    private static final String PASSWORD = "PASSWORD";
    private static final String USERNAME = "USERNAME";
    private static final String URL = "URL";
    private Properties dbProperties;
	private FileInputStream fileInputStream;
	private DataSource datasource;	
	
	
	public final DataSource getConnection(final DatabaseEnum database, final String path) throws IOException {
		this.dbProperties = new Properties();
		this.fileInputStream = new FileInputStream(path);
		this.dbProperties.load(this.fileInputStream);
		switch (database) {
		    case BAD_MySQL:
                this.datasource = this.buildBadMysqlDatasource();
                break;
			case MySQL:
			    this.datasource = this.buildMysqlDatasource();
			    break;
			case BAD_SQLServer:
			    this.datasource = this.buildBadSqlServerDatasource();
			    break;
			case SQLServer:
			default:
				this.datasource = this.buildSqlServerDatasource();
				break;
		}
		return this.datasource;
	}

    private DataSource buildSqlServerDatasource() {
        final SQLServerDataSource sqlServerDataSource = new SQLServerDataSource();
        sqlServerDataSource.setURL(this.dbProperties.getProperty(URL));
        sqlServerDataSource.setUser(this.dbProperties.getProperty(USERNAME));
        sqlServerDataSource.setPassword(this.dbProperties.getProperty(PASSWORD));
        return sqlServerDataSource;
    }

    private DataSource buildBadSqlServerDatasource() {
        final SQLServerDataSource sqlServerDataSource = new SQLServerDataSource();
        sqlServerDataSource.setURL(this.dbProperties.getProperty(BAD_URL));
        sqlServerDataSource.setUser(this.dbProperties.getProperty(BAD_USERNAME));
        sqlServerDataSource.setPassword(this.dbProperties.getProperty(BAD_PASSWORD));
        return sqlServerDataSource;
    }

    private MysqlDataSource buildMysqlDatasource() {
        final MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setUrl(this.dbProperties.getProperty(URL));
        mysqlDataSource.setUser(this.dbProperties.getProperty(USERNAME));
        mysqlDataSource.setPassword(this.dbProperties.getProperty(PASSWORD));
        return mysqlDataSource;
    }

    private MysqlDataSource buildBadMysqlDatasource() {
        final MysqlDataSource badMysqlDataSource = new MysqlDataSource();
        badMysqlDataSource.setUrl(this.dbProperties.getProperty(BAD_URL));
        badMysqlDataSource.setUser(this.dbProperties.getProperty(BAD_USERNAME));
        badMysqlDataSource.setPassword(this.dbProperties.getProperty(BAD_PASSWORD));
        return badMysqlDataSource;
    }
}
