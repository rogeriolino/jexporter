package com.rogeriolino.jexporter.db.jdbc;

/**
 *
 * @author Rogerio Lino <rogeriolino@gmail.com>
 */
public class MsSqlConnection extends JdbcConnection {

    protected String driver() {
        return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    }
    
    protected String url(String host, int port, String dbname) {
        return "sqlserver://" + host + ":" + port + ";databaseName=" + dbname;
    }
    
}
