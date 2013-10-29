package com.rogeriolino.jexporter.db.jdbc;

/**
 *
 * @author Rogerio Lino <rogeriolino@gmail.com>
 */
public class PostgreSqlConnection extends JdbcConnection {

    @Override
    protected String driver() {
        return "org.postgresql.Driver";
    }
    
    @Override
    protected String url(String host, int port, String dbname) {
        return "postgresql://" + host + ":" + port + "/" + dbname;
    }
    
}
