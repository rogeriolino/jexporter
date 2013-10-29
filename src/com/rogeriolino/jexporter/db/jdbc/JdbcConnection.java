package com.rogeriolino.jexporter.db.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Rogerio Lino <rogeriolino@gmail.com>
 */
public abstract class JdbcConnection {

    private Connection conn;
    
    public void connect(String host, int port, String dbname, String username, String password) throws ClassNotFoundException, SQLException {
        Class.forName(driver());
        conn = DriverManager.getConnection("jdbc:" + url(host, port, dbname), username, password);
    }
    
    public Connection getConnection() {
        return conn;
    }
    
    public void close() throws SQLException {
        conn.close();
    }
    
    protected abstract String driver();
    protected abstract String url(String host, int port, String dbname);
    
}
